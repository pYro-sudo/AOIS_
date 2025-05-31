package by.losik;

import java.util.ArrayList;
import java.util.List;

public class DiagonalMatrix {
    private int[][] matrix;
    private int rows;
    private int columns;

    public DiagonalMatrix(int rows, int cols) {
        this.rows = rows;
        this.columns = cols;
        this.matrix = new int[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if(j != cols -1) {
                    matrix[i][j] = 1;
                }
            }
        }
    }

    public void setWord(List<Integer> word, int startRow, int col) {
        for (int i = 0; i < word.size(); i++) {
            int row = (startRow + i) % rows;
            matrix[row][col] = word.get(i);
        }
    }

    public void setIndexColumn(List<Integer> index, int startRow, int startColumn) {
        for (int i = 0; i < index.size(); i++) {
            int row = (startRow + i) % rows;
            int col = (startColumn + i) % columns;
            matrix[row][col] = index.get(i);
        }
    }

    public List<Integer> getWord(int startRow, int col, int length) {
        List<Integer> word = new ArrayList<>();
        for (int i = 0; i < length; i++) {
            int row = (startRow + i) % rows;
            word.add(matrix[row][col]);
        }
        return word;
    }

    public List<Integer> getIndexColumn(int startRow, int startColumn, int length) {
        List<Integer> index = new ArrayList<>();
        for (int i = 0; i < length; i++) {
            int row = (startRow + i) % rows;
            int col = (startColumn + i) % columns;
            index.add(matrix[row][col]);
        }
        return index;
    }

    // Функция f2 - логическое НЕТ
    public List<Integer> f2(int col1, int col2) {
        List<Integer> word1 = getWord(col1, col1, rows);
        List<Integer> word2 = getWord(col2, col2, rows);
        List<Integer> result = new ArrayList<>();
        for (int i = 0; i < rows; i++) {
            result.add((word1.get(i) != 0) & !(word2.get(i) != 0) ? 1 : 0);
        }
        return result;
    }

    // Функция f7 - логическое ИЛИ
    public List<Integer> f7(int col1, int col2) {
        List<Integer> word1 = getWord(col1, col1, rows);
        List<Integer> word2 = getWord(col2, col2, rows);
        List<Integer> result = new ArrayList<>();
        for (int i = 0; i < rows; i++) {
            result.add(((word1.get(i) != 0) | (word2.get(i) != 0)) ? 1 : 0);
        }
        return result;
    }

    // Функция f8 - логическое ИЛИ НЕ
    public List<Integer> f8(int col1, int col2) {
        List<Integer> word1 = getWord(col1, col1, rows);
        List<Integer> word2 = getWord(col2, col2, rows);
        List<Integer> result = new ArrayList<>();
        for (int i = 0; i < rows; i++) {
            result.add(!((word1.get(i) != 0) & (word2.get(i) != 0)) ? 1 : 0);
        }
        return result;
    }

    // Функция f13 - импликация 1 аргумента
    public List<Integer> f13(int col1, int col2) {
        List<Integer> word1 = getWord(col1, col1, rows);
        List<Integer> word2 = getWord(col2, col2, rows);
        List<Integer> result = new ArrayList<>();
        for (int i = 0; i < rows; i++) {
            result.add((word1.get(i) == 0) | (word2.get(i) != 0) ? 1 : 0);
        }
        return result;
    }

    public List<Integer> addBinary(List<Integer> a, List<Integer> b) {
        List<Integer> result = new ArrayList<>();
        int carry = 0;
        for (int i = a.size() - 1; i >= 0; i--) {
            int total = a.get(i) + b.get(i) + carry;
            result.add(0, total % 2);
            carry = total / 2;
        }
        if (carry != 0) {
            result.add(0, carry);
        }
        return result;
    }

    public void sumFields(List<Integer> keyBits) {
        for (int column = 0; column < columns; column++) {
            List<Integer> word = getWord(column, column, rows);
            boolean match = true;
            for (int i = 0; i < keyBits.size(); i++) {
                if (word.get(i) != keyBits.get(i)) {
                    match = false;
                    break;
                }
            }
            if (match) {
                int length = (word.size() - keyBits.size() - 1) / 3;
                List<Integer> A = word.subList(keyBits.size(), keyBits.size() + length);
                List<Integer> B = word.subList(keyBits.size() + A.size(), keyBits.size() + A.size() + length);
                List<Integer> sum = addBinary(A, B);
                List<Integer> newWord = new ArrayList<>(keyBits);
                newWord.addAll(A);
                newWord.addAll(B);
                newWord.addAll(sum);
                setWord(newWord, column, column);
            }
        }
    }

    public int compare(List<Integer> value1, List<Integer> value2) {
        for (int i = 0; i < value1.size(); i++) {
            if (value1.get(i) > value2.get(i)) {
                return 1;
            } else if (value1.get(i) < value2.get(i)) {
                return -1;
            }
        }
        return 0;
    }

    public List<Boolean> findWordsInRange(List<Integer> top, List<Integer> bottom) {
        List<Boolean> flags = new ArrayList<>();
        for (int i = 0; i < rows; i++) {
            List<Integer> currentWord = getWord(i, i, rows);
            int topCompare = compare(top, currentWord);
            int bottomCompare = compare(bottom, currentWord);
            flags.add(topCompare >= 0 && bottomCompare <= 0);
        }
        return flags;
    }

    public static void main(String[] args) {
        DiagonalMatrix matrix = new DiagonalMatrix(16, 16);

        System.out.println("Матрица:");
        for (int[] row : matrix.matrix) {
            for (int val : row) {
                System.out.print(val + " ");
            }
            System.out.println();
        }

        matrix.setWord(List.of(1, 1, 0, 1, 1, 0, 1, 0, 0, 1, 0, 1, 1, 0, 1, 1), 0, 0);
        matrix.setWord(List.of(1, 0, 1, 1, 0, 1, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0), 1, 1);
        matrix.setWord(List.of(0, 0, 0, 0, 0, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1), 2, 2);
        matrix.setIndexColumn(List.of(0, 0, 0, 1, 1, 1, 1, 1, 0, 0, 0, 1, 1, 1, 1, 1), 1, 0);
        matrix.setIndexColumn(List.of(1, 1, 1, 1, 1, 0, 1, 1, 0, 1, 0, 1, 1, 0, 1, 1), 3, 1);

        System.out.println("Матрица после записи слов и адресных столбцов:");
        for (int[] row : matrix.matrix) {
            for (int val : row) {
                System.out.print(val + " ");
            }
            System.out.println();
        }

        System.out.println("Слово из столбца 0: " + matrix.getWord(0, 0, 16));
        System.out.println("Слово из столбца 1: " + matrix.getWord(1, 1, 16));
        System.out.println("Слово из столбца 2: " + matrix.getWord(2, 2, 16));
        System.out.println("Адресный столбец 1 из 0 столбца: " + matrix.getIndexColumn(1, 0, 16));
        System.out.println("Адресный столбец 3 из 1 столбца: " + matrix.getIndexColumn(3, 1, 16));

        System.out.println("Функция f2 (ИЛИ): " + matrix.f2(1, 2));
        System.out.println("Функция f7 (ИЛИ-НЕ): " + matrix.f7(1, 2));
        System.out.println("Функция f8 (НЕ И): " + matrix.f8(1, 2));
        System.out.println("Функция f13 (Импликация): " + matrix.f13(1, 2));

        matrix.sumFields(List.of(1, 0, 1));
        System.out.println("Матрица после сложения полей:");
        for (int[] row : matrix.matrix) {
            for (int val : row) {
                System.out.print(val + " ");
            }
            System.out.println();
        }

        System.out.println("\nПоиск в диапазоне\n");

        matrix.findWordsInRange(
                List.of(0, 1, 0, 1, 1, 0, 1, 0, 0, 1, 0, 1, 1, 0, 1, 1),
                List.of(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)
        ).forEach(x -> System.out.print(x ? 1 + " " : 0 + " "));

    }
}