package by.losik;

import by.losik.lab2.TruthTableGenerator;

import java.util.Scanner;

public class LogicMinimizerApp {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Введите логическое выражение (используй &, |, !, ->, ~. ∨ - |, ∧ - &): ");
        String expression = scanner.nextLine().replaceAll("\\s+", "");

        TruthTableGenerator tableGenerator = new TruthTableGenerator();
        tableGenerator.injectExpression(expression);

        TruthTableGenerator.extractVariables();
        TruthTableGenerator.generateTruthTable();
        TruthTableGenerator.evaluateExpression();
        TruthTableGenerator.printTruthTable();

        String sdnf = TruthTableGenerator.buildSDNF();
        String sknf = TruthTableGenerator.buildSKNF();

        System.out.println("\nСДНФ: " + sdnf);
        System.out.println("СКНФ: " + sknf);

        Minimizer minimizer = new Minimizer();

        KarnaughMinimizer karnaughMinimizer = new KarnaughMinimizer();

        IOSystem.printSDNFCalculatingMethod(minimizer.minimizeSDNFByCalculating(sdnf));
        IOSystem.printSDNFTableCalculatingMethod(minimizer.minimizeSDNFByTableAndCalculating(sdnf));
        IOSystem.printSDNFTableMethod(karnaughMinimizer.minimizeSDNFByKarnaugh(sdnf));


        IOSystem.printSKNFCalculatingMethod(minimizer.minimizeSKNFByCalculating(sknf));
        IOSystem.printSKNFTableCalculatingMethod(minimizer.minimizeSKNFByTableAndCalculating(sknf));
        IOSystem.printSKNFTableMethod(karnaughMinimizer.minimizeSKNFByKarnaugh(sknf));

    }
}
