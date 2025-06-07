package by.losik;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

class KarnaughMinimizer {
    private final Minimizer minimizer;

    public KarnaughMinimizer() {
        this.minimizer = new Minimizer();
    }

    public String minimizeSDNFByKarnaugh(String sdnf) {
        List<List<String>> parsedSDNF = minimizer.parseFormule(sdnf);
        List<String> variableOrder = getVariableOrder(parsedSDNF);
        int n = variableOrder.size();
        if (n > 5) throw new IllegalArgumentException("Supported up to 5 variables");

        List<List<Integer>> cases = generateCases(n);
        Map<List<Integer>, Integer> casesValuesPairs = findSDNFValues(cases, parsedSDNF, variableOrder);
        int[][] kmap = buildKarnaughMap(casesValuesPairs, n);
        IOSystem.printKarnaughMap(kmap);
        return minimizeKarnaughToDNF(kmap, n, variableOrder);
    }

    public String minimizeSKNFByKarnaugh(String sknf) {
        List<List<String>> parsedSKNF = minimizer.parseFormule(sknf);
        List<String> variableOrder = getVariableOrder(parsedSKNF);
        int n = variableOrder.size();
        if (n > 5) throw new IllegalArgumentException("Supported up to 5 variables");

        List<List<Integer>> cases = generateCases(n);
        Map<List<Integer>, Integer> casesValuesPairs = findSKNFValues(cases, parsedSKNF, variableOrder);
        int[][] kmap = buildKarnaughMap(casesValuesPairs, n);
        IOSystem.printKarnaughMap(kmap);
        return minimizeKarnaughToCNF(kmap, n, variableOrder);
    }

    private List<String> getVariableOrder(List<List<String>> parsedFormule) {
        Set<String> variables = new HashSet<>();
        for (List<String> constituent : parsedFormule) {
            for (String literal : constituent) {
                String varName = literal.startsWith("!") ? literal.substring(1) : literal;
                variables.add(varName);
            }
        }
        return new ArrayList<>(variables);
    }

    private List<List<Integer>> generateCases(int n) {
        List<List<Integer>> cases = new ArrayList<>();
        int total = 1 << n;
        for (int i = 0; i < total; i++) {
            List<Integer> combination = new ArrayList<>();
            for (int j = 0; j < n; j++) {
                int bit = (i >> j) & 1;
                combination.add(bit);
            }
            cases.add(combination);
        }
        return cases;
    }

    private Map<List<Integer>, Integer> findSDNFValues(List<List<Integer>> cases, List<List<String>> parsedSDNF, List<String> variableOrder) {
        Map<List<Integer>, Integer> results = new HashMap<>();
        for (List<Integer> caseValues : cases) {
            boolean formulaResult = false;
            for (List<String> constituent : parsedSDNF) {
                boolean constituentResult = true;
                for (String literal : constituent) {
                    boolean isNegated = literal.startsWith("!");
                    String varName = isNegated ? literal.substring(1) : literal;
                    int varIndex = variableOrder.indexOf(varName);
                    int varValue = caseValues.get(varIndex);
                    boolean literalResult = isNegated ? (varValue == 0) : (varValue == 1);
                    constituentResult = constituentResult && literalResult;
                    if (!constituentResult) break;
                }
                if (constituentResult) {
                    formulaResult = true;
                    break;
                }
            }
            results.put(caseValues, formulaResult ? 1 : 0);
        }
        return results;
    }

    private Map<List<Integer>, Integer> findSKNFValues(List<List<Integer>> cases, List<List<String>> parsedSKNF, List<String> variableOrder) {
        Map<List<Integer>, Integer> results = new HashMap<>();
        for (List<Integer> caseValues : cases) {
            boolean formulaResult = true;
            for (List<String> disjunction : parsedSKNF) {
                boolean disjunctionResult = false;
                for (String literal : disjunction) {
                    boolean isNegated = literal.startsWith("!");
                    String varName = isNegated ? literal.substring(1) : literal;
                    int varIndex = variableOrder.indexOf(varName);
                    int varValue = caseValues.get(varIndex);
                    boolean literalResult = isNegated ? (varValue == 0) : (varValue == 1);
                    if (literalResult) {
                        disjunctionResult = true;
                        break;
                    }
                }
                if (!disjunctionResult) {
                    formulaResult = false;
                    break;
                }
            }
            results.put(caseValues, formulaResult ? 1 : 0);
        }
        return results;
    }

    public int[][] buildKarnaughMap(Map<List<Integer>, Integer> results, int n) {
        int rows, cols;
        switch (n) {
            case 1:
                rows = 1;
                cols = 2;
                break;
            case 2:
                rows = 2;
                cols = 2;
                break;
            case 3:
                rows = 2;
                cols = 4;
                break;
            case 4:
                rows = 4;
                cols = 4;
                break;
            case 5:
                rows = 4;
                cols = 8;
                break;
            default:
                throw new IllegalArgumentException("Поддерживается до 5 переменных");
        }

        // Создаём двумерный массив нужного размера, все элементы инициализируются нулями
        int[][] kmap = new int[rows][cols];

        // Перебираем каждую запись в Map: ключ — это List<Integer> caseValues, значение — 0 или 1 (или любая int)
        for (Map.Entry<List<Integer>, Integer> entry : results.entrySet()) {
            List<Integer> caseValues = entry.getKey();
            int value = entry.getValue();

            // Предполагается, что эти методы возвращают правильную ячейку (индекс строки/столбца) для указанного набора переменных
            int row = getRowIndex(caseValues, n);
            int col = getColIndex(caseValues, n);

            // Записываем значение в соответствующую ячейку
            kmap[row][col] = value;
        }

        // Возвращаем готовую карту Карно
        return kmap;
    }


    private int getGrayCode(int n) {
        return n ^ (n >> 1);
    }

    private int getRowIndex(List<Integer> values, int n) {
        if (n == 1) return 0;
        if (n == 2) return values.get(0);
        if (n == 3) return values.get(0);
        if (n == 4 || n == 5) {
            int a = values.get(0);
            int b = values.get(1);
            return getGrayCode((a << 1) | b);
        }
        throw new IllegalArgumentException("Unsupported amount of variables");
    }

    private int getColIndex(List<Integer> values, int n) {
        if (n == 1) return values.get(0);
        if (n == 2) return values.get(1);

        if (n == 3) {
            int b = values.get(1);
            int c = values.get(2);
            int binaryValue = (b << 1) | c;
            int[] grayOrder = { 0, 1, 3, 2 };
            return indexOf(grayOrder, binaryValue);
        }

        if (n == 4) {
            int c = values.get(2);
            int d = values.get(3);
            int binaryValue = (c << 1) | d;
            int[] grayOrder = { 0, 1, 3, 2 };
            return indexOf(grayOrder, binaryValue);
        }

        if (n == 5) {
            int c = values.get(2);
            int d = values.get(3);
            int e = values.get(4);
            int binaryValue = (c << 2) | (d << 1) | e;
            int[] grayOrder = { 0, 1, 3, 2, 6, 7, 5, 4 };
            return indexOf(grayOrder, binaryValue);
        }

        throw new IllegalArgumentException("Unsupported amount of variables");
    }

    // Вспомогательный метод, аналог C# Array.IndexOf
    private int indexOf(int[] array, int value) {
        for (int i = 0; i < array.length; i++) {
            if (array[i] == value) {
                return i;
            }
        }
        return -1; // не найдено
    }


