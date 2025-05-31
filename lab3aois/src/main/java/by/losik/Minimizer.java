package by.losik;//package by.losik;
//
//import java.util.*;
//
//public class Minimizer {
//    public String minimizeSDNFByCalculating(String sdnf) {
//        List<List<String>> parsedSDNF = parseFormula(sdnf);
//        List<List<String>> minimizedSDNF = minimizeFormulaByMingling(parsedSDNF);
//        List<List<String>> simplifiedSDNF = findRedundantSDNFByCalculation(parsedSDNF, minimizedSDNF);
//        return buildDNFString(simplifiedSDNF);
//    }
//
//    public String minimizeSKNFByCalculating(String sknf) {
//        List<List<String>> parsedSKNF = parseFormula(sknf);
//        List<List<String>> minimizedSKNF = minimizeFormulaByMingling(parsedSKNF);
//        List<List<String>> simplifiedSKNF = findRedundantSKNFByCalculation(parsedSKNF, minimizedSKNF);
//        return buildCNFString(simplifiedSKNF);
//    }
//
//    public String minimizeSDNFByTableAndCalculating(String sdnf) {
//        List<List<String>> parsedSDNF = parseFormula(sdnf);
//        List<List<String>> minimizedSDNF = minimizeFormulaByMingling(parsedSDNF);
//        List<List<String>> essentialImplicants = findRedundantByTable(parsedSDNF, minimizedSDNF);
//        printSDNFTable(parsedSDNF, minimizedSDNF);
//        return buildDNFString(essentialImplicants);
//    }
//
//    public String minimizeSKNFByTableAndCalculating(String sknf) {
//        List<List<String>> parsedSKNF = parseFormula(sknf);
//        List<List<String>> minimizedSKNF = minimizeFormulaByMingling(parsedSKNF);
//        List<List<String>> essentialImplicants = findRedundantByTable(parsedSKNF, minimizedSKNF);
//        printSKNFTable(parsedSKNF, minimizedSKNF);
//        return buildCNFString(essentialImplicants);
//    }
//
//    private List<List<String>> parseFormula(String formula) {
//        List<List<String>> parsedFormula = new ArrayList<>();
//        int index = 0;
//        for (int i = 0; i < formula.length(); i++) {
//            if (formula.charAt(i) == '(') {
//                parsedFormula.add(new ArrayList<>());
//                while (i < formula.length() && formula.charAt(i) != ')') {
//                    if ((formula.charAt(i) >= 'a' && formula.charAt(i) <= 'z') ||
//                            (formula.charAt(i) >= 'A' && formula.charAt(i) <= 'Z')) {
//                        parsedFormula.get(index).add(String.valueOf(formula.charAt(i)));
//                    } else if (formula.charAt(i) == '!') {
//                        if (i + 1 < formula.length()) {
//                            parsedFormula.get(index).add("!" + formula.charAt(i + 1));
//                            i++;
//                        }
//                    }
//                    i++;
//                }
//                index++;
//            }
//        }
//        return parsedFormula;
//    }
//
//    private List<List<String>> minimizeFormulaByMingling(List<List<String>> parsedFormula) {
//        if (parsedFormula.stream().anyMatch(innerList -> innerList.size() == 1)) {
//            return distinctValue(parsedFormula);
//        }
//
//        List<List<String>> minimizedFormula = new ArrayList<>();
//        List<List<String>> parsedFormulaCopy = new ArrayList<>();
//        for (List<String> term : parsedFormula) {
//            parsedFormulaCopy.add(new ArrayList<>(term));
//        }
//
//        for (int i = 0; i < parsedFormula.size() - 1; i++) {
//            for (int j = i + 1; j < parsedFormula.size(); j++) {
//                List<String> commonElements = new ArrayList<>();
//                List<String> unCommonElements = new ArrayList<>();
//                for (int k = 0; k < parsedFormula.get(i).size(); k++) {
//                    if (parsedFormula.get(i).get(k).equals(parsedFormula.get(j).get(k))) {
//                        commonElements.add(parsedFormula.get(i).get(k));
//                    } else if (parsedFormula.get(i).get(k).equals(returnReversedElement(parsedFormula.get(j).get(k)))) {
//                        unCommonElements.add(parsedFormula.get(i).get(k));
//                    }
//                }
//
//                if (commonElements.size() == parsedFormula.get(i).size() - 1 && unCommonElements.size() == 1) {
//                    minimizedFormula.add(commonElements);
//                    int finalI = i;
//                    parsedFormulaCopy.removeIf(term -> term.equals(parsedFormula.get(finalI)));
//                    int finalJ = j;
//                    parsedFormulaCopy.removeIf(term -> term.equals(parsedFormula.get(finalJ)));
//                }
//            }
//        }
//        minimizedFormula.addAll(parsedFormulaCopy);
//
//        // Check if minimization changed anything
//        boolean unchanged = true;
//        if (minimizedFormula.size() == parsedFormula.size()) {
//            for (int i = 0; i < minimizedFormula.size(); i++) {
//                if (!minimizedFormula.get(i).equals(parsedFormula.get(i))) {
//                    unchanged = false;
//                    break;
//                }
//            }
//        } else {
//            unchanged = false;
//        }
//
//        return unchanged ? distinctValue(minimizedFormula) : minimizeFormulaByMingling(minimizedFormula);
//    }
//
//    private List<List<String>> findRedundantSDNFByCalculation(List<List<String>> parsedSDNF, List<List<String>> minimizedSDNF) {
//        List<List<String>> result = new ArrayList<>();
//        for (List<String> term : minimizedSDNF) {
//            result.add(new ArrayList<>(term));
//        }
//
//        for (int i = 0; i < minimizedSDNF.size(); i++) {
//            List<String> currentImplicant = minimizedSDNF.get(i);
//            Map<String, Integer> assignment = getAssignmentForSDNFImplicant(currentImplicant);
//            if (assignment == null) continue;
//
//            boolean isRedundant = checkIfRedundantSDNF(currentImplicant, minimizedSDNF, assignment);
//            if (isRedundant) {
//                result.removeIf(term -> term.equals(currentImplicant));
//            }
//        }
//        return result;
//    }
//
//    private Map<String, Integer> getAssignmentForSDNFImplicant(List<String> implicant) {
//        Map<String, Integer> assignment = new HashMap<>();
//        for (String literal : implicant) {
//            if (literal.startsWith("!")) {
//                String var = literal.substring(1);
//                assignment.put(var, 0);
//            } else {
//                assignment.put(literal, 1);
//            }
//        }
//        return assignment;
//    }
//
//    private boolean checkIfRedundantSDNF(List<String> implicant, List<List<String>> minimizedSDNF, Map<String, Integer> assignment) {
//        List<List<String>> minimizedSDNFCopy = new ArrayList<>();
//        for (List<String> term : minimizedSDNF) {
//            minimizedSDNFCopy.add(new ArrayList<>(term));
//        }
//
//        minimizedSDNFCopy.removeIf(term -> term.equals(implicant));
//
//        for (List<String> term : minimizedSDNFCopy) {
//            for (int i = 0; i < term.size(); i++) {
//                String var = term.get(i).startsWith("!") ? term.get(i).substring(1) : term.get(i);
//                if (assignment.containsKey(var)) {
//                    String newValue = term.get(i).startsWith("!") ?
//                            String.valueOf(returnReversedValue(assignment.get(var))) :
//                            String.valueOf(assignment.get(var));
//                    term.set(i, newValue);
//                }
//            }
//        }
//
//        List<List<String>> checkingSDNF = minimizeFormulaByMingling(minimizedSDNFCopy);
//        return checkingSDNF.stream().anyMatch(innerList -> innerList.contains("1") && innerList.size() == 1);
//    }
//
//    private List<List<String>> findRedundantSKNFByCalculation(List<List<String>> parsedSKNF, List<List<String>> minimizedSKNF) {
//        List<List<String>> result = new ArrayList<>();
//        for (List<String> term : minimizedSKNF) {
//            result.add(new ArrayList<>(term));
//        }
//
//        for (int i = 0; i < minimizedSKNF.size(); i++) {
//            List<String> currentImplicant = minimizedSKNF.get(i);
//            Map<String, Integer> assignment = getAssignmentForSKNFImplicant(currentImplicant);
//            if (assignment == null) continue;
//
//            boolean isRedundant = checkIfRedundantSKNF(currentImplicant, minimizedSKNF, assignment);
//            if (isRedundant) {
//                result.removeIf(term -> term.equals(currentImplicant));
//            }
//        }
//        return result;
//    }
//
//    private Map<String, Integer> getAssignmentForSKNFImplicant(List<String> implicant) {
//        Map<String, Integer> assignment = new HashMap<>();
//        for (String literal : implicant) {
//            if (literal.startsWith("!")) {
//                String var = literal.substring(1);
//                assignment.put(var, 1);
//            } else {
//                assignment.put(literal, 0);
//            }
//        }
//        return assignment;
//    }
//
//    private boolean checkIfRedundantSKNF(List<String> implicant, List<List<String>> minimizedSKNF, Map<String, Integer> assignment) {
//        List<List<String>> minimizedSKNFCopy = new ArrayList<>();
//        for (List<String> term : minimizedSKNF) {
//            minimizedSKNFCopy.add(new ArrayList<>(term));
//        }
//
//        minimizedSKNFCopy.removeIf(term -> term.equals(implicant));
//
//        for (List<String> term : minimizedSKNFCopy) {
//            for (int i = 0; i < term.size(); i++) {
//                String var = term.get(i).startsWith("!") ? term.get(i).substring(1) : term.get(i);
//                if (assignment.containsKey(var)) {
//                    String newValue = term.get(i).startsWith("!") ?
//                            String.valueOf(returnReversedValue(assignment.get(var))) :
//                            String.valueOf(assignment.get(var));
//                    term.set(i, newValue);
//                }
//            }
//        }
//
//        List<List<String>> checkingSKNF = minimizeFormulaByMingling(minimizedSKNFCopy);
//        return checkingSKNF.stream().anyMatch(innerList -> innerList.contains("0") && innerList.size() == 1);
//    }
//
//    private List<List<String>> findRedundantByTable(List<List<String>> parsedFormulas, List<List<String>> minimizedFormulas) {
//        List<List<String>> resultFormulas = new ArrayList<>();
//        int[] amountOfInclusions = new int[parsedFormulas.size()];
//        int index = 0;
//
//        for (List<String> constituent : parsedFormulas) {
//            for (List<String> implicant : minimizedFormulas) {
//                if (implicant.stream().allMatch(item -> constituent.contains(item))) {
//                    amountOfInclusions[index]++;
//                }
//            }
//            index++;
//        }
//
//        int minInclusions = Arrays.stream(amountOfInclusions).min().orElse(0);
//        for (int i = 0; i < parsedFormulas.size(); i++) {
//            if (amountOfInclusions[i] == minInclusions) {
//                for (List<String> implicant : minimizedFormulas) {
//                    int finalI = i;
//                    if (implicant.stream().allMatch(item -> parsedFormulas.get(finalI).contains(item))) {
//                        resultFormulas.add(implicant);
//                    }
//                }
//            }
//        }
//        return distinctValue(resultFormulas);
//    }
//
//    private String returnReversedElement(String element) {
//        if (element.startsWith("!")) {
//            return element.substring(1);
//        } else {
//            return "!" + element;
//        }
//    }
//
//    private int returnReversedValue(int value) {
//        return value == 0 ? 1 : 0;
//    }
//
//    private String buildDNFString(List<List<String>> terms) {
//        List<String> disjunctions = new ArrayList<>();
//        for (List<String> term : terms) {
//            if (term.isEmpty()) continue;
//            disjunctions.add("(" + String.join("&", term) + ")");
//        }
//        return disjunctions.isEmpty() ? "0" : String.join("|", disjunctions);
//    }
//
//    private String buildCNFString(List<List<String>> terms) {
//        List<String> conjunctions = new ArrayList<>();
//        for (List<String> term : terms) {
//            if (term.isEmpty()) continue;
//            conjunctions.add("(" + String.join("|", term) + ")");
//        }
//        return conjunctions.isEmpty() ? "1" : String.join("&", conjunctions);
//    }
//
//    private List<List<String>> distinctValue(List<List<String>> formulas) {
//        Set<List<String>> uniqueFormulas = new LinkedHashSet<>(formulas);
//        return new ArrayList<>(uniqueFormulas);
//    }
//
//    private void printSDNFTable(List<List<String>> parsedSDNF, List<List<String>> minimizedSDNF) {
//        System.out.println("SDNF Implicant Table:");
//        System.out.println("Constituents | Implicants");
//        for (List<String> constituent : parsedSDNF) {
//            System.out.print(constituent + "\t|\t");
//            for (List<String> implicant : minimizedSDNF) {
//                if (implicant.stream().allMatch(item -> constituent.contains(item))) {
//                    System.out.print("X ");
//                } else {
//                    System.out.print("  ");
//                }
//            }
//            System.out.println();
//        }
//    }
//
//    private void printSKNFTable(List<List<String>> parsedSKNF, List<List<String>> minimizedSKNF) {
//        System.out.println("SKNF Implicant Table:");
//        System.out.println("Constituents | Implicants");
//        for (List<String> constituent : parsedSKNF) {
//            System.out.print(constituent + "\t| ");
//            for (List<String> implicant : minimizedSKNF) {
//                if (implicant.stream().allMatch(item -> constituent.contains(item))) {
//                    System.out.print("X ");
//                } else {
//                    System.out.print("  ");
//                }
//            }
//            System.out.println();
//        }
//    }
//
//    public static void main(String[] args) {
//        Minimizer minimizer = new Minimizer();
//        // Test SDNF
//        String sdnf = "(!A&!B&!C&!D&!E) | (!A&!B&!C&D&!E) | (!A&!B&!C&D&E) | (!A&!B&C&!D&!E) | (!A&!B&C&D&!E) | (!A&!B&C&D&E) | (!A&B&!C&!D&E) | (!A&B&C&!D&!E) | (!A&B&C&D&!E) | (!A&B&C&D&E) | (A&!B&!C&!D&E) | (A&!B&C&!D&!E) | (A&!B&C&D&!E) | (A&!B&C&D&E) | (A&B&!C&!D&E) | (A&B&C&!D&!E) | (A&B&C&D&!E) | (A&B&C&D&E)";
//        System.out.println("SDNF Calculating: " + minimizer.minimizeSDNFByCalculating(sdnf));
//        System.out.println("SDNF Table: " + minimizer.minimizeSDNFByTableAndCalculating(sdnf));
//        // Test SKNF
//        String sknf = "(A|B|C|D|!E) & (A|B|!C|D|!E) & (A|!B|C|D|E) & (A|!B|C|!D|E) & (A|!B|C|!D|!E) & (A|!B|!C|D|!E) & (!A|B|C|D|E) & (!A|B|C|!D|E) & (!A|B|C|!D|!E) & (!A|B|!C|D|!E) & (!A|!B|C|D|E) & (!A|!B|C|!D|E) & (!A|!B|C|!D|!E) & (!A|!B|!C|D|!E)";
//        System.out.println("SKNF Calculating: " + minimizer.minimizeSKNFByCalculating(sknf));
//        System.out.println("SKNF Table: " + minimizer.minimizeSKNFByTableAndCalculating(sknf));
//    }
//}

