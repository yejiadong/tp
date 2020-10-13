package seedu.address.logic.commands;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.commons.core.Messages.MESSAGE_STUDENTS_LISTED_OVERVIEW;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalStudents.CARL;
import static seedu.address.testutil.TypicalStudents.ELLE;
import static seedu.address.testutil.TypicalStudents.FIONA;
import static seedu.address.testutil.TypicalStudents.getTypicalAddressBook;

import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.Test;

import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.student.NameContainsKeywordsPredicate;
import seedu.address.model.student.PhoneMatchesPredicate;
import seedu.address.model.student.AllMatchPredicateList;

/**
 * Contains integration tests (interaction with the Model) for {@code FindCommand}.
 */
public class FindCommandTest {
    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    private Model expectedModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void equals() {
        NameContainsKeywordsPredicate firstNamePredicate =
                new NameContainsKeywordsPredicate(Collections.singletonList("first"));
        NameContainsKeywordsPredicate secondNamePredicate =
                new NameContainsKeywordsPredicate(Collections.singletonList("second"));
        PhoneMatchesPredicate firstPhonePredicate =
                new PhoneMatchesPredicate("12345678");
        PhoneMatchesPredicate secondPhonePredicate =
                new PhoneMatchesPredicate("87654321");
        AllMatchPredicateList firstAllMatchPredicateList =
                new AllMatchPredicateList(Arrays.asList(firstNamePredicate, firstPhonePredicate));
        AllMatchPredicateList secondAllMatchPredicateList =
                new AllMatchPredicateList(Arrays.asList(secondNamePredicate, secondPhonePredicate));

        FindCommand findFirstCommand = new FindCommand(firstAllMatchPredicateList);
        FindCommand findSecondCommand = new FindCommand(secondAllMatchPredicateList);

        // same object -> returns true
        assertTrue(findFirstCommand.equals(findFirstCommand));

        // same values -> returns true
        FindCommand findFirstCommandCopy = new FindCommand(firstAllMatchPredicateList);
        assertTrue(findFirstCommand.equals(findFirstCommandCopy));

        // different types -> returns false
        assertFalse(findFirstCommand.equals(1));

        // null -> returns false
        assertFalse(findFirstCommand.equals(null));

        // different student -> returns false
        assertFalse(findFirstCommand.equals(findSecondCommand));
    }

    @Test
    public void execute_zeroKeywords_noStudentFound() {
        String expectedMessage = String.format(MESSAGE_STUDENTS_LISTED_OVERVIEW, 0);
        NameContainsKeywordsPredicate namePredicate = preparePredicate(" ");
        PhoneMatchesPredicate phonePredicate = new PhoneMatchesPredicate(" ");
        AllMatchPredicateList allMatchPredicateList = new AllMatchPredicateList(Arrays.asList(namePredicate, phonePredicate));
        FindCommand command = new FindCommand(allMatchPredicateList);
        expectedModel.updateFilteredStudentList(allMatchPredicateList);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Collections.emptyList(), model.getFilteredStudentList());
    }

    @Test
    public void execute_multipleNameKeywords_multipleStudentsFound() {
        String expectedMessage = String.format(MESSAGE_STUDENTS_LISTED_OVERVIEW, 3);
        NameContainsKeywordsPredicate namePredicate = preparePredicate("Kurz Elle Kunz");
        AllMatchPredicateList allMatchPredicateList = new AllMatchPredicateList(Arrays.asList(namePredicate));
        FindCommand command = new FindCommand(allMatchPredicateList);
        expectedModel.updateFilteredStudentList(allMatchPredicateList);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Arrays.asList(CARL, ELLE, FIONA), model.getFilteredStudentList());
    }

    @Test
    public void execute_phoneKeyword_oneStudentFound() {
        String expectedMessage = String.format(MESSAGE_STUDENTS_LISTED_OVERVIEW, 1);
        PhoneMatchesPredicate phonePredicate = new PhoneMatchesPredicate("9482224");
        AllMatchPredicateList allMatchPredicateList = new AllMatchPredicateList(Arrays.asList(phonePredicate));
        FindCommand command = new FindCommand(allMatchPredicateList);
        expectedModel.updateFilteredStudentList(allMatchPredicateList);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Arrays.asList(ELLE), model.getFilteredStudentList());
    }

    @Test
    public void execute_multipleKeywords_oneStudentFound() {
        String expectedMessage = String.format(MESSAGE_STUDENTS_LISTED_OVERVIEW, 1);
        NameContainsKeywordsPredicate namePredicate = preparePredicate("Kurz Elle Kunz");
        PhoneMatchesPredicate phonePredicate = new PhoneMatchesPredicate("9482427");
        AllMatchPredicateList allMatchPredicateList = new AllMatchPredicateList(Arrays.asList(namePredicate, phonePredicate));
        FindCommand command = new FindCommand(allMatchPredicateList);
        expectedModel.updateFilteredStudentList(allMatchPredicateList);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Arrays.asList(FIONA), model.getFilteredStudentList());
    }

    @Test
    public void execute_multipleKeywords_noStudentFound() {
        String expectedMessage = String.format(MESSAGE_STUDENTS_LISTED_OVERVIEW, 0);
        NameContainsKeywordsPredicate namePredicate = preparePredicate("Kurz Elle Kunz");
        PhoneMatchesPredicate phonePredicate = new PhoneMatchesPredicate("1234567");
        AllMatchPredicateList allMatchPredicateList = new AllMatchPredicateList(Arrays.asList(namePredicate, phonePredicate));
        FindCommand command = new FindCommand(allMatchPredicateList);
        expectedModel.updateFilteredStudentList(allMatchPredicateList);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Collections.emptyList(), model.getFilteredStudentList());
    }

    /**
     * Parses {@code userInput} into a {@code NameContainsKeywordsPredicate}.
     */
    private NameContainsKeywordsPredicate preparePredicate(String userInput) {
        return new NameContainsKeywordsPredicate(Arrays.asList(userInput.split("\\s+")));
    }

}