    private List<List<Integer>> findKarnaughGroups(int[][] kmap, int targetValue, int n) {
        int rows = kmap.length;
        int cols = kmap[0].length;
        List<List<Integer>> groups = new ArrayList<>();
        Set<String> uniqueGroups = new HashSet<>();

        List<Map.Entry<Integer, Integer>> dimensions = getPossibleGroupSizes(rows, cols);
        for (Map.Entry<Integer, Integer> dim : dimensions) {
            int h = dim.getKey();
            int w = dim.getValue();
            List<Integer> startRows = h == rows ? Collections.singletonList(0) : new ArrayList<>();
            if (h != rows) for (int i = 0; i < rows; i++) startRows.add(i);

            for (int s : startRows) {
                for (int c = 0; c < cols; c++) {
                    List<Map.Entry<Integer, Integer>> cells = new ArrayList<>();
                    boolean isValid = true;

                    for (int row = s; row < s + h; row++) {
                        int currentRow = row % rows;
                        for (int i = 0; i < w; i++) {
                            int currentCol = (c + i) % cols;
                            if (kmap[currentRow][currentCol] != targetValue) {
                                isValid = false;
                                break;
                            }
                            cells.add(new AbstractMap.SimpleEntry<>(currentRow, currentCol));
                        }
                        if (!isValid) break;
                    }

                    if (isValid) {
                        List<Integer> coordinates = cells.stream()
                                .flatMap(cell -> Stream.of(cell.getKey(), cell.getValue()))
                                .collect(Collectors.toList());
                        List<Map.Entry<Integer, Integer>> sortedCells = new ArrayList<>(cells);
                        Collections.sort(sortedCells, Comparator.comparingInt((Map.Entry<Integer, Integer> e) -> e.getKey()).thenComparingInt(Map.Entry::getValue));
                        StringBuilder keyBuilder = new StringBuilder();
                        for (int i = 0; i < sortedCells.size(); i++) {
                            Map.Entry<Integer, Integer> cell = sortedCells.get(i);
                            keyBuilder.append(cell.getKey()).append(",").append(cell.getValue());
                            if (i < sortedCells.size() - 1) {
                                keyBuilder.append(";");
                            }
                        }
                        String key = keyBuilder.toString();
                        if (!uniqueGroups.contains(key)) {
                            uniqueGroups.add(key);
                            groups.add(coordinates);
                        }
                    }
                }
            }
        }

        if (n == 5) {
            groups.addAll(findAdditionalGroupsByMinglingMaps(new int[]{0, 3, 4, 7}, targetValue, kmap,
                    Arrays.asList(new AbstractMap.SimpleEntry<>(0, 0), new AbstractMap.SimpleEntry<>(1, 3),
                            new AbstractMap.SimpleEntry<>(2, 4), new AbstractMap.SimpleEntry<>(3, 7))));
            groups.addAll(findAdditionalGroupsByMinglingMaps(new int[]{1, 2, 5, 6}, targetValue, kmap,
                    Arrays.asList(new AbstractMap.SimpleEntry<>(0, 1), new AbstractMap.SimpleEntry<>(1, 2),
                            new AbstractMap.SimpleEntry<>(2, 5), new AbstractMap.SimpleEntry<>(3, 6))));
        }
        return filterGroups(groups, kmap, targetValue);
    }