import by.losik.ListUtils;

import java.util.*;

class Minimizer {
    public String minimizeSDNFByCalculating(String sdnf) {
        List<List<String>> parsedSDNF = parseFormule(sdnf);
        List<List<String>> minimizedSDNF = minimizeFormuleByMingling(parsedSDNF);
        List<List<String>> simplifiedSDNF = findRedundantSDNFByCalculation(parsedSDNF, minimizedSDNF);
        return buildDNFString(simplifiedSDNF);
    }

    public String minimizeSKNFByCalculating(String sknf) {
        List<List<String>> parsedSKNF = parseFormule(sknf);
        List<List<String>> minimizedSKNF = minimizeFormuleByMingling(parsedSKNF);
        List<List<String>> simplifiedSKNF = findRedundantSKNFByCalculation(parsedSKNF, minimizedSKNF);
        return buildCNFString(simplifiedSKNF);
    }

    public String minimizeSDNFByTableAndCalculating(String sdnf) {
        List<List<String>> parsedSDNF = parseFormule(sdnf);
        List<List<String>> minimizedSDNF = minimizeFormuleByMingling(parsedSDNF);
        List<List<String>> essentialImplicants = findRedundantByTable(parsedSDNF, minimizedSDNF);
        IOSystem.printSDNFTable(parsedSDNF, minimizedSDNF);
        return buildDNFString(essentialImplicants);
    }

