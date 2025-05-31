package by.losik;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import static org.junit.jupiter.api.Assertions.*;

class AutomataSynthesizerCriticalMethodsTest {

    private AutomataSynthesizer synthesizer;
    private List<AutomataSynthesizer.State> states;
    private List<AutomataSynthesizer.Transition> transitions;

    @BeforeEach
    void setUp() {
        states = Arrays.asList(
                new AutomataSynthesizer.State("S0", 0, 0, 0),
                new AutomataSynthesizer.State("S1", 0, 0, 1)
        );

        transitions = Arrays.asList(
                new AutomataSynthesizer.Transition(states.get(0), states.get(1), new int[]{1}),
                new AutomataSynthesizer.Transition(states.get(1), states.get(0), new int[]{0})
        );

        synthesizer = new AutomataSynthesizer(
                states, transitions,
                AutomataSynthesizer.TriggerType.T,
                AutomataSynthesizer.LogicBasis.AND_OR_NOT,
                1
        );
    }

    @Test
    void testPrintExcitationFunctionsDoesNotThrow() {
        Map<Integer, List<Integer>> excitationFunctions = new HashMap<>();
        excitationFunctions.put(0, Arrays.asList(1, 3, 5));
        excitationFunctions.put(1, Arrays.asList(2, 4, 6));

        assertDoesNotThrow(() -> synthesizer.printExcitationFunctions(excitationFunctions));
    }

    @Test
    void testImplementInLogicBasisDoesNotThrow() {
        assertDoesNotThrow(() -> synthesizer.implementInLogicBasis());
    }

    @Test
    void testMainMethodDoesNotThrow() {
        assertDoesNotThrow(() -> AutomataSynthesizer.main(new String[]{}));
    }

    @Test
    void testPrintExcitationFunctionsWithEmptyInput() {
        assertDoesNotThrow(() -> synthesizer.printExcitationFunctions(new HashMap<>()));
    }

    @Test
    void testImplementInLogicBasisWithDifferentBases() {
        for (AutomataSynthesizer.LogicBasis basis : AutomataSynthesizer.LogicBasis.values()) {
            AutomataSynthesizer synth = new AutomataSynthesizer(
                    states, transitions,
                    AutomataSynthesizer.TriggerType.T,
                    basis,
                    1
            );
            assertDoesNotThrow(synth::implementInLogicBasis);
        }
    }
}