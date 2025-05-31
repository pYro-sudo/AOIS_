package by.losik;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class MinimizerTest {
    private final Minimizer minimizer = new Minimizer();

    @Test
    @DisplayName("parseFormule should correctly parse SDNF formula")
    void testParseFormuleSDNF() {
        String formula = "(A&B)|(!A&C)";
        List<List<String>> result = minimizer.parseFormule(formula);

        assertEquals(2, result.size());
        assertEquals(List.of("A", "B"), result.get(0));
        assertEquals(List.of("!A", "C"), result.get(1));
    }

    @Test
    @DisplayName("parseFormule should correctly parse SKNF formula")
    void testParseFormuleSKNF() {
        String formula = "(A|B|!C)&(!A|C|D)";
        List<List<String>> result = minimizer.parseFormule(formula);

        assertEquals(2, result.size());
        assertEquals(List.of("A", "B", "!C"), result.get(0));
        assertEquals(List.of("!A", "C", "D"), result.get(1));
    }

    @Test
    @DisplayName("parseFormule should handle empty formula")
    void testParseFormuleEmpty() {
        List<List<String>> result = minimizer.parseFormule("");
        assertTrue(result.isEmpty());
    }

    @ParameterizedTest
    @DisplayName("minimizeFormuleByMingling should correctly minimize formulas")
    @CsvSource({
            "(A&B)|(A&!B), (A)",
            "(A&B&C)|(A&B&!C), (A/\\B)",
            "(A&B)|(!A&B), (B)"
    })
    void testMinimizeFormuleByMingling(String input, String expected) {
        List<List<String>> parsed = minimizer.parseFormule(input);
        List<List<String>> minimized = minimizer.minimizeFormuleByMingling(parsed);
        String result = minimizer.buildDNFString(minimized);

        assertEquals(expected, result);
    }

    @Test
    @DisplayName("minimizeSDNFByCalculating should minimize simple SDNF")
    void testMinimizeSDNFByCalculatingSimple() {
        String sdnf = "(A&B)|(A&!B)";
        String result = minimizer.minimizeSDNFByCalculating(sdnf);
        assertEquals("(A)", result);
    }

    @Test
    @DisplayName("minimizeSKNFByCalculating should minimize simple SKNF")
    void testMinimizeSKNFByCalculatingSimple() {
        String sknf = "(A|B)&(A|!B)";
        String result = minimizer.minimizeSKNFByCalculating(sknf);
        assertEquals("(A)", result);
    }

    @Test
    @DisplayName("getAssignmentForSDNFImplicant should create correct assignments")
    void testGetAssignmentForSDNFImplicant() {
        List<String> implicant = List.of("A", "!B", "C");
        Map<String, Integer> assignment = minimizer.getAssignmentForSDNFImplicant(implicant);

        assertEquals(3, assignment.size());
        assertEquals(1, assignment.get("A"));
        assertEquals(0, assignment.get("B"));
        assertEquals(1, assignment.get("C"));
    }

    @Test
    @DisplayName("getAssignmentForSKNFImplicant should create correct assignments")
    void testGetAssignmentForSKNFImplicant() {
        List<String> implicant = List.of("A", "!B", "C");
        Map<String, Integer> assignment = minimizer.getAssignmentForSKNFImplicant(implicant);

        assertEquals(3, assignment.size());
        assertEquals(0, assignment.get("A"));
        assertEquals(1, assignment.get("B"));
        assertEquals(0, assignment.get("C"));
    }

    @Test
    @DisplayName("checkIfRedundantSDNF should detect redundant implicants")
    void testCheckIfRedundantSDNF() {
        List<List<String>> minimizedSDNF = List.of(
                List.of("A", "B"),
                List.of("A")
        );
        List<String> implicant = List.of("A", "B");
        Map<String, Integer> assignment = Map.of("A", 1, "B", 1);

        boolean isRedundant = minimizer.checkIfRedundantSDNF(implicant, minimizedSDNF, assignment);
        assertTrue(isRedundant);
    }

    @Test
    @DisplayName("checkIfRedundantSKNF should detect redundant implicants")
    void testCheckIfRedundantSKNF() {
        List<List<String>> minimizedSKNF = List.of(
                List.of("A", "B"),
                List.of("A")
        );
        List<String> implicant = List.of("A", "B");
        Map<String, Integer> assignment = Map.of("A", 0, "B", 0);

        boolean isRedundant = minimizer.checkIfRedundantSKNF(implicant, minimizedSKNF, assignment);
        assertTrue(isRedundant);
    }

    @Test
    @DisplayName("findRedundantByTable should find essential implicants")
    void testFindRedundantByTable() {
        List<List<String>> parsed = List.of(
                List.of("A", "B", "C"),
                List.of("A", "B", "!C")
        );
        List<List<String>> minimized = List.of(
                List.of("A", "B"),
                List.of("C")
        );

        List<List<String>> result = minimizer.findRedundantByTable(parsed, minimized);
        assertEquals(1, result.size());
        assertEquals(List.of("A", "B"), result.get(0));
    }

    @Test
    @DisplayName("buildDNFString should format correctly")
    void testBuildDNFString() {
        List<List<String>> terms = List.of(
                List.of("A", "B"),
                List.of("!A", "C")
        );
        String result = minimizer.buildDNFString(terms);
        assertEquals("(A/\\B)\\/(!A/\\C)", result);
    }

    @Test
    @DisplayName("buildCNFString should format correctly")
    void testBuildCNFString() {
        List<List<String>> terms = List.of(
                List.of("A", "B"),
                List.of("!A", "C")
        );
        String result = minimizer.buildCNFString(terms);
        assertEquals("(A\\/B)/\\(!A\\/C)", result);
    }

    @Test
    @DisplayName("returnReversedElement should negate elements")
    void testReturnReversedElement() {
        assertEquals("!A", minimizer.returnReversedElement("A"));
        assertEquals("A", minimizer.returnReversedElement("!A"));
    }

    @Test
    @DisplayName("minimizeSDNFByTableAndCalculating should work with simple input")
    void testMinimizeSDNFByTableAndCalculating() {
        String sdnf = "(A&B)|(A&!B)";
        String result = minimizer.minimizeSDNFByTableAndCalculating(sdnf);
        assertEquals("(A)", result);
    }

    @Test
    @DisplayName("minimizeSKNFByTableAndCalculating should work with simple input")
    void testMinimizeSKNFByTableAndCalculating() {
        String sknf = "(A|B)&(A|!B)";
        String result = minimizer.minimizeSKNFByTableAndCalculating(sknf);
        assertEquals("(A)", result);
    }

    @Test
    @DisplayName("minimizeSDNFByCalculating should handle empty input")
    void testMinimizeSDNFByCalculatingEmpty() {
        String result = minimizer.minimizeSDNFByCalculating("");
        assertEquals("0", result);
    }

    @Test
    @DisplayName("minimizeSKNFByCalculating should handle empty input")
    void testMinimizeSKNFByCalculatingEmpty() {
        String result = minimizer.minimizeSKNFByCalculating("");
        assertEquals("1", result);
    }
}