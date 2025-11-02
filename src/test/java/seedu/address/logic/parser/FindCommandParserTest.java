package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.FindCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.ClientContainsKeywordsPredicate;

class FindCommandParserTest {

    private final FindCommandParser parser = new FindCommandParser();

    @Test
    // multiple name tokens under one prefix not allowed
    void parse_multipleNames_parseFailure() {
        String userInput = "n/Alice Bob"; // multiple names in a single prefix
        assertThrows(ParseException.class, () -> parser.parse(userInput));
    }

    @Test
    // multiple tag tokens under one prefix not allowed
    void parse_multipleTags_parseFailure() {
        String userInput = "t/friend colleague"; // multiple tags
        assertThrows(ParseException.class, () -> parser.parse(userInput));
    }

    @Test
    // multiple dates or invalid date formats rejected
    void parse_multipleDates_parseFailure() {
        String userInput = "d/2025-12-15 2026-12-15";
        assertThrows(ParseException.class, () -> parser.parse(userInput));
    }

    @Test
    // invalid date format rejected
    void parse_invalidDate_parseFailure() {
        String userInput = "d/2025-13-40"; // invalid month and day
        assertThrows(ParseException.class, () -> parser.parse(userInput));
    }

    @Test
    // missing any valid prefix rejected
    void parse_missingPrefix_throwsParseException() {
        String userInput = "Alice"; // no prefix at all
        assertThrows(ParseException.class, () -> parser.parse(userInput));
    }

    @Test
    // mixed prefixes: ensure parsed predicate includes both name and tag
    void parse_nameAndTag() throws Exception {
        String userInput = "n/Alex t/friend";
        Map<String, List<String>> criteria = Map.of(
            "tag", List.of("friend")
        );
        FindCommand expectedCommand = new FindCommand(new ClientContainsKeywordsPredicate(criteria));
        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    // empty prefix for name should throw parse exception
    void parse_emptyNamePrefix() {
        String userInput = "n/";
        assertThrows(ParseException.class, () -> parser.parse(userInput));
    }

    @Test
    // empty prefix for tag should throw parse exception
    void parse_emptyTagPrefix() {
        String userInput = "t/";
        assertThrows(ParseException.class, () -> parser.parse(userInput));
    }

    @Test
    // empty prefix for date should throw parse exception
    void parse_emptyDatePrefix() {
        String userInput = "d/";
        assertThrows(ParseException.class, () -> parser.parse(userInput));
    }

    @Test
    // Test mixing empty and non-empty prefixes - expect only non-empty values in criteria
    void parse_mixedEmptyAndNonEmptyPrefixes() throws Exception {
        // Empty tag with non-empty name
        String userInput = "n/Alice t/";
        Map<String, List<String>> criteria = Map.of("tag", List.of());
        FindCommand expectedCommand = new FindCommand(new ClientContainsKeywordsPredicate(criteria));
        assertParseSuccess(parser, userInput, expectedCommand);

        // Empty date with non-empty tag
        userInput = "t/friend d/";
        Map<String, List<String>> criteria2 = Map.of("date", List.of());
        FindCommand expectedCommand2 = new FindCommand(new ClientContainsKeywordsPredicate(criteria2));
        assertParseSuccess(parser, userInput, expectedCommand2);
    }
}
