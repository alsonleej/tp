package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class NameTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Name(null));
    }

    @Test
    public void constructor_invalidName_throwsIllegalArgumentException() {
        String invalidName = "";
        assertThrows(IllegalArgumentException.class, () -> new Name(invalidName));
    }

    @Test
    public void constructor_nameTooLong_throwsIllegalArgumentException() {
        // Test various lengths over 100 characters
        assertThrows(IllegalArgumentException.class, () -> new Name("a".repeat(101))); // 101 characters
        assertThrows(IllegalArgumentException.class, () -> new Name("a".repeat(102))); // 102 characters
        assertThrows(IllegalArgumentException.class, () -> new Name("a".repeat(150))); // 150 characters
        assertThrows(IllegalArgumentException.class, () -> new Name("a".repeat(200))); // 200 characters
        assertThrows(IllegalArgumentException.class, () -> new Name("a".repeat(1000))); // 1000 characters

        // Test with valid name pattern but exceeding length
        String longNameWithSpecialChars = "Alice" + "-".repeat(96); // 101 characters with hyphens
        assertThrows(IllegalArgumentException.class, () -> new Name(longNameWithSpecialChars));
    }

    @Test
    public void isValidName() {
        // null name
        assertThrows(NullPointerException.class, () -> Name.isValidName(null));

        // invalid name (blank/empty)
        assertFalse(Name.isValidName("")); // empty string
        assertFalse(Name.isValidName(" ")); // spaces only

        // valid name (can contain any characters, including special characters and numbers)
        assertTrue(Name.isValidName("peter jack")); // alphabets only
        assertTrue(Name.isValidName("Capital Tan")); // with capital letters
        assertTrue(Name.isValidName("David Roger Jackson Ray Jr")); // long names
        assertTrue(Name.isValidName("O'Brien")); // with apostrophe
        assertTrue(Name.isValidName("Mary-Jane")); // with hyphen
        assertTrue(Name.isValidName("Jean-Paul O'Connor")); // with both apostrophe and hyphen
        assertTrue(Name.isValidName("John/Doe")); // with forward slash
        assertTrue(Name.isValidName("John\\Doe")); // with backslash
        assertTrue(Name.isValidName("Jean-Paul O'Connor/Mary-Jane")); // with all special chars
        assertTrue(Name.isValidName("^")); // special characters only
        assertTrue(Name.isValidName("peter*")); // contains special characters
        assertTrue(Name.isValidName("12345")); // numbers only
        assertTrue(Name.isValidName("peter the 2nd")); // contains numbers
        assertTrue(Name.isValidName("David123")); // contains numbers
        assertTrue(Name.isValidName("John@Doe")); // contains @ symbol
        assertTrue(Name.isValidName("R@chel")); // contains @ symbol
        assertTrue(Name.isValidName("James&")); // contains & symbol
        assertTrue(Name.isValidName("小明")); // contains foreign symbols
        assertTrue(Name.isValidName("André")); // contains é
        assertTrue(Name.isValidName("Jennifer 8. Lee")); // mix of symbol and number
        assertTrue(Name.isValidName("😀😃😄😁😆")); // contains emojis
        assertTrue(Name.isValidName("a".repeat(100))); // exactly 100 chars (max length)
    }

    @Test
    public void constructor_nameWithSpecialCharacters_success() {
        // Test that names with special characters can be created successfully
        Name name1 = new Name("R@chel");
        assertEquals("R@chel", name1.toString());

        Name name2 = new Name("James&");
        assertEquals("James&", name2.toString());

        Name name3 = new Name("Bob#123");
        assertEquals("Bob#123", name3.toString());
    }

    @Test
    public void equals() {
        Name name = new Name("Valid Name");

        // same values -> returns true
        assertTrue(name.equals(new Name("Valid Name")));

        // same object -> returns true
        assertTrue(name.equals(name));

        // null -> returns false
        assertFalse(name.equals(null));

        // different types -> returns false
        assertFalse(name.equals(5.0f));

        // different values -> returns false
        assertFalse(name.equals(new Name("Other Valid Name")));
    }
}
