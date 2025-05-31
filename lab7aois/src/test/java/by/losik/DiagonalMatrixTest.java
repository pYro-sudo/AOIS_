package by.losik;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;
import java.util.Arrays;

class DiagonalMatrixTest {
    private DiagonalMatrix matrix;

    @BeforeEach
    void setUp() {
        matrix = new DiagonalMatrix(16, 16);
    }

    @Test
    void testSetAndGetWord() {
        List<Integer> word = Arrays.asList(1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0);
        matrix.setWord(word, 0, 0);
        assertEquals(word, matrix.getWord(0, 0, 16));
    }

    @Test
    void testSetAndGetIndexColumn() {
        List<Integer> index = Arrays.asList(1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0);
        matrix.setIndexColumn(index, 0, 0);
        assertEquals(index, matrix.getIndexColumn(0, 0, 16));
    }

    @Test
    void testF2() {
        matrix.setWord(Arrays.asList(1, 0, 1, 0), 0, 0);
        matrix.setWord(Arrays.asList(0, 1, 1, 0), 0, 1);
        assertEquals(Arrays.asList(0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0), matrix.f2(0, 1));
    }

    @Test
    void testF7() {
        matrix.setWord(Arrays.asList(1, 0, 1, 0), 0, 0);
        matrix.setWord(Arrays.asList(0, 1, 1, 0), 0, 1);
        assertEquals(Arrays.asList(1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0), matrix.f7(0, 1));
    }

    @Test
    void testF8() {
        matrix.setWord(Arrays.asList(1, 0, 1, 0), 0, 0);
        matrix.setWord(Arrays.asList(0, 1, 1, 0), 0, 1);
        assertEquals(Arrays.asList(0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1), matrix.f8(0, 1));
    }

    @Test
    void testF13_Implication() {
        matrix.setWord(Arrays.asList(1, 0, 1, 0), 0, 0);
        matrix.setWord(Arrays.asList(0, 1, 1, 0), 0, 1);
        assertEquals(Arrays.asList(1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1), matrix.f13(0, 1));
    }

    @Test
    void testAddBinary() {
        List<Integer> a = Arrays.asList(1, 0, 1, 1);
        List<Integer> b = Arrays.asList(0, 1, 1, 0);
        assertEquals(Arrays.asList(1, 0, 0, 0, 1), matrix.addBinary(a, b));
    }

    @Test
    void testSumFields() {
        matrix.setWord(Arrays.asList(1, 0, 1, 1, 0, 1, 0, 1, 1, 0, 1, 0, 1, 1, 0, 1), 0, 0);
        matrix.sumFields(Arrays.asList(1, 0, 1));
        List<Integer> expected = Arrays.asList(1, 0, 1, 1, 0, 1, 0, 1, 1, 0, 1, 1, 0, 1, 1, 1, 1, 0, 1);
        assertEquals(expected, matrix.getWord(0, 0, expected.size()));
    }

    @Test
    void testCompare() {
        assertEquals(1, matrix.compare(Arrays.asList(1, 0, 1), Arrays.asList(0, 1, 0)));
        assertEquals(-1, matrix.compare(Arrays.asList(0, 1, 0), Arrays.asList(1, 0, 1)));
        assertEquals(0, matrix.compare(Arrays.asList(1, 0, 1), Arrays.asList(1, 0, 1)));
    }

    @Test
    void testFindWordsInRange() {
        matrix.setWord(Arrays.asList(0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1), 0, 0);
        matrix.setWord(Arrays.asList(1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0), 1, 1);

        List<Boolean> result = matrix.findWordsInRange(
                Arrays.asList(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
                Arrays.asList(1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1)
        );

        assertFalse(result.get(0));
        assertFalse(result.get(1));
    }
}