package by.losik;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

class LogicMinimizerAppTest {

    @Test
    @DisplayName("Приложение должно обрабатывать пустой ввод")
    void testAppWithEmptyInput() {
        String input = "\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));

        Assertions.assertThrows(Exception.class, () -> LogicMinimizerApp.main(new String[]{}));

        String output = out.toString();
        assertTrue(output.contains("Введите логическое выражение"));
    }

    @Test
    @DisplayName("Приложение должно обрабатывать сложное выражение")
    void testAppWithComplexExpression() {
        String input = "(A&B)|(!A&C)\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));

        LogicMinimizerApp.main(new String[]{});

        String output = out.toString();
        assertTrue(output.contains("СДНФ"));
        assertTrue(output.contains("СКНФ"));
        assertTrue(output.contains("A"));
        assertTrue(output.contains("B"));
        assertTrue(output.contains("C"));
    }

    @Test
    @DisplayName("Приложение должно выводить все этапы минимизации")
    void testAppOutputsAllMinimizationStages() {
        String input = "A|B\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));

        LogicMinimizerApp.main(new String[]{});

        String output = out.toString();
        assertTrue(output.contains("расчётным методом"));
        assertTrue(output.contains("расчётно-табличным методом"));
        assertTrue(output.contains("табличным методом"));
    }
}