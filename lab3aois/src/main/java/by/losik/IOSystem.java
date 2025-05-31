package by.losik;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class IOSystem {
    public static void printSDNFTable(List<List<String>> parsedFormulas,
                                       List<List<String>> minimizedFormulas) {
        // 1. Find the “longestList” by sum of string‐lengths, then by count
        List<String> longestList = minimizedFormulas.stream()
                .max(Comparator.comparingInt((List<String> inner) ->
                                inner.stream().mapToInt(String::length).sum())
                        .thenComparingInt(List::size))
                .orElse(Collections.emptyList());

        // 2. Compute how many characters the joined longestList uses
        int longestListLengthWithSymbols = String.join("/\\", longestList).length();

        // 3. Print the header row: spaces + ‘|’ + each parsedFormula joined by “/\\” and ‘|’
        System.out.print(" ".repeat(longestListLengthWithSymbols) + "\t|");
        for (List<String> parsed : parsedFormulas) {
            System.out.print(String.join("/\\", parsed) + "\t|");
        }
        System.out.println();

        // 4. For each minimized formula, print its row
        for (List<String> minimized : minimizedFormulas) {
            String joinedMin = String.join("/\\", minimized);
            int paddingForThis = longestListLengthWithSymbols - joinedMin.length();
            System.out.print(joinedMin + " ".repeat(paddingForThis) + "\t|");

            for (List<String> parsed : parsedFormulas) {
                // 5. Determine if all items in 'minimized' are found in 'parsed'
                boolean allContained = minimized.stream().allMatch(parsed::contains);
                int cellValue = allContained ? 1 : 0;

                // We want to center‐align the single digit (0 or 1) under the header
                int headerLen = String.join("/\\", parsed).length();
                int leftSpaces  = headerLen / 2;
                int rightSpaces = (headerLen % 2 != 0)
                        ? headerLen / 2
                        : headerLen / 2 - 1;
                System.out.print(" ".repeat(leftSpaces) + cellValue + " ".repeat(rightSpaces) + "\t|");
            }
            System.out.println();
        }
    }


    public static void printSKNFTable(List<List<String>> parsedFormulas,
                                      List<List<String>> minimizedFormulas) {
        // Same logic as SDNF, but join with “\\/” instead of “/\\”
        List<String> longestList = minimizedFormulas.stream()
                .max(Comparator.comparingInt((List<String> inner) ->
                                inner.stream().mapToInt(String::length).sum())
                        .thenComparingInt(List::size))
                .orElse(Collections.emptyList());

        int longestListLengthWithSymbols = String.join("\\/", longestList).length();

        System.out.print(" ".repeat(longestListLengthWithSymbols) + "\t|");
        for (List<String> parsed : parsedFormulas) {
            System.out.print(String.join("\\/", parsed) + "\t|");
        }
        System.out.println();

        for (List<String> minimized : minimizedFormulas) {
            String joinedMin = String.join("\\/", minimized);
            int paddingForThis = longestListLengthWithSymbols - joinedMin.length();
            System.out.print(joinedMin + " ".repeat(paddingForThis) + "\t|");

            for (List<String> parsed : parsedFormulas) {
                boolean allContained = minimized.stream().allMatch(parsed::contains);
                int cellValue = allContained ? 1 : 0;

                int headerLen = String.join("\\/", parsed).length();
                int leftSpaces  = headerLen / 2;
                int rightSpaces = (headerLen % 2 != 0)
                        ? headerLen / 2
                        : headerLen / 2 - 1;
                System.out.print(" ".repeat(leftSpaces) + cellValue + " ".repeat(rightSpaces) + "\t|");
            }
            System.out.println();
        }
    }


    public static void printKarnaughMap(int[][] kMap) {
        int rows = kMap.length;
        int cols = rows > 0 ? kMap[0].length : 0;

        if (!((rows == 1 && cols == 2) || (rows == 2 && cols == 2) ||
                (rows == 2 && cols == 4) || (rows == 4 && cols == 4) ||
                (rows == 4 && cols == 8))) {
            throw new IllegalArgumentException("Неподдерживаемый размер карты Карно");
        }

        System.out.println("Карта Карно:");

        int colVars = cols == 2 ? 1 : (cols == 4 ? 2 : 3);
        String[] colHeaders = generateColumnHeaders(cols, colVars);
        System.out.print("       ");
        for (String header : colHeaders) {
            System.out.printf("%4s", header);
        }
        System.out.println();

        int rowVars = rows == 1 ? 0 : (rows == 2 ? 1 : 2);
        String[] rowHeaders = generateRowHeaders(rows, rowVars);
        for (int i = 0; i < rows; i++) {
            System.out.printf("%4s |", rowHeaders[i]);
            for (int j = 0; j < cols; j++) {
                System.out.printf("%4d", kMap[i][j]);
            }
            System.out.println();
        }
    }

    static String[] generateColumnHeaders(int cols, int varCount) {
        if (cols == 2)
            return new String[] { "0", "1" };
        else if (cols == 4)
            return new String[] { "00", "01", "11", "10" };
        else if (cols == 8)
            return new String[] { "000", "001", "011", "010", "110", "111", "101", "100" };
        else
            throw new IllegalArgumentException("Неподдерживаемое количество столбцов");
    }

    static String[] generateRowHeaders(int rows, int varCount) {
        if (rows == 1)
            return new String[] { "" };
        else if (rows == 2)
            return new String[] { "0", "1" };
        else if (rows == 4)
            return new String[] { "00", "01", "11", "10" };
        else
            throw new IllegalArgumentException("Неподдерживаемое количество строк");
    }

    public static int chooseFormulaType() {
        System.out.println("Choose formula type:\n 1.SDNF\n 2.SKNF");

        Scanner scanner = new Scanner(System.in);
        while (true) {
            try {
                int formulaType = scanner.nextInt();
                if (formulaType == 1 || formulaType == 2) {
                    return formulaType;
                } else {
                    System.out.println("Type 1 or 2!");
                }
            } catch (Exception e) {
                System.out.println("Type 1 or 2!");
                scanner.next(); // consume invalid input
            }
        }
    }

    public static String takeTheInput() {
        System.out.println("Введите формулу: ");
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }

    public static void printSDNFCalculatingMethod(String formula) {
        System.out.println("Минимизированная СДНФ расчётным методом: " + formula + "\n");
    }

    public static void printSKNFCalculatingMethod(String formula) {
        System.out.println("Минимизированная СКНФ расчётным методом: " + formula + "\n");
    }

    public static void printSDNFTableCalculatingMethod(String formula) {
        System.out.println("Минимизированная СДНФ расчётно-табличным методом: " + formula + " \n");
    }

    public static void printSKNFTableCalculatingMethod(String formula) {
        System.out.println("Минимизированная СКНФ расчётно-табличным методом: " + formula + " \n");
    }

    public static void printSDNFTableMethod(String formula) {
        System.out.println("Минимизированная СДНФ табличным методом: " + formula + " \n");
    }

    public static void printSKNFTableMethod(String formula) {
        System.out.println("Минимизированная СКНФ табличным методом: " + formula + "  \n");
    }
}