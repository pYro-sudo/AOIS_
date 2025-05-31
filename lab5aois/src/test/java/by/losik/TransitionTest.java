package by.losik;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TransitionTest {

    @Test
    void testTransitionCreation() {
        AutomataSynthesizer.State s0 = new AutomataSynthesizer.State("S0", 0, 0, 0);
        AutomataSynthesizer.State s1 = new AutomataSynthesizer.State("S1", 0, 0, 1);
        AutomataSynthesizer.Transition transition = new AutomataSynthesizer.Transition(s0, s1, new int[]{1});

        assertEquals(s0, transition.getCurrentState());
        assertEquals(s1, transition.getNextState());
        assertArrayEquals(new int[]{1}, transition.getInputConditions());
    }

    @Test
    void testTransitionToString() {
        AutomataSynthesizer.State s0 = new AutomataSynthesizer.State("S0", 0, 0, 0);
        AutomataSynthesizer.State s1 = new AutomataSynthesizer.State("S1", 0, 0, 1);
        AutomataSynthesizer.Transition transition = new AutomataSynthesizer.Transition(s0, s1, new int[]{1});

        assertEquals("S0[0, 0, 0] -> S1[0, 0, 1] при [1]", transition.toString());
    }
}