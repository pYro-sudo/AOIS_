package by.losik;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class StateTest {

    @Test
    void testStateCreation() {
        AutomataSynthesizer.State state = new AutomataSynthesizer.State("S0", 0, 0, 0);
        assertEquals("S0", state.name);
        assertArrayEquals(new int[]{0, 0, 0}, state.getCode());
        assertEquals(3, state.getCodeLength());
    }

    @Test
    void testStateToString() {
        AutomataSynthesizer.State state = new AutomataSynthesizer.State("S1", 0, 0, 1);
        assertEquals("S1[0, 0, 1]", state.toString());
    }

    @Test
    void testStateEquality() {
        AutomataSynthesizer.State state1 = new AutomataSynthesizer.State("S0", 0, 0, 0);
        AutomataSynthesizer.State state2 = new AutomataSynthesizer.State("S0", 0, 0, 0);
        AutomataSynthesizer.State state3 = new AutomataSynthesizer.State("S1", 0, 0, 1);

        assertEquals(state1.name, state2.name);
        assertArrayEquals(state1.getCode(), state2.getCode());
        assertNotEquals(state1.getCode(), state3.getCode());
        assertNotEquals(state1.name, state3.name);
    }
}