    private List<List<Integer>> findAdditionalGroupsByMinglingMaps(int[] colIndexesForSubMap, int targetValue, int[][] kmap, List<Map.Entry<Integer, Integer>> translationList) {
        int[][] subKMap = new int[4][4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                subKMap[j][i] = kmap[j][colIndexesForSubMap[i]];
            }
        }
        List<List<Integer>> additionalGroups = findKarnaughGroups(subKMap, targetValue, 4);
        translateCoordinatesFromSubMapToGeneralMap(additionalGroups, translationList);
        return additionalGroups;
    }

    private void translateCoordinatesFromSubMapToGeneralMap(List<List<Integer>> subGroups, List<Map.Entry<Integer, Integer>> translationList) {
        for (List<Integer> subGroup : subGroups) {
            for (int j = 0; j < subGroup.size(); j++) {
                if ((j + 1) % 2 == 0) {
                    for (Map.Entry<Integer, Integer> rule : translationList) {
                        if (subGroup.get(j).equals(rule.getKey())) {
                            subGroup.set(j, rule.getValue());
                            break;
                        }
                    }
                }
            }
        }
    }

    private List<Map.Entry<Integer, Integer>> getPossibleGroupSizes(int rows, int cols) {
        List<Map.Entry<Integer, Integer>> sizes = new ArrayList<>();
        int[] rowSizes = rows == 1 ? new int[]{1} : new int[]{1, 2, 4};
        int[] colSizes = new int[]{1, 2, 4, 8};
        for (int r : rowSizes) {
            if (r > rows) continue;
            for (int c : colSizes) {
                if (c <= cols && r * c >= 1) {
                    sizes.add(new AbstractMap.SimpleEntry<>(r, c));
                }
            }
        }
        return sizes;
    }

    private List<List<Integer>> filterGroups(List<List<Integer>> groups, int[][] kmap, int targetValue) {
        List<List<Integer>> filteredGroups = new ArrayList<>();
        List<List<Integer>> sortedGroups = new ArrayList<>(groups);
        Collections.sort(sortedGroups, (g1, g2) -> {
            return g2.size() - g1.size(); // Reversed order: larger size comes first
        });

        for (List<Integer> group : sortedGroups) {
            boolean isSubset = filteredGroups.stream().anyMatch(existing -> isSubset(group, existing));
            if (!isSubset) filteredGroups.add(group);
        }

        int n = kmap[0].length == 8 ? 5 : 4;
        if (n == 5) {
            filteredGroups = filterGroupsForFiveVariables(filteredGroups, kmap);
        }

        for (int i = 0; i < filteredGroups.size(); i++) {
            if (isCoveredByUnion(filteredGroups.get(i), filteredGroups)) {
                filteredGroups.remove(i);
                i--;
            }
        }
        return filteredGroups;
    }

    private List<List<Integer>> filterGroupsForFiveVariables(List<List<Integer>> groups, int[][] kmap) {
        List<List<Integer>> validGroups = new ArrayList<>();
        int rows = kmap.length;
        int cols = kmap[0].length;

        for (List<Integer> group : groups) {
            List<Integer> uniqueCols = IntStream.range(0, group.size())
                    .filter(idx -> idx % 2 == 1)          // Select odd indices
                    .mapToObj(group::get)                 // Get elements at those indices
                    .distinct()                           // Remove duplicates
                    .collect(Collectors.toList());

            List<Integer> uniqueRows = IntStream.range(0, group.size())
                    .filter(idx -> idx % 2 == 0)          // Select even indices
                    .mapToObj(group::get)                 // Get elements at those indices
                    .distinct()                           // Remove duplicates
                    .collect(Collectors.toList());


            boolean isValid = true;
            if (uniqueCols.stream().anyMatch(c -> c < 4) && uniqueCols.stream().anyMatch(c -> c >= 4)) {
                List<Integer> leftCols = uniqueCols.stream().filter(c -> c < 4).collect(Collectors.toList());
                List<Integer> rightCols = uniqueCols.stream().filter(c -> c >= 4).collect(Collectors.toList());
                boolean leftValid = checkSubGroupValidity(uniqueRows, leftCols);
                boolean rightValid = checkSubGroupValidity(uniqueRows, rightCols);
                isValid = leftValid && rightValid;
            } else {
                isValid = checkSubGroupValidity(uniqueRows, uniqueCols);
            }

            if (isValid) validGroups.add(group);
        }
        return validGroups;
    }

    private boolean checkSubGroupValidity(List<Integer> uniqueRows, List<Integer> uniqueCols) {
        if (uniqueCols.isEmpty() && uniqueRows.isEmpty()) return false;
        int rowCount = uniqueRows.size();
        int colCount = uniqueCols.size();
        return isPowerOfTwo(rowCount) && isPowerOfTwo(colCount);
    }

    private boolean isPowerOfTwo(int x) {
        return x > 0 && (x & (x - 1)) == 0;
    }

    private boolean isSubset(List<Integer> group, List<Integer> superGroup) {
        List<Map.Entry<Integer, Integer>> groupPairs = new ArrayList<>();
        for (int i = 0; i < group.size(); i += 2) {
            groupPairs.add(new AbstractMap.SimpleEntry<>(group.get(i), group.get(i + 1)));
        }
        List<Map.Entry<Integer, Integer>> superGroupPairs = new ArrayList<>();
        for (int i = 0; i < superGroup.size(); i += 2) {
            superGroupPairs.add(new AbstractMap.SimpleEntry<>(superGroup.get(i), superGroup.get(i + 1)));
        }
        return groupPairs.stream().allMatch(superGroupPairs::contains);
    }

    private boolean isCoveredByUnion(List<Integer> group, List<List<Integer>> allGroups) {
        List<Map.Entry<Integer, Integer>> groupPairs = new ArrayList<>();
        for (int i = 0; i < group.size(); i += 2) {
            groupPairs.add(new AbstractMap.SimpleEntry<>(group.get(i), group.get(i + 1)));
        }
        List<Map.Entry<Integer, Integer>> unitedGroups = new ArrayList<>();
        for (List<Integer> other : allGroups) {
            if (other.equals(group)) continue;
            for (int i = 0; i < other.size(); i += 2) {
                unitedGroups.add(new AbstractMap.SimpleEntry<>(other.get(i), other.get(i + 1)));
            }
        }
        return groupPairs.stream().allMatch(unitedGroups::contains);
    }

    private String minimizeKarnaughToDNF(int[][] kmap, int n, List<String> variableOrder) {
        List<List<Integer>> groups = findKarnaughGroups(kmap, 1, n);
        List<String> terms = new ArrayList<>();

        for (List<Integer> group : groups) {
            List<Set<Integer>> varValues = new ArrayList<>();
            for (int i = 0; i < n; i++) varValues.add(new HashSet<>());
            for (int i = 0; i < group.size(); i += 2) {
                int row = group.get(i);
                int col = group.get(i + 1);
                List<Integer> values = getVariableValues(row, col, n);
                for (int j = 0; j < n; j++) varValues.get(j).add(values.get(j));
            }

            List<String> termParts = new ArrayList<>();
            for (int i = 0; i < n; i++) {
                if (varValues.get(i).size() == 1) {
                    int value = varValues.get(i).iterator().next();
                    termParts.add(value == 1 ? variableOrder.get(i) : "!" + variableOrder.get(i));
                }
            }

            if (termParts.isEmpty()) return "1";
            terms.add("(" + String.join("/\\", termParts) + ")");
        }
        return terms.isEmpty() ? "0" : String.join("\\/", terms);
    }

    private String minimizeKarnaughToCNF(int[][] kmap, int n, List<String> variableOrder) {
        List<List<Integer>> groups = findKarnaughGroups(kmap, 0, n);
        List<String> terms = new ArrayList<>();

        for (List<Integer> group : groups) {
            List<Set<Integer>> varValues = new ArrayList<>();
            for (int i = 0; i < n; i++) varValues.add(new HashSet<>());
            for (int i = 0; i < group.size(); i += 2) {
                int row = group.get(i);
                int col = group.get(i + 1);
                List<Integer> values = getVariableValues(row, col, n);
                for (int j = 0; j < n; j++) varValues.get(j).add(values.get(j));
            }

            List<String> termParts = new ArrayList<>();
            for (int i = 0; i < n; i++) {
                if (varValues.get(i).size() == 1) {
                    int value = varValues.get(i).iterator().next();
                    termParts.add(value == 1 ? "!" + variableOrder.get(i) : variableOrder.get(i));
                }
            }

            if (termParts.isEmpty()) return "0";
            terms.add("(" + String.join("\\/", termParts) + ")");
        }
        return terms.isEmpty() ? "1" : String.join("/\\", terms);
    }

    private List<Integer> getVariableValues(int row, int col, int n) {
        List<Integer> values = new ArrayList<>();
        if (n == 1) {
            values.add(col);
        } else if (n == 2) {
            values.add(row);
            values.add(col);
        } else if (n == 3) {
            values.add(row);
            Map.Entry<Integer, Integer> bc = getBCFromColumn(col);
            values.add(bc.getKey());
            values.add(bc.getValue());
        } else if (n == 4) {
            Map.Entry<Integer, Integer> ab = getABFromRow(row);
            values.add(ab.getKey());
            values.add(ab.getValue());
            Map.Entry<Integer, Integer> cd = getCDFromColumn(col);
            values.add(cd.getKey());
            values.add(cd.getValue());
        } else if (n == 5) {
            Map.Entry<Integer, Integer> ab = getABFromRow(row);
            values.add(ab.getKey());
            values.add(ab.getValue());
            List<Triplet<Integer, Integer, Integer>> cdeList = getCDEFromColumn(col);
            Triplet<Integer, Integer, Integer> cde = cdeList.get(0); // Берем первый (и единственный) триплет
            values.add(cde.getKey());
            values.add(cde.getValue());
            values.add(cde.getExtra());
        }
        return values;
    }

    private Map.Entry<Integer, Integer> getABFromRow(int row) {
        switch (row) {
            case 0: return new AbstractMap.SimpleEntry<>(0, 0);
            case 1: return new AbstractMap.SimpleEntry<>(0, 1);
            case 2: return new AbstractMap.SimpleEntry<>(1, 1);
            case 3: return new AbstractMap.SimpleEntry<>(1, 0);
            default: throw new IllegalArgumentException("Invalid row index");
        }
    }

    private Map.Entry<Integer, Integer> getCDFromColumn(int col) {
        return getBCFromColumn(col);
    }

    private Map.Entry<Integer, Integer> getBCFromColumn(int col) {
        switch (col) {
            case 0: return new AbstractMap.SimpleEntry<>(0, 0);
            case 1: return new AbstractMap.SimpleEntry<>(0, 1);
            case 2: return new AbstractMap.SimpleEntry<>(1, 1);
            case 3: return new AbstractMap.SimpleEntry<>(1, 0);
            default: throw new IllegalArgumentException("Invalid column index");
        }
    }

    private List<Triplet<Integer, Integer, Integer>> getCDEFromColumn(int col) {
        Triplet<Integer, Integer, Integer> triplet;
        switch (col) {
            case 0: triplet = new Triplet<>(0, 0, 0); break;
            case 1: triplet = new Triplet<>(0, 0, 1); break;
            case 2: triplet = new Triplet<>(0, 1, 1); break;
            case 3: triplet = new Triplet<>(0, 1, 0); break;
            case 4: triplet = new Triplet<>(1, 1, 0); break;
            case 5: triplet = new Triplet<>(1, 1, 1); break;
            case 6: triplet = new Triplet<>(1, 0, 1); break;
            case 7: triplet = new Triplet<>(1, 0, 0); break;
            default: throw new IllegalArgumentException("Недопустимый индекс столбца для 5 переменных");
        }
        return Collections.singletonList(triplet);
    }

    // Custom triplet class since Java doesn't have a built-in Tuple for three elements
    private static class Triplet<K, V, E> implements Map.Entry<K, V> {
        private final K key;
        private final V value;
        private final E extra;

        Triplet(K key, V value, E extra) {
            this.key = key;
            this.value = value;
            this.extra = extra;
        }

        @Override public K getKey() { return key; }
        @Override public V getValue() { return value; }
        public E getExtra() { return extra; }
        @Override public V setValue(V value) { throw new UnsupportedOperationException(); }
    }

}