package by.losik;

import org.junit.jupiter.api.Test;

import static by.losik.BCD8421ToExcess5Converter.binaryStringToInt;
import static org.junit.jupiter.api.Assertions.*;

class BCD8421ToExcess5ConverterTest {
    @Test
    void testBinaryStringToIntAnyNumber() {
        assertEquals(0, binaryStringToInt("0"));
        assertEquals(1, binaryStringToInt("1"));
        assertEquals(5, binaryStringToInt("101"));
        assertEquals(15, binaryStringToInt("1111"));

        assertEquals(-1, binaryStringToInt("-1"));
        assertEquals(-5, binaryStringToInt("-101"));
        assertEquals(-15, binaryStringToInt("-1111"));

        assertEquals(255, binaryStringToInt("11111111"));
        assertEquals(-255, binaryStringToInt("-11111111"));
    }

    @Test
    void testBinaryStringToIntInvalidInput() {
        assertThrows(IllegalArgumentException.class, () -> binaryStringToInt(null));
        assertThrows(IllegalArgumentException.class, () -> binaryStringToInt(""));
        assertThrows(IllegalArgumentException.class, () -> binaryStringToInt("  "));
        assertThrows(IllegalArgumentException.class, () -> binaryStringToInt("12"));
        assertThrows(IllegalArgumentException.class, () -> binaryStringToInt("1a1"));
        assertThrows(IllegalArgumentException.class, () -> binaryStringToInt("-1a1"));
    }

    @Test
    void testConvertDigitValidInput() {
        assertEquals(5, BCD8421ToExcess5Converter.convertDigit(0));
        assertEquals(6, BCD8421ToExcess5Converter.convertDigit(1));
        assertEquals(7, BCD8421ToExcess5Converter.convertDigit(2));
        assertEquals(8, BCD8421ToExcess5Converter.convertDigit(3));
        assertEquals(9, BCD8421ToExcess5Converter.convertDigit(4));
        assertEquals(10, BCD8421ToExcess5Converter.convertDigit(5));
    }

    @Test
    void testBinaryStringToIntValidInput() {
        assertEquals(0, binaryStringToInt("0000"));
        assertEquals(1, binaryStringToInt("0001"));
        assertEquals(2, binaryStringToInt("0010"));
        assertEquals(5, binaryStringToInt("0101"));
        assertEquals(9, binaryStringToInt("1001"));
        assertEquals(15, binaryStringToInt("1111"));
    }

    @Test
    void testBinaryToDigitValidInput() {
        assertEquals(0, BCD8421ToExcess5Converter.binaryToDigit("0000"));
        assertEquals(5, BCD8421ToExcess5Converter.binaryToDigit("0101"));
        assertEquals(9, BCD8421ToExcess5Converter.binaryToDigit("1001"));
    }

    @Test
    void testDigitToBinaryInvalidInput() {
        assertDoesNotThrow(() -> BCD8421ToExcess5Converter.digitToBinary(89));
    }
}