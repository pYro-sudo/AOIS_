package by.losik;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Set;

class LogicMinimizerTest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    void setUpStreams() {
        System.setOut(new PrintStream(outContent));
    }

    @BeforeEach
    void restoreStreams() {
        System.setOut(originalOut);
    }

    @Test
    void testTermEqualsAndHashCode() {
        LogicMinimizer.Term term1 = new LogicMinimizer.Term("AB'C", Set.of(1, 3));
        LogicMinimizer.Term term2 = new LogicMinimizer.Term("AB'C", Set.of(2, 4));
        LogicMinimizer.Term term3 = new LogicMinimizer.Term("A'BC", Set.of(1, 3));

        assertEquals(term1, term2);
        assertNotEquals(term1, term3);
        assertEquals(term1.hashCode(), term2.hashCode());
        assertNotEquals(term1.hashCode(), term3.hashCode());
    }

    @Test
    void testMinimizeFunctionEmpty() {
        String result = LogicMinimizer.minimizeFunction(new int[0], 3);
        assertEquals("0", result);
    }

    @Test
    void testMinimizeFunctionFull() {
        int[] allMinterms = {0, 1, 2, 3, 4, 5, 6, 7};
        String result = LogicMinimizer.minimizeFunction(allMinterms, 3);
        assertEquals("1", result);
    }

    @Test
    void testMinimizeFunctionSimple() {
        int[] minterms = {0, 1, 2, 3};
        String result = LogicMinimizer.minimizeFunction(minterms, 2);
        assertTrue(result == "1");
    }

    @Test
    void testFindPrimeImplicants() {
        int[] minterms = {0, 1, 2, 3, 6, 7};
        Set<LogicMinimizer.Term> primes = LogicMinimizer.findPrimeImplicants(minterms, 3);

        assertTrue(primes.size() >= 2);
        boolean foundA = false;
        boolean foundBC = false;

        for (LogicMinimizer.Term term : primes) {
            if (term.vars.contains("A")) foundA = true;
            if (term.vars.contains("BC")) foundBC = true;
        }

        assertTrue(foundA || foundBC);
    }

    @Test
    void testFindMinimalCover() {
        int[] minterms = {0, 1, 2, 3, 6, 7};
        Set<LogicMinimizer.Term> primes = LogicMinimizer.findPrimeImplicants(minterms, 3);
        Set<LogicMinimizer.Term> cover = LogicMinimizer.findMinimalCover(minterms, primes);

        assertFalse(cover.isEmpty());
        assertTrue(cover.size() <= primes.size());
    }

    @Test
    void testMainOutput() {
        assertDoesNotThrow(() -> LogicMinimizer.main(new String[]{}));
    }
}