    public String minimizeSKNFByTableAndCalculating(String sknf) {
        List<List<String>> parsedSKNF = parseFormule(sknf);
        List<List<String>> minimizedSKNF = minimizeFormuleByMingling(parsedSKNF);
        List<List<String>> essentialImplicants = findRedundantByTable(parsedSKNF, minimizedSKNF);
        IOSystem.printSKNFTable(parsedSKNF, minimizedSKNF);
        return buildCNFString(essentialImplicants);
    }

    List<List<String>> parseFormule(String formule) {
        List<List<String>> parsedFormule = new ArrayList<>();
        int index = 0;
        for (int i = 0; i < formule.length(); i++) {
            if (formule.charAt(i) == '(') {
                parsedFormule.add(new ArrayList<>());
                i++;
                while (i < formule.length() && formule.charAt(i) != ')') {
                    if (Character.isLetter(formule.charAt(i))) {
                        parsedFormule.get(index).add(String.valueOf(formule.charAt(i)));
                    } else if (formule.charAt(i) == '!') {
                        i++;
                        if (i < formule.length() && Character.isLetter(formule.charAt(i))) {
                            parsedFormule.get(index).add("!" + formule.charAt(i));
                        }
                    }
                    i++;
                }
                index++;
            }
        }
        return parsedFormule;
    }

