package by.losik;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class ListUtilsTest {

    @Test
    @DisplayName("distinctValue should remove duplicate inner lists")
    void testDistinctValueWithDuplicates() {
        List<List<String>> input = List.of(
                List.of("A", "B"),
                List.of("C", "D"),
                List.of("A", "B"),
                List.of("E", "F"),
                List.of("C", "D")
        );

        List<List<String>> expected = List.of(
                List.of("A", "B"),
                List.of("C", "D"),
                List.of("E", "F")
        );

        List<List<String>> result = ListUtils.distinctValue(input);
        assertEquals(expected, result);
    }

    @Test
    @DisplayName("distinctValue should return same list when no duplicates")
    void testDistinctValueWithoutDuplicates() {
        List<List<String>> input = List.of(
                List.of("A", "B"),
                List.of("C", "D"),
                List.of("E", "F")
        );

        List<List<String>> result = ListUtils.distinctValue(input);
        assertEquals(input, result);
    }

    @Test
    @DisplayName("distinctValue should handle empty list")
    void testDistinctValueEmptyList() {
        List<List<String>> input = List.of();
        List<List<String>> result = ListUtils.distinctValue(input);
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("distinctValue should preserve order of first occurrences")
    void testDistinctValuePreservesOrder() {
        List<List<String>> input = List.of(
                List.of("A", "B"),
                List.of("C", "D"),
                List.of("A", "B"),
                List.of("E", "F"),
                List.of("C", "D")
        );

        List<List<String>> expectedOrder = List.of(
                List.of("A", "B"),
                List.of("C", "D"),
                List.of("E", "F")
        );

        List<List<String>> result = ListUtils.distinctValue(input);
        assertEquals(expectedOrder, result);
    }

    @Test
    @DisplayName("distinctValue should handle lists with different order")
    void testDistinctValueDifferentOrder() {
        List<List<String>> input = List.of(
                List.of("A", "B"),
                List.of("B", "A"),
                List.of("A", "B")
        );

        List<List<String>> expected = List.of(
                List.of("A", "B"),
                List.of("B", "A")
        );

        List<List<String>> result = ListUtils.distinctValue(input);
        assertEquals(expected, result);
    }
}
