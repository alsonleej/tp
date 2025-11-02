package seedu.address.model.tag;

import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class TagTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Tag(null));
    }

    @Test
    public void constructor_invalidTagName_throwsIllegalArgumentException() {
        String invalidTagName = "";
        assertThrows(IllegalArgumentException.class, () -> new Tag(invalidTagName));
    }

    @Test
    public void constructor_invalidCharacters_throwsIllegalArgumentException() {
        String invalidTagName = "friend-colleague"; // contains hyphen
        assertThrows(IllegalArgumentException.class, () -> new Tag(invalidTagName));
    }

    @Test
    public void constructor_tagTooLong_throwsIllegalArgumentException() {
        // Test various lengths over 50 characters
        assertThrows(IllegalArgumentException.class, () -> new Tag("a".repeat(51))); // 51 characters
        assertThrows(IllegalArgumentException.class, () -> new Tag("a".repeat(52))); // 52 characters
        assertThrows(IllegalArgumentException.class, () -> new Tag("a".repeat(100))); // 100 characters
        assertThrows(IllegalArgumentException.class, () -> new Tag("a".repeat(200))); // 200 characters

        // Test with valid tag pattern (alphanumeric) but exceeding length
        String longTagWithNumbers = "tag" + "1".repeat(49); // 52 characters with numbers
        assertThrows(IllegalArgumentException.class, () -> new Tag(longTagWithNumbers));

        // Test with mixed alphanumeric characters
        String longTagMixed = "VIP" + "123".repeat(16); // 51 characters with mixed chars
        assertThrows(IllegalArgumentException.class, () -> new Tag(longTagMixed));
    }

    @Test
    public void isValidTagName() {
        // null tag name
        assertThrows(NullPointerException.class, () -> Tag.isValidTagName(null));
    }

}
