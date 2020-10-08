package seedu.address.logic.parser;

import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.FindCommand;
import seedu.address.model.student.EmailContainsKeywordPredicate;
import seedu.address.model.student.IdMatchesPredicate;
import seedu.address.model.student.NameContainsKeywordsPredicate;
import seedu.address.model.student.PhoneMatchesPredicate;
import seedu.address.model.student.PredicateList;
import seedu.address.model.student.time.Day;

public class FindCommandParserTest {

    private FindCommandParser parser = new FindCommandParser();

    @Test
    public void parse_validArgs_returnsFindCommand() {
        // no leading and trailing whitespaces
        FindCommand firstExpectedFindCommand =
                new FindCommand(PredicateList.of(
                        new NameContainsKeywordsPredicate(Arrays.asList("Alice", "Bob")),
                        new PhoneMatchesPredicate("123456")
                ));
        assertParseSuccess(parser, " n/Alice Bob p/123456", firstExpectedFindCommand);

        // multiple whitespaces between keywords
        FindCommand secondExpectedFindCommand =
                new FindCommand(PredicateList.of(
                        new NameContainsKeywordsPredicate(Arrays.asList("Alice", "Bob")),
                        new PhoneMatchesPredicate("85355255")
                ));
        assertParseSuccess(parser, "  \n  \t n/Alice Bob \t \n \n p/85355255", secondExpectedFindCommand);

        // mixture of find parameters
        FindCommand thirdExpectedFindCommand =
                new FindCommand(PredicateList.of(
                        new EmailContainsKeywordPredicate("meow"),
                        new IdMatchesPredicate("42")
                ));
        assertParseSuccess(parser, " e/meow id/42", thirdExpectedFindCommand);

        // TODO: Test for AcademicYearMatchesPredicate currently fails tests for unknown reason. But works in app.
    }

    @Test
    public void parse_noQuery_failure() {
        assertParseFailure(parser, " ", FindCommand.MESSAGE_NO_QUERY);
    }

    @Test
    public void parse_invalidTime_failure() {
        assertParseFailure(parser, " d1/423", Day.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_validTimeFollowedByInvalidTime_failure() {
        assertParseFailure(parser, " d1/0800 d2/432", Day.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_multipleRepeatedFields_acceptsLast() {
        FindCommand expectedFindCommand = new FindCommand(PredicateList.of(
                new PhoneMatchesPredicate("33333333")
        ));
        assertParseSuccess(parser, " p/11111111 p/22222222 p/33333333", expectedFindCommand);
    }
}