    List<List<String>> minimizeFormuleByMingling(List<List<String>> parsedFormule) {
        if (parsedFormule.stream().anyMatch(innerList -> innerList.size() == 1)) {
            return ListUtils.distinctValue(parsedFormule);
        }

        List<List<String>> minimizedSdnf = new ArrayList<>();
        List<List<String>> parsedSdnfCopy = new ArrayList<>(parsedFormule);
        for (int i = 0; i < parsedFormule.size() - 1; i++) {
            for (int j = i + 1; j < parsedFormule.size(); j++) {
                List<String> commonElements = new ArrayList<>();
                List<String> unCommonElements = new ArrayList<>();
                for (int k = 0; k < parsedFormule.get(i).size(); k++) {
                    if (parsedFormule.get(i).get(k).equals(parsedFormule.get(j).get(k))) {
                        commonElements.add(parsedFormule.get(i).get(k));
                    } else if (parsedFormule.get(i).get(k).equals(returnReversedElement(parsedFormule.get(j).get(k)))) {
                        unCommonElements.add(parsedFormule.get(i).get(k));
                    }
                }

                if (commonElements.size() == parsedFormule.get(i).size() - 1 && unCommonElements.size() == 1) {
                    minimizedSdnf.add(commonElements);
                    parsedSdnfCopy.remove(parsedFormule.get(i));
                    parsedSdnfCopy.remove(parsedFormule.get(j));
                }
            }
        }
        minimizedSdnf.addAll(parsedSdnfCopy);

        return minimizedSdnf.equals(parsedFormule) ? ListUtils.distinctValue(minimizedSdnf) : minimizeFormuleByMingling(minimizedSdnf);
    }

