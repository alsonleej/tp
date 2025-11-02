package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.parser.ParserUtil.MESSAGE_INVALID_INDEX;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Phone;
import seedu.address.model.tag.Tag;

public class ParserUtilTest {
    private static final String INVALID_NAME = ""; // empty string (names must be non-blank)
    private static final String INVALID_PHONE = " ";
    private static final String INVALID_ADDRESS = " ";
    private static final String INVALID_EMAIL = "example.com";
    private static final String INVALID_TAG = "#friend";

    private static final String VALID_NAME = "Rachel Walker";
    private static final String VALID_PHONE = "123456";
    private static final String VALID_ADDRESS = "123 Main Street #0505";
    private static final String VALID_EMAIL = "rachel@example.com";
    private static final String VALID_TAG_1 = "friend";
    private static final String VALID_TAG_2 = "neighbour";

    private static final String WHITESPACE = " \t\r\n";

    @Test
    // EP: non-numeric index tokens
    public void parseIndex_invalidInput_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseIndex("10 a"));
    }

    @Test
    // BVA: index just above Integer.MAX_VALUE
    public void parseIndex_outOfRangeInput_throwsParseException() {
        assertThrows(ParseException.class, MESSAGE_INVALID_INDEX, ()
            -> ParserUtil.parseIndex(Long.toString(Integer.MAX_VALUE + 1)));
    }

    @Test
    // BVA: minimal valid index; EP: surrounding whitespace tolerated
    public void parseIndex_validInput_success() throws Exception {
        // No whitespaces
        assertEquals(INDEX_FIRST_PERSON, ParserUtil.parseIndex("1"));

        // Leading and trailing whitespaces
        assertEquals(INDEX_FIRST_PERSON, ParserUtil.parseIndex("  1  "));
    }

    @Test
    // EP: null name should throw NPE
    public void parseName_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseName((String) null));
    }

    @Test
    // EP: invalid name (blank/empty)
    public void parseName_invalidValue_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseName(INVALID_NAME));
    }

    @Test
    // BVA: name length exactly 100 (valid)
    public void parseName_maxLength_returnsName() throws Exception {
        String hundredAs = "A".repeat(100);
        Name expectedName = new Name(hundredAs);
        assertEquals(expectedName, ParserUtil.parseName(hundredAs));
    }

    @Test
    // BVA: name length 101 (invalid)
    public void parseName_exceedsMaxLength_throwsParseException() {
        // Test various lengths over 100 characters
        assertThrows(ParseException.class, () -> ParserUtil.parseName("A".repeat(101))); // 101 characters
        assertThrows(ParseException.class, () -> ParserUtil.parseName("A".repeat(102))); // 102 characters
        assertThrows(ParseException.class, () -> ParserUtil.parseName("A".repeat(150))); // 150 characters
        assertThrows(ParseException.class, () -> ParserUtil.parseName("A".repeat(200))); // 200 characters

        // Test with valid name pattern but exceeding length
        String longNameWithSpaces = "John " + "Doe ".repeat(50); // More than 100 characters with spaces
        assertThrows(ParseException.class, () -> ParserUtil.parseName(longNameWithSpaces));
    }

    @Test
    // EP: valid name without extra whitespace
    public void parseName_validValueWithoutWhitespace_returnsName() throws Exception {
        Name expectedName = new Name(VALID_NAME);
        assertEquals(expectedName, ParserUtil.parseName(VALID_NAME));
    }

    @Test
    // EP: valid name with surrounding whitespace trimmed
    public void parseName_validValueWithWhitespace_returnsTrimmedName() throws Exception {
        String nameWithWhitespace = WHITESPACE + VALID_NAME + WHITESPACE;
        Name expectedName = new Name(VALID_NAME);
        assertEquals(expectedName, ParserUtil.parseName(nameWithWhitespace));
    }

    @Test
    // EP: valid name with special characters (previously invalid, now valid)
    public void parseName_validValueWithSpecialCharacters_returnsName() throws Exception {
        // Test names with various special characters that were previously invalid
        Name name1 = ParserUtil.parseName("R@chel");
        assertEquals(new Name("R@chel"), name1);

        Name name2 = ParserUtil.parseName("James&");
        assertEquals(new Name("James&"), name2);

        Name name3 = ParserUtil.parseName("John*Smith");
        assertEquals(new Name("John*Smith"), name3);

        Name name4 = ParserUtil.parseName("Bob#123");
        assertEquals(new Name("Bob#123"), name4);
    }

    @Test
    // EP: null phone should throw NPE
    public void parsePhone_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parsePhone((String) null));
    }

    @Test
    // EP: invalid phone value
    public void parsePhone_invalidValue_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parsePhone(INVALID_PHONE));
    }

    @Test
    // EP: valid phone without whitespace
    public void parsePhone_validValueWithoutWhitespace_returnsPhone() throws Exception {
        Phone expectedPhone = new Phone(VALID_PHONE);
        assertEquals(expectedPhone, ParserUtil.parsePhone(VALID_PHONE));
    }

    @Test
    // BVA: phone length exactly 50 (valid)
    public void parsePhone_maxLength_returnsPhone() throws Exception {
        String fiftyOnes = "1".repeat(50);
        Phone expectedPhone = new Phone(fiftyOnes);
        assertEquals(expectedPhone, ParserUtil.parsePhone(fiftyOnes));
    }

    @Test
    // BVA: phone length 51 (invalid)
    public void parsePhone_exceedsMaxLength_throwsParseException() {
        // Test various lengths over 50 characters
        assertThrows(ParseException.class, () -> ParserUtil.parsePhone("1".repeat(51))); // 51 characters
        assertThrows(ParseException.class, () -> ParserUtil.parsePhone("1".repeat(52))); // 52 characters
        assertThrows(ParseException.class, () -> ParserUtil.parsePhone("1".repeat(100))); // 100 characters
        assertThrows(ParseException.class, () -> ParserUtil.parsePhone("1".repeat(200))); // 200 characters

        // Test with valid phone pattern but exceeding length
        String longPhoneWithFormat = "+65 " + "1".repeat(48); // 51 characters with format
        assertThrows(ParseException.class, () -> ParserUtil.parsePhone(longPhoneWithFormat));
    }

    @Test
    // EP: valid phone with surrounding whitespace trimmed
    public void parsePhone_validValueWithWhitespace_returnsTrimmedPhone() throws Exception {
        String phoneWithWhitespace = WHITESPACE + VALID_PHONE + WHITESPACE;
        Phone expectedPhone = new Phone(VALID_PHONE);
        assertEquals(expectedPhone, ParserUtil.parsePhone(phoneWithWhitespace));
    }

    @Test
    // EP: null address should throw NPE
    public void parseAddress_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseAddress((String) null));
    }

    @Test
    // EP: invalid address value
    public void parseAddress_invalidValue_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseAddress(INVALID_ADDRESS));
    }

    @Test
    // EP: valid address without whitespace
    public void parseAddress_validValueWithoutWhitespace_returnsAddress() throws Exception {
        Address expectedAddress = new Address(VALID_ADDRESS);
        assertEquals(expectedAddress, ParserUtil.parseAddress(VALID_ADDRESS));
    }

    @Test
    // EP: valid address with surrounding whitespace trimmed
    public void parseAddress_validValueWithWhitespace_returnsTrimmedAddress() throws Exception {
        String addressWithWhitespace = WHITESPACE + VALID_ADDRESS + WHITESPACE;
        Address expectedAddress = new Address(VALID_ADDRESS);
        assertEquals(expectedAddress, ParserUtil.parseAddress(addressWithWhitespace));
    }

    @Test
    // EP: null email should throw NPE
    public void parseEmail_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseEmail((String) null));
    }

    @Test
    // EP: invalid email value
    public void parseEmail_invalidValue_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseEmail(INVALID_EMAIL));
    }

    @Test
    // EP: valid email without whitespace
    public void parseEmail_validValueWithoutWhitespace_returnsEmail() throws Exception {
        Email expectedEmail = new Email(VALID_EMAIL);
        assertEquals(expectedEmail, ParserUtil.parseEmail(VALID_EMAIL));
    }

    @Test
    // EP: valid email with surrounding whitespace trimmed
    public void parseEmail_validValueWithWhitespace_returnsTrimmedEmail() throws Exception {
        String emailWithWhitespace = WHITESPACE + VALID_EMAIL + WHITESPACE;
        Email expectedEmail = new Email(VALID_EMAIL);
        assertEquals(expectedEmail, ParserUtil.parseEmail(emailWithWhitespace));
    }

    @Test
    // BVA: email length exactly 50 (valid)
    public void parseEmail_maxLength_returnsEmail() throws Exception {
        String fiftyCharEmail = "a".repeat(44) + "@b.co"; // exactly 50 chars
        Email expectedEmail = new Email(fiftyCharEmail);
        assertEquals(expectedEmail, ParserUtil.parseEmail(fiftyCharEmail));
    }

    @Test
    // BVA: email length 51 (invalid)
    public void parseEmail_exceedsMaxLength_throwsParseException() {
        // Test various lengths over 50 characters with valid email format
        assertThrows(ParseException.class, () -> ParserUtil.parseEmail(
                "a".repeat(43) + "@example.com")); // 55 characters
        assertThrows(ParseException.class, () -> ParserUtil.parseEmail(
                "user" + "1".repeat(40) + "@example.com")); // 56 characters
        assertThrows(ParseException.class, () -> ParserUtil.parseEmail(
                "a".repeat(92) + "@example.com")); // 104 characters
        assertThrows(ParseException.class, () -> ParserUtil.parseEmail(
                "a".repeat(192) + "@example.com")); // 204 characters

        // Test with valid email pattern but exceeding length - long local part
        String longEmailLocal = "user" + "1".repeat(41) + "@example.com"; // 61 characters with valid format
        assertThrows(ParseException.class, () -> ParserUtil.parseEmail(longEmailLocal));

        // Test with valid email pattern but exceeding length - long domain
        String longEmailDomain = "user@" + "example".repeat(7) + ".com"; // 57 characters with long domain
        assertThrows(ParseException.class, () -> ParserUtil.parseEmail(longEmailDomain));
    }

    @Test
    // EP: null tag should throw NPE
    public void parseTag_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseTag(null));
    }

    @Test
    // EP: invalid tag value
    public void parseTag_invalidValue_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseTag(INVALID_TAG));
    }

    @Test
    // EP: valid single tag without whitespace
    public void parseTag_validValueWithoutWhitespace_returnsTag() throws Exception {
        Tag expectedTag = new Tag(VALID_TAG_1);
        assertEquals(expectedTag, ParserUtil.parseTag(VALID_TAG_1));
    }

    @Test
    // EP: valid single tag with surrounding whitespace trimmed
    public void parseTag_validValueWithWhitespace_returnsTrimmedTag() throws Exception {
        String tagWithWhitespace = WHITESPACE + VALID_TAG_1 + WHITESPACE;
        Tag expectedTag = new Tag(VALID_TAG_1);
        assertEquals(expectedTag, ParserUtil.parseTag(tagWithWhitespace));
    }

    @Test
    // BVA: tag length exactly 50 (valid)
    public void parseTag_maxLength_returnsTag() throws Exception {
        String fiftyAs = "a".repeat(50);
        Tag expectedTag = new Tag(fiftyAs);
        assertEquals(expectedTag, ParserUtil.parseTag(fiftyAs));
    }

    @Test
    // BVA: tag length 51 (invalid)
    public void parseTag_exceedsMaxLength_throwsParseException() {
        // Test various lengths over 50 characters
        assertThrows(ParseException.class, () -> ParserUtil.parseTag("a".repeat(51))); // 51 characters
        assertThrows(ParseException.class, () -> ParserUtil.parseTag("a".repeat(52))); // 52 characters
        assertThrows(ParseException.class, () -> ParserUtil.parseTag("a".repeat(100))); // 100 characters
        assertThrows(ParseException.class, () -> ParserUtil.parseTag("a".repeat(200))); // 200 characters

        // Test with valid tag pattern (alphanumeric) but exceeding length
        String longTagWithNumbers = "tag" + "1".repeat(49); // 52 characters with numbers
        assertThrows(ParseException.class, () -> ParserUtil.parseTag(longTagWithNumbers));

        // Test with mixed alphanumeric characters
        String longTagMixed = "VIP" + "123".repeat(16); // 51 characters with mixed chars
        assertThrows(ParseException.class, () -> ParserUtil.parseTag(longTagMixed));
    }

    @Test
    // EP: null tags collection should throw NPE
    public void parseTags_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseTags(null));
    }

    @Test
    // EP: collection with at least one invalid tag
    public void parseTags_collectionWithInvalidTags_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseTags(Arrays.asList(VALID_TAG_1, INVALID_TAG)));
    }

    @Test
    // EP: empty collection returns empty set
    public void parseTags_emptyCollection_returnsEmptySet() throws Exception {
        assertTrue(ParserUtil.parseTags(Collections.emptyList()).isEmpty());
    }

    @Test
    // EP: collection with only valid tags returns set of tags
    public void parseTags_collectionWithValidTags_returnsTagSet() throws Exception {
        Set<Tag> actualTagSet = ParserUtil.parseTags(Arrays.asList(VALID_TAG_1, VALID_TAG_2));
        Set<Tag> expectedTagSet = new HashSet<Tag>(Arrays.asList(new Tag(VALID_TAG_1), new Tag(VALID_TAG_2)));

        assertEquals(expectedTagSet, actualTagSet);
    }
}
