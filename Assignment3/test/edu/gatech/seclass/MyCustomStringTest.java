package edu.gatech.seclass;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MyCustomStringTest {

    private MyCustomStringInterface mycustomstring;

    @Before
    public void setUp() {
        mycustomstring = new MyCustomString();
    }

    @After
    public void tearDown() {
        mycustomstring = null;
    }

    @Test
    public void testCountNumbers1() {
        mycustomstring.setString("I'd b3tt3r put s0me d161ts in this 5tr1n6, right?");
        assertEquals(7, mycustomstring.countNumbers());      // This test checks whether method (countNumbers) can check that contiguous sequence of digits exist in the string.
    }

    @Test
    public void testCountNumbers2() {
        mycustomstring.setString("I'd b3tt3r put s0me d1gits in this 5tr1n6, right?");
        assertEquals(7, mycustomstring.countNumbers());     // This test checks whether method (countNumbers) can check that only single bit digits exist in the string.
    }

    //
    @Test
    public void testCountNumbers3() {
        mycustomstring.setString("3333r put s0me d161ts in this 5tr1n6, right? 52%0");
        assertEquals(8, mycustomstring.countNumbers());     // This test checks whether method (countNumbers) can check that digits exist at the beginning and end of string.
    }

    //
    @Test
    public void testCountNumbers4() {
        mycustomstring.setString("No digials in this string, can you check that?");
        assertEquals(0, mycustomstring.countNumbers());     // This test checks whether method (countNumbers) can check that no digits exist in the string.
    }

    //
    @Test
    public void testCountNumbers5() {
        mycustomstring.setString("1101201199112502");
        assertEquals(1, mycustomstring.countNumbers());     // This test checks whether method (countNumbers) can check that only digits exist in the string.
    }

    //
    @Test
    public void testCountNumbers6() {
        mycustomstring.setString("");
        assertEquals(0, mycustomstring.countNumbers());     // This test checks whether method (countNumbers) can check empty string.
    }

    //
    @Test
    public void testremoveEveryNthCharacter1() {
        mycustomstring.setString("I'd b3tt3r put s0me d161ts in this 5tr1n6, right?");
        assertEquals("I' bttr uts0e 16tsinths trn6 rgh?", mycustomstring.removeEveryNthCharacter(3, false));
        //   This test checks method (removeEveryNthCharacter) functional correctness for maintainSpacing=false, when string's length is not multiple of n.
    }

    //
    @Test
    public void testremoveEveryNthCharacter2() {
        mycustomstring.setString("I'd b3tt3r put s0me d161ts in this 5tr1n6, right?");
        assertEquals("I'  b tt r  ut s0 e  16 ts in th s  tr n6  r gh ?", mycustomstring.removeEveryNthCharacter(3, true));
        //   This test checks method (removeEveryNthCharacter) functional correctness for maintainSpacing=true, when string's length is not multiple of n.
    }

    //
    @Test
    public void testremoveEveryNthCharacter3() {
        mycustomstring.setString("123 567 901 ");
        assertEquals("123567901", mycustomstring.removeEveryNthCharacter(4, false));
    }
    //   This test checks method (removeEveryNthCharacter) functional correctness for maintainSpacing=false, when string's length is multiple of n.

    //
    @Test
    public void testremoveEveryNthCharacter4() {
        mycustomstring.setString("123 567 901 ");
        assertEquals("123 567 901 ", mycustomstring.removeEveryNthCharacter(4, true));
    }
    //   This test checks method (removeEveryNthCharacter) functional correctness for maintainSpacing=true, when string's length is multiple of n.

  //
    @Test
    public void testremoveEveryNthCharacter5() {
        mycustomstring.setString("asdfadvxcvkadf");
        assertEquals("", mycustomstring.removeEveryNthCharacter(1, false));
    }
    //   This test checks whether the method (removeEveryNthCharacter) return empty for maintainSpacing=false, when n=1.

  //
    @Test
    public void testremoveEveryNthCharacter6() {
        mycustomstring.setString("12345678");
        assertEquals("        ", mycustomstring.removeEveryNthCharacter(1, true));
    }
    //   This test checks whether the method (removeEveryNthCharacter) return the same length of whitespace for maintainSpacing=true, when n=1.

  //
    @Test
    public void testremoveEveryNthCharacter7() {
        mycustomstring.setString("12345678");
        assertEquals("1234567", mycustomstring.removeEveryNthCharacter(8, false));
    }
    //   This test checks whether the method (removeEveryNthCharacter) replace only the last character for maintainSpacing=false, when n=string.length().

  //
    @Test
    public void testremoveEveryNthCharacter8() {
        mycustomstring.setString("12345678");
        assertEquals("1234567 ", mycustomstring.removeEveryNthCharacter(8, true));
    }
    //   This test checks whether the method (removeEveryNthCharacter) replace only the last character for maintainSpacing=true, when n=string.length().

  //
    @Test(expected = IllegalArgumentException.class)
    public void testremoveEveryNthCharacter9() {
		mycustomstring.setString("test string");
		mycustomstring.removeEveryNthCharacter(0, false);
    }
    //   This test checks whether the method (removeEveryNthCharacter) suitable throws an IllegalArgumentException for maintainSpacing=false, when the position less than or equal zero.
  //
    @Test(expected = IllegalArgumentException.class)
    public void testremoveEveryNthCharacter10() {
		mycustomstring.setString("test string");
		mycustomstring.removeEveryNthCharacter(0, true);
    }
    //   This test checks whether the method (removeEveryNthCharacter) suitable throws an IllegalArgumentException for maintainSpacing=true, when the position less than or equal zero.
  //
    @Test(expected = MyIndexOutOfBoundsException.class)
    public void testremoveEveryNthCharacter11() {
		mycustomstring.setString("123456");
		mycustomstring.removeEveryNthCharacter(7, false);
    }
    //   This test checks whether the method (removeEveryNthCharacter) suitable throws an MyIndexOutOfBoundsException for maintainSpacing=false, when the position is greater than the string length.
  //
    @Test(expected = MyIndexOutOfBoundsException.class)
    public void testremoveEveryNthCharacter12() {
		mycustomstring.setString("123456");
		mycustomstring.removeEveryNthCharacter(7, true);
    }
    //   This test checks whether the method (removeEveryNthCharacter) suitable throws an MyIndexOutOfBoundsException for maintainSpacing=true, when the position is greater than the string length.
  //
    @Test
    public void testConvertDigitsToNamesInSubstring1() {
        mycustomstring.setString("I'd b3tt3r put s0me d161ts in this 5tr1n6, right?");
        mycustomstring.convertDigitsToNamesInSubstring(17, 23);
        assertEquals("I'd b3tt3r put szerome done-six1ts in this 5tr1n6, right?", mycustomstring.getString());
    }
  //   This test checks method (convertDigitsToNamesInSubstring) functional correctness for common use.
    @Test
    public void testConvertDigitsToNamesInSubstring2() {
        mycustomstring.setString("asdfqenfklfndvj");
        mycustomstring.convertDigitsToNamesInSubstring(3, 8);
        assertEquals("asdfqenfklfndvj", mycustomstring.getString());
    }
    //  This test checks method (convertDigitsToNamesInSubstring) functional correctness, when there is no digit in the string.
    @Test
    public void testConvertDigitsToNamesInSubstring3() {
        mycustomstring.setString("1234567890");
        mycustomstring.convertDigitsToNamesInSubstring(3, 8);
        assertEquals("12three-four-five-six-seven-eight90", mycustomstring.getString());
    }
    //  This test checks method (convertDigitsToNamesInSubstring) functional correctness, when the string only contain all digits.
    @Test
    public void testConvertDigitsToNamesInSubstring4() {
        mycustomstring.setString("12345678ER");
        mycustomstring.convertDigitsToNamesInSubstring(1, 10);
        assertEquals("one-two-three-four-five-six-seven-eightER", mycustomstring.getString());
    }
  //    This test checks method (convertDigitsToNamesInSubstring) functional correctness, when the startposition == 1 and endpostion == string.length.
    @Test
    public void testConvertDigitsToNamesInSubstring5() {
        mycustomstring.setString("12#4$6%PER");
        mycustomstring.convertDigitsToNamesInSubstring(2, 7);
        assertEquals("1two#four$six%PER", mycustomstring.getString());
    }
  //    This test checks method (convertDigitsToNamesInSubstring) functional correctness, when there is no contiguous digit.
    @Test
    public void testConvertDigitsToNamesInSubstring6() {
        mycustomstring.setString("123werasd");
        mycustomstring.convertDigitsToNamesInSubstring(2, 3);
        assertEquals("1two-threewerasd", mycustomstring.getString());
    }
  //   This test checks method (convertDigitsToNamesInSubstring) functional correctness, when the minimum distance between startposition and endposition.
    @Test(expected = IllegalArgumentException.class)
    public void testConvertDigitsToNamesInSubstring7() {
		mycustomstring.setString("test string");
		mycustomstring.convertDigitsToNamesInSubstring(0, 2);
    }
   //   This test checks whether the method (convertDigitsToNamesInSubstring) suitable throws an IllegalArgumentException, if "startPosition" <= "endPosition", and "startPosition" is less than 1.
    @Test(expected = MyIndexOutOfBoundsException.class)
    public void testConvertDigitsToNamesInSubstring8() {
		mycustomstring.setString("test string");
		mycustomstring.convertDigitsToNamesInSubstring(2, 12);
    }
  //   This test checks whether the method (convertDigitsToNamesInSubstring) suitable throws an MyIndexOutOfBoundsException, if endPosition" is out of bounds.
    @Test(expected = MyIndexOutOfBoundsException.class)
    public void testConvertDigitsToNamesInSubstring9() {
		mycustomstring.setString("test string");
		mycustomstring.convertDigitsToNamesInSubstring(4, 3);
    }
  //   This test checks whether the method (convertDigitsToNamesInSubstring) suitable throws an MyIndexOutOfBoundsException, if startPosition" > "endPosition".
    @Test(expected = NullPointerException.class)
    public void testConvertDigitsToNamesInSubstring10() {
		mycustomstring.setString(null);
		mycustomstring.convertDigitsToNamesInSubstring(1, 3);
    }
  //   This test checks whether the method (convertDigitsToNamesInSubstring) suitable throws an MyIndexOutOfBoundsException, if the current string is null.
}