    private List<List<String>> findRedundantSDNFByCalculation(List<List<String>> parsedSDNF, List<List<String>> minimizedSdnf) {
        List<List<String>> result = new ArrayList<>(minimizedSdnf);
        for (int i = 0; i < minimizedSdnf.size(); i++) {
            List<String> currentImplicant = minimizedSdnf.get(i);
            Map<String, Integer> assignment = getAssignmentForSDNFImplicant(currentImplicant);
            if (assignment == null) continue;
            boolean isRedundant = checkIfRedundantSDNF(currentImplicant, minimizedSdnf, assignment);
            if (isRedundant) {
                result.remove(currentImplicant);
            }
        }
        return result;
    }

    Map<String, Integer> getAssignmentForSDNFImplicant(List<String> implicant) {
        Map<String, Integer> assignment = new HashMap<>();
        for (String literal : implicant) {
            if (literal.startsWith("!")) {
                String var = literal.substring(1);
                assignment.put(var, 0);
            } else {
                assignment.put(literal, 1);
            }
        }
        return assignment;
    }

    boolean checkIfRedundantSDNF(List<String> implicant, List<List<String>> minimizedSdnf, Map<String, Integer> assignment) {
        List<List<String>> minimizedSdnfCopy = new ArrayList<>();
        for (List<String> imp : minimizedSdnf) {
            if (!imp.equals(implicant)) {
                minimizedSdnfCopy.add(new ArrayList<>(imp));
            }
        }

        for (List<String> imp : minimizedSdnfCopy) {
            for (int i = 0; i < imp.size(); i++) {
                String literal = imp.get(i);
                String var = literal.startsWith("!") ? literal.substring(1) : literal;
                if (assignment.containsKey(var)) {
                    int value = assignment.get(var);
                    imp.set(i, literal.startsWith("!") ? String.valueOf(1 - value) : String.valueOf(value));
                }
            }
        }
        List<List<String>> checkingSDNF = minimizeFormuleByMingling(minimizedSdnfCopy);
        return checkingSDNF.stream().anyMatch(innerList -> innerList.size() == 1 && innerList.get(0).equals("1"));
    }

