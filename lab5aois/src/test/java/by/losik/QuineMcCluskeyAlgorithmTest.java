package by.losik;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class QuineMcCluskeyAlgorithmTest {
    private static final List<AutomataSynthesizer.State> DUMMY_STATES = List.of(
            new AutomataSynthesizer.State("S0", 0)
    );

    private static final List<AutomataSynthesizer.Transition> DUMMY_TRANSITIONS = List.of(
            new AutomataSynthesizer.Transition(DUMMY_STATES.get(0), DUMMY_STATES.get(0), new int[]{0})
    );

    private AutomataSynthesizer createTestSynthesizer() {
        return new AutomataSynthesizer(
                DUMMY_STATES,
                DUMMY_TRANSITIONS,
                AutomataSynthesizer.TriggerType.T,
                AutomataSynthesizer.LogicBasis.AND_OR_NOT,
                1
        );
    }

    @Test
    void testFindPrimeImplicants() {
        AutomataSynthesizer synthesizer = createTestSynthesizer();
        List<Integer> minterms = Arrays.asList(0, 1, 2, 5, 6, 7);

        List<AutomataSynthesizer.Implicant> primes = synthesizer.findPrimeImplicants(minterms, 3);

        assertNotNull(primes);
        assertEquals(6, primes.size());
    }

    @Test
    void testFindEssentialImplicants() {
        AutomataSynthesizer synthesizer = createTestSynthesizer();
        List<Integer> minterms = Arrays.asList(0, 1, 2, 5, 6, 7);

        List<AutomataSynthesizer.Implicant> primes = synthesizer.findPrimeImplicants(minterms, 3);
        List<AutomataSynthesizer.Implicant> essentials = synthesizer.findEssentialImplicants(primes, minterms);

        assertNotNull(essentials);
        assertEquals(3, essentials.size());
    }

    @Test
    void testQuineMcCluskey() {
        AutomataSynthesizer synthesizer = createTestSynthesizer();
        List<Integer> minterms = Arrays.asList(0, 1, 2, 5, 6, 7);

        String result = synthesizer.quineMcCluskey(minterms, 3);

        assertNotNull(result);
        assertFalse(result.isEmpty());

        assertTrue(result.contains("¬x0") || result.contains("x1") || result.contains("x2"));
    }

    @Test
    void testImplicantToString() {
        AutomataSynthesizer synthesizer = createTestSynthesizer();

        AutomataSynthesizer.Implicant imp1 = new AutomataSynthesizer.Implicant(6, 0, 1 << 6);
        assertEquals("x2x1¬x0", synthesizer.implicantToString(imp1, 3));

        AutomataSynthesizer.Implicant imp2 = new AutomataSynthesizer.Implicant(2, 1, 1 << 2);
        assertEquals("¬x2x1", synthesizer.implicantToString(imp2, 3));
    }

    @Test
    void testEmptyMinterms() {
        AutomataSynthesizer synthesizer = createTestSynthesizer();
        String result = synthesizer.quineMcCluskey(Collections.emptyList(), 2);
        assertEquals("0", result);
    }

    @Test
    void testAllMinterms() {
        AutomataSynthesizer synthesizer = createTestSynthesizer();
        String result = synthesizer.quineMcCluskey(Arrays.asList(0, 1, 2, 3), 2);
        assertEquals("1", result);
    }
}