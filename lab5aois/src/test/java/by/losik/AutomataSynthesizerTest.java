package by.losik;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class AutomataSynthesizerTest {

    private AutomataSynthesizer synthesizer;
    private List<AutomataSynthesizer.State> states;
    private List<AutomataSynthesizer.Transition> transitions;

    @BeforeEach
    void setUp() {
        states = Arrays.asList(
                new AutomataSynthesizer.State("S0", 0, 0, 0),
                new AutomataSynthesizer.State("S1", 0, 0, 1),
                new AutomataSynthesizer.State("S2", 0, 1, 0)
        );

        transitions = Arrays.asList(
                new AutomataSynthesizer.Transition(states.get(0), states.get(1), new int[]{1}),
                new AutomataSynthesizer.Transition(states.get(1), states.get(2), new int[]{1}),
                new AutomataSynthesizer.Transition(states.get(2), states.get(0), new int[]{1})
        );

        synthesizer = new AutomataSynthesizer(
                states, transitions,
                AutomataSynthesizer.TriggerType.T,
                AutomataSynthesizer.LogicBasis.AND_OR_NOT,
                1
        );
    }

    @Test
    void testBuildTransitionTable() {
        var table = synthesizer.buildTransitionTable();
        assertEquals(3, table.size());
        assertEquals(states.get(1), table.get(states.get(0)).get("[1]"));
    }

    @Test
    void testCalculateExcitationFunctions() {
        var table = synthesizer.buildTransitionTable();
        var functions = synthesizer.calculateExcitationFunctions(table);

        assertNotNull(functions);
        assertTrue(functions.containsKey(0) || functions.containsKey(1) || functions.containsKey(2));
    }

    @Test
    void testQuineMcCluskey() {
        String result = synthesizer.quineMcCluskey(Arrays.asList(0, 1, 3, 7), 3);
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    void testBinaryToDecimal() {
        assertEquals(5, synthesizer.binaryToDecimal(new int[]{1, 0, 1}));
        assertEquals(0, synthesizer.binaryToDecimal(new int[]{0, 0, 0}));
        assertEquals(7, synthesizer.binaryToDecimal(new int[]{1, 1, 1}));
    }
}