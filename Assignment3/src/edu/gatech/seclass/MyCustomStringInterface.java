package edu.gatech.seclass;

/**
 * This is an interface for a simple class that represents a string, defined
 * as a sequence of characters.
 *
 * @author Rufus
 */
public interface MyCustomStringInterface {

    /**
     * Returns the current string. If the string is null, it should return null.
     *
     * @return Current string
     */
    String getString();

    /**
     * Sets the value of the current string.
     *
     * @param string The value to be set
     */
    void setString(String string);

    /**
     * Returns the number of numbers in the current string, where a number is defined as a
     * contiguous sequence of digits.
     *
     * If the current string is empty or unset, the method should return 0.
     *
     * Examples:
     * - countNumbers would return 2 for string "My numbers are 11 and 96".
     *
     * @return Number of numbers in the current string
     */
    int countNumbers();

    /**
     * Returns a string that consists of all characters in the original string except for the characters
     * in positions n, 2n, 3n, and so on, either deleting those or replacing them
     * with a white space. The characters in the resulting string should be in the same order
     * and with the same case as in the current string.
     *
     *
     *
     * Examples:
     * - For n=2 and maintainSpacing=false, the method would return the string without the 2nd, 4th,
     *   6th, and so on characters in the string.
     * - For n=3 and maintainSpacing=true, the method would return the string with a space replacing
     *  the 3nd, 6th, 9th, and so on characters in the string.
     *
     * Values n and maintainSpacing are passed as parameters. The starting character is considered to be in Position 1.
     *
     * @param n Determines the positions of the characters to be returned
     * @param maintainSpacing Determines whether replace the missing characters with a space, in order
     *                        to maintain the length of the original string.
     * @return String made of characters at positions other than n, 2n, and so on in the current string
     * @throws MyIndexOutOfBoundsException If n is greater than the string length.
     * @throws IllegalArgumentException If "n" less than or equal to zero.
     *
     *
     */
    String removeEveryNthCharacter(int n, boolean maintainSpacing);

    /**
     * Replace the individual digits in the current string, between startPosition and endPosition
     * (included), with the corresponding English names of those digits, with no letters
     * capitalized. The first character in the string is considered to be in Position 1.
     * Contiguous digits will be converted individually and joined by hyphens (-).
     *
     * Examples:
     * - String 460 would be converted to four-six-zero
     * - String 416 would be converted to four-one-six
     *
     * @param startPosition Position of the first character to consider
     * @param endPosition   Position of the last character to consider
     * @throws NullPointerException        If the current string is null.
     * @throws MyIndexOutOfBoundsException If "startPosition" > "endPosition", or "endPosition" is out of
     *                                     bounds (greater than the length of the string)
     * @throws IllegalArgumentException    If "startPosition" <= "endPosition", and "startPosition" is less than 1
     */
    void convertDigitsToNamesInSubstring(int startPosition, int endPosition);
}