    private List<List<String>> findRedundantSKNFByCalculation(List<List<String>> parsedSKNF, List<List<String>> minimizedSknf) {
        List<List<String>> result = new ArrayList<>(minimizedSknf);
        for (int i = 0; i < minimizedSknf.size(); i++) {
            List<String> currentImplicant = minimizedSknf.get(i);
            Map<String, Integer> assignment = getAssignmentForSKNFImplicant(currentImplicant);
            if (assignment == null) continue;
            boolean isRedundant = checkIfRedundantSKNF(currentImplicant, minimizedSknf, assignment);
            if (isRedundant) {
                result.remove(currentImplicant);
            }
        }
        return result;
    }

    Map<String, Integer> getAssignmentForSKNFImplicant(List<String> implicant) {
        Map<String, Integer> assignment = new HashMap<>();
        for (String literal : implicant) {
            if (literal.startsWith("!")) {
                String var = literal.substring(1);
                assignment.put(var, 1);
            } else {
                assignment.put(literal, 0);
            }
        }
        return assignment;
    }

    boolean checkIfRedundantSKNF(List<String> implicant, List<List<String>> minimizedSknf, Map<String, Integer> assignment) {
        List<List<String>> minimizedSknfCopy = new ArrayList<>();
        for (List<String> imp : minimizedSknf) {
            if (!imp.equals(implicant)) {
                minimizedSknfCopy.add(new ArrayList<>(imp));
            }
        }

        for (List<String> imp : minimizedSknfCopy) {
            for (int i = 0; i < imp.size(); i++) {
                String literal = imp.get(i);
                String var = literal.startsWith("!") ? literal.substring(1) : literal;
                if (assignment.containsKey(var)) {
                    int value = assignment.get(var);
                    imp.set(i, literal.startsWith("!") ? String.valueOf(1 - value) : String.valueOf(value));
                }
            }
        }
        List<List<String>> checkingSKNF = minimizeFormuleByMingling(minimizedSknfCopy);
        return checkingSKNF.stream().anyMatch(innerList -> innerList.size() == 1 && innerList.get(0).equals("0"));
    }

    List<List<String>> findRedundantByTable(List<List<String>> parsedFormulas, List<List<String>> minimizedFormulas) {
        List<List<String>> resultFormulas = new ArrayList<>();
        int[] amountOfInclusions = new int[parsedFormulas.size()];
        int index = 0;
        for (List<String> constituent : parsedFormulas) {
            for (List<String> implicant : minimizedFormulas) {
                if (implicant.stream().allMatch(constituent::contains)) {
                    amountOfInclusions[index]++;
                }
            }
            index++;
        }
        int minInclusions = Arrays.stream(amountOfInclusions).min().orElse(0);
        for (int i = 0; i < parsedFormulas.size(); i++) {
            if (amountOfInclusions[i] == minInclusions) {
                for (List<String> implicant : minimizedFormulas) {
                    if (implicant.stream().allMatch(parsedFormulas.get(i)::contains)) {
                        resultFormulas.add(implicant);
                    }
                }
            }
        }
        return ListUtils.distinctValue(resultFormulas);
    }

    String returnReversedElement(String element) {
        return element.startsWith("!") ? element.substring(1) : "!" + element;
    }

    String buildDNFString(List<List<String>> terms) {
        List<String> disjunctions = new ArrayList<>();
        for (List<String> term : terms) {
            if (!term.isEmpty()) {
                disjunctions.add("(" + String.join("/\\", term) + ")");
            }
        }
        return disjunctions.isEmpty() ? "0" : String.join("\\/", disjunctions);
    }

    String buildCNFString(List<List<String>> terms) {
        List<String> conjunctions = new ArrayList<>();
        for (List<String> term : terms) {
            if (!term.isEmpty()) {
                conjunctions.add("(" + String.join("\\/", term) + ")");
            }
        }
        return conjunctions.isEmpty() ? "1" : String.join("/\\", conjunctions);
    }
}