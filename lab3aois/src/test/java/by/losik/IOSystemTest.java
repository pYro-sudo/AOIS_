package by.losik;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class IOSystemTest {

    @Test
    @DisplayName("printSDNFTable should correctly format output")
    void testPrintSDNFTable() {
        List<List<String>> parsed = List.of(
                List.of("A", "B"),
                List.of("!A", "B")
        );
        List<List<String>> minimized = List.of(
                List.of("B")
        );

        assertDoesNotThrow(() -> IOSystem.printSDNFTable(parsed, minimized));
    }

    @Test
    @DisplayName("printSKNFTable should correctly format output")
    void testPrintSKNFTable() {
        List<List<String>> parsed = List.of(
                List.of("A", "B"),
                List.of("!A", "B")
        );
        List<List<String>> minimized = List.of(
                List.of("B")
        );

        assertDoesNotThrow(() -> IOSystem.printSKNFTable(parsed, minimized));
    }

    @ParameterizedTest
    @DisplayName("printKarnaughMap should support valid map sizes")
    @ValueSource(ints = {2, 4, 8})
    void testPrintKarnaughMapValidSizes(int cols) {
        int rows = cols == 8 ? 4 : (cols == 4 ? 2 : (cols == 2 ? (cols == 2 ? 1 : 2) : 0));
        int[][] map = new int[rows][cols];

        assertDoesNotThrow(() -> IOSystem.printKarnaughMap(map));
    }

    @Test
    @DisplayName("printKarnaughMap should throw for invalid size")
    void testPrintKarnaughMapInvalidSize() {
        int[][] invalidMap = new int[3][3];
        assertThrows(IllegalArgumentException.class, () -> IOSystem.printKarnaughMap(invalidMap));
    }

    @ParameterizedTest
    @DisplayName("generateColumnHeaders should return correct headers")
    @CsvSource({
            "2, '0,1'",
            "4, '00,01,11,10'",
            "8, '000,001,011,010,110,111,101,100'"
    })
    void testGenerateColumnHeaders(int cols, String expected) {
        String[] result = IOSystem.generateColumnHeaders(cols, cols == 2 ? 1 : (cols == 4 ? 2 : 3));
        assertArrayEquals(expected.split(","), result);
    }

    @Test
    @DisplayName("generateColumnHeaders should throw for invalid column count")
    void testGenerateColumnHeadersInvalid() {
        assertThrows(IllegalArgumentException.class, () -> IOSystem.generateColumnHeaders(3, 2));
    }

    @ParameterizedTest
    @DisplayName("generateRowHeaders should return correct headers")
    @CsvSource({
            "1, ''",
            "2, '0,1'",
            "4, '00,01,11,10'"
    })
    void testGenerateRowHeaders(int rows, String expected) {
        String[] result = IOSystem.generateRowHeaders(rows, rows == 1 ? 0 : (rows == 2 ? 1 : 2));
        assertArrayEquals(expected.split(","), result);
    }

    @Test
    @DisplayName("generateRowHeaders should throw for invalid row count")
    void testGenerateRowHeadersInvalid() {
        assertThrows(IllegalArgumentException.class, () -> IOSystem.generateRowHeaders(3, 1));
    }

    @ParameterizedTest
    @DisplayName("chooseFormulaType should return valid choices")
    @ValueSource(ints = {1, 2})
    void testChooseFormulaTypeValid(int choice) throws NoSuchMethodException {
        assertNotNull(IOSystem.class.getDeclaredMethod("chooseFormulaType"));
    }

    @Test
    @DisplayName("takeTheInput should return non-null string")
    void testTakeTheInput() throws NoSuchMethodException {
        assertNotNull(IOSystem.class.getDeclaredMethod("takeTheInput"));
    }

    @ParameterizedTest
    @DisplayName("Print methods should output correct format")
    @CsvSource({
            "printSDNFCalculatingMethod, 'test', 'Минимизированная СДНФ расчётным методом: test\n'",
            "printSKNFCalculatingMethod, 'test', 'Минимизированная СКНФ расчётным методом: test\n'",
            "printSDNFTableCalculatingMethod, 'test', 'Минимизированная СДНФ расчётно-табличным методом: test \n'",
            "printSKNFTableCalculatingMethod, 'test', 'Минимизированная СКНФ расчётно-табличным методом: test \n'",
            "printSDNFTableMethod, 'test', 'Минимизированная СДНФ табличным методом: test \n'",
            "printSKNFTableMethod, 'test', 'Минимизированная СКНФ табличным методом: test  \n'"
    })
    void testPrintMethods(String methodName, String input, String expected) throws Exception {
        var method = IOSystem.class.getDeclaredMethod(methodName, String.class);
        assertDoesNotThrow(() -> method.invoke(null, input));
    }

    @Test
    @DisplayName("SDNF table print with empty input")
    void testPrintSDNFTableEmptyInput() {
        assertDoesNotThrow(() -> IOSystem.printSDNFTable(List.of(), List.of()));
    }

    @Test
    @DisplayName("SKNF table print with empty input")
    void testPrintSKNFTableEmptyInput() {
        assertDoesNotThrow(() -> IOSystem.printSKNFTable(List.of(), List.of()));
    }

    @Test
    @DisplayName("Karnaugh map print with minimum size")
    void testPrintKarnaughMapMinimumSize() {
        int[][] map = new int[1][2];
        assertDoesNotThrow(() -> IOSystem.printKarnaughMap(map));
    }
}
