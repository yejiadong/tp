package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.VALID_DATETIME;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ID_ARRAY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ID_ARRAY2;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ID_STRINGS;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_STUDENT;
import static seedu.address.testutil.TypicalStudents.ALICE;

import java.util.HashSet;

import org.junit.jupiter.api.Test;

import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.student.Training;
import seedu.address.testutil.TypicalTraining;

public class AddStudentCommandTest {

    private Model model = new ModelManager(TypicalTraining.getTypicalAddressBook(), new UserPrefs());

    public Model getModel() {
        return model;
    }

    @Test
    public void constructor_nullStudent_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new AddStudentCommand(INDEX_FIRST_STUDENT, null));
    }

    @Test
    public void constructor_nullIndex_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new AddStudentCommand(null, VALID_ID_ARRAY));
    }

    @Test
    public void execute_studentAcceptedByModel_addSuccessful() throws Exception {
        AddStudentCommand addStudentCommand = new AddStudentCommand(INDEX_FIRST_STUDENT, VALID_ID_ARRAY);
        Training editedTraining = new Training(VALID_DATETIME, new HashSet<>());
        editedTraining.addStudent(ALICE);
        System.out.println(addStudentCommand.execute(model).getFeedbackToUser());

        String expectedMessage = String
                .format(AddStudentCommand.MESSAGE_ADD_STUDENT_SUCCESS, VALID_ID_STRINGS);
        System.out.println(expectedMessage);

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setTraining(model.getFilteredTrainingList().get(0), editedTraining);

        assertCommandSuccess(addStudentCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void equals() {
        AddStudentCommand addStudent1Command = new AddStudentCommand(INDEX_FIRST_STUDENT, VALID_ID_ARRAY);
        AddStudentCommand addStudent12Command = new AddStudentCommand(INDEX_FIRST_STUDENT, VALID_ID_ARRAY2);

        // same object -> returns true
        assertTrue(addStudent1Command.equals(addStudent1Command));

        // same values -> returns true
        AddStudentCommand addStudentCommandCopy = new AddStudentCommand(INDEX_FIRST_STUDENT, VALID_ID_ARRAY);
        assertTrue(addStudent1Command.equals(addStudentCommandCopy));

        // different types -> returns false
        assertFalse(addStudent1Command.equals(1));

        // null -> returns false
        assertFalse(addStudent1Command.equals(null));

        // different student -> returns false
        assertFalse(addStudent1Command.equals(addStudent12Command));
    }
}
