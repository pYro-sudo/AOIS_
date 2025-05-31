package by.losik;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class KarnaughMinimizer1Test {
    private final KarnaughMinimizer1 minimizer = new KarnaughMinimizer1();

    @Test
    void testMinimizeSDNFByKarnaugh_2Variables() {
        String sdnf = "(A/\\B) \\/ (A/\\!B) \\/ (!A/\\B)";
        String expected = "(A)\\/(B)";
        String result = minimizer.minimizeSDNFByKarnaugh(sdnf);
        assertEquals(expected, result);
    }

    @Test
    void testMinimizeSKNFByKarnaugh_2Variables() {
        String sknf = "(A\\/B) /\\ (A\\/!B) /\\ (!A\\/B)";
        String expected = "(A)/\\(B)";
        String result = minimizer.minimizeSKNFByKarnaugh(sknf);
        assertEquals(expected, result);
    }

    @Test
    void testMinimizeSDNFByKarnaugh_AllTrue() {
        String sdnf = "(A/\\B) \\/ (A/\\!B) \\/ (!A/\\B) \\/ (!A/\\!B)";
        String expected = "1";
        String result = minimizer.minimizeSDNFByKarnaugh(sdnf);
        assertEquals(expected, result);
    }

    @Test
    void testMinimizeSKNFByKarnaugh_AllFalse() {
        String sknf = "(A\\/B) /\\ (A\\/!B) /\\ (!A\\/B) /\\ (!A\\/!B)";
        String expected = "0";
        String result = minimizer.minimizeSKNFByKarnaugh(sknf);
        assertEquals(expected, result);
    }

    @Test
    void testMinimizeSDNFByKarnaugh_4Variables() {
        String sdnf = "(A/\\B/\\C/\\D) \\/ (A/\\B/\\C/\\!D) \\/ (A/\\B/\\!C/\\D) \\/ (A/\\!B/\\C/\\D) \\/ (!A/\\B/\\C/\\D)";
        String expected = "(A/\\B/\\D)\\/(A/\\B/\\C)\\/(B/\\C/\\D)\\/(A/\\C/\\D)";
        String result = minimizer.minimizeSDNFByKarnaugh(sdnf);
        assertEquals(expected, result);
    }

    @Test
    void testMinimizeSKNFByKarnaugh_5Variables() {
        String sknf = "(A\\/B\\/C\\/D\\/E) /\\ (A\\/B\\/C\\/D\\/!E) /\\ (A\\/B\\/C\\/!D\\/E) /\\ (A\\/B\\/!C\\/D\\/E) /\\ (A\\/!B\\/C\\/D\\/E) /\\ (!A\\/B\\/C\\/D\\/E)";
        String expected = "(A\\/B\\/C\\/D)/\\(A\\/B\\/D\\/E)/\\(A\\/C\\/D\\/E)/\\(B\\/C\\/D\\/E)/\\(A\\/B\\/C\\/E)";
        String result = minimizer.minimizeSKNFByKarnaugh(sknf);
        assertEquals(expected, result);
    }

    @Test
    void testMinimizeSDNFByKarnaugh_Empty() {
        String sdnf = "";
        assertThrows(IllegalArgumentException.class, () -> minimizer.minimizeSDNFByKarnaugh(sdnf));
    }

    @Test
    void testMinimizeSKNFByKarnaugh_Empty() {
        String sknf = "";
        assertThrows(IllegalArgumentException.class, () -> minimizer.minimizeSKNFByKarnaugh(sknf));
    }

    @Test
    void testMinimizeSDNFByKarnaugh_InvalidVariableCount() {
        String sdnf = "(A/\\B/\\C/\\D/\\E/\\F)";
        assertThrows(IllegalArgumentException.class, () -> minimizer.minimizeSDNFByKarnaugh(sdnf));
    }

    @Test
    void testMinimizeSKNFByKarnaugh_InvalidVariableCount() {
        String sknf = "(A\\/B\\/C\\/D\\/E\\/F)";
        assertThrows(IllegalArgumentException.class, () -> minimizer.minimizeSKNFByKarnaugh(sknf));
    }
}
