package by.losik;

import java.util.*;
import java.util.stream.Collectors;

public class AutomataSynthesizer {

    public static void main(String[] args) {
        // Пример: синтез 3-битного счетчика с T-триггерами
        List<State> states = Arrays.asList(
                new State("S0", 0, 0, 0),
                new State("S1", 0, 0, 1),
                new State("S2", 0, 1, 0),
                new State("S3", 0, 1, 1),
                new State("S4", 1, 0, 0),
                new State("S5", 1, 0, 1),
                new State("S6", 1, 1, 0),
                new State("S7", 1, 1, 1)
        );

        List<Transition> transitions = new ArrayList<>();
        for (int i = 0; i < states.size(); i++) {
            transitions.add(new Transition(
                    states.get(i),
                    states.get((i + 1) % states.size()),
                    new int[]{1} // вход "счет"
            ));
        }

        transitions.add(new Transition(states.get(1), states.get(0), new int[]{0}));
        transitions.add(new Transition(states.get(3), states.get(0), new int[]{0}));
        transitions.add(new Transition(states.get(5), states.get(0), new int[]{0}));
        transitions.add(new Transition(states.get(7), states.get(0), new int[]{0}));

        AutomataSynthesizer synthesizer = new AutomataSynthesizer(
                states, transitions, TriggerType.T, LogicBasis.NAND_NOR, 1);

        synthesizer.synthesize();
    }

    public enum TriggerType { T, D, JK, RS }
    public enum LogicBasis { AND_OR_NOT, NAND, NOR, NAND_NOR }

    public static class State {
        final String name;
        private final int[] code;

        public State(String name, int... code) {
            this.name = name;
            this.code = code;
        }

        public int[] getCode() { return code.clone(); }
        public int getCodeLength() { return code.length; }

        @Override
        public String toString() {
            return name + Arrays.toString(code);
        }
    }

    public static class Transition {
        private final State currentState;
        private final State nextState;
        private final int[] inputConditions;

        public Transition(State currentState, State nextState, int[] inputConditions) {
            this.currentState = currentState;
            this.nextState = nextState;
            this.inputConditions = inputConditions;
        }

        public State getCurrentState() { return currentState; }
        public State getNextState() { return nextState; }
        public int[] getInputConditions() { return inputConditions.clone(); }

        @Override
        public String toString() {
            return currentState + " -> " + nextState +
                    (inputConditions.length > 0 ? " при " + Arrays.toString(inputConditions) : "");
        }
    }

    private final List<State> states;
    private final List<Transition> transitions;
    private final TriggerType triggerType;
    private final LogicBasis logicBasis;
    private final int inputCount;

    public AutomataSynthesizer(List<State> states, List<Transition> transitions,
                               TriggerType triggerType, LogicBasis logicBasis, int inputCount) {
        this.states = new ArrayList<>(states);
        this.transitions = new ArrayList<>(transitions);
        this.triggerType = triggerType;
        this.logicBasis = logicBasis;
        this.inputCount = inputCount;
    }

    public void synthesize() {
        System.out.println("=== Синтез цифрового автомата ===");
        System.out.println("Тип триггеров: " + triggerType);
        System.out.println("Логический базис: " + logicBasis);
        System.out.println("Количество входов: " + inputCount);
        System.out.println("Количество состояний: " + states.size());

        Map<State, Map<String, State>> transitionTable = buildTransitionTable();
        printTransitionTable(transitionTable);

        Map<Integer, List<Integer>> excitationFunctions = calculateExcitationFunctions(transitionTable);
        printExcitationFunctions(excitationFunctions);

        for (int i = 0; i < states.get(0).getCodeLength(); i++) {
            List<Integer> minterms = excitationFunctions.get(i);
            if (minterms != null && !minterms.isEmpty()) {
                System.out.println("\nМинимизация функции для T" + i + ":");
                String minimized = quineMcCluskey(minterms, states.get(0).getCodeLength() + inputCount);
                System.out.println("T" + i + " = " + minimized);
            }
        }

        implementInLogicBasis();
    }

    Map<State, Map<String, State>> buildTransitionTable() {
        Map<State, Map<String, State>> table = new HashMap<>();

        for (Transition t : transitions) {
            String inputKey = Arrays.toString(t.getInputConditions());
            table.computeIfAbsent(t.getCurrentState(), k -> new HashMap<>())
                    .put(inputKey, t.getNextState());
        }

        return table;
    }

    private void printTransitionTable(Map<State, Map<String, State>> table) {
        System.out.println("\nТаблица переходов:");
        System.out.println("Текущее состояние | Входы | Следующее состояние");
        System.out.println("--------------------------------------------");

        for (State state : table.keySet()) {
            Map<String, State> transitions = table.get(state);
            for (Map.Entry<String, State> entry : transitions.entrySet()) {
                System.out.printf("%-15s | %-5s | %-15s%n",
                        state, entry.getKey(), entry.getValue());
            }
        }
    }

    Map<Integer, List<Integer>> calculateExcitationFunctions(Map<State, Map<String, State>> transitionTable) {
        Map<Integer, List<Integer>> functions = new HashMap<>();
        int numTriggers = states.get(0).getCodeLength();

        for (State current : transitionTable.keySet()) {
            for (Map.Entry<String, State> entry : transitionTable.get(current).entrySet()) {
                State next = entry.getValue();
                int[] inputs = parseInputs(entry.getKey());

                // Создаем комбинированный код (состояние + входы)
                int[] combined = new int[current.getCodeLength() + inputs.length];
                System.arraycopy(current.getCode(), 0, combined, 0, current.getCodeLength());
                System.arraycopy(inputs, 0, combined, current.getCodeLength(), inputs.length);

                int combinedCode = binaryToDecimal(combined);

                for (int i = 0; i < numTriggers; i++) {
                    int excitation = calculateExcitation(
                            current.getCode()[i], next.getCode()[i], triggerType);

                    if (excitation == 1) {
                        functions.computeIfAbsent(i, k -> new ArrayList<>()).add(combinedCode);
                    }
                }
            }
        }

        return functions;
    }

    private int calculateExcitation(int current, int next, TriggerType type) {
        switch (type) {
            case T: return current ^ next;
            case D: return next;
            case JK:
            case RS:
                if (current == 0 && next == 0) return 0;
                if (current == 0 && next == 1) return 1;
                if (current == 1 && next == 0) return 1;
                return 0;
            default: return 0;
        }
    }

    void printExcitationFunctions(Map<Integer, List<Integer>> functions) {
        System.out.println("\nФункции возбуждения:");
        for (Map.Entry<Integer, List<Integer>> entry : functions.entrySet()) {
            System.out.println("T" + entry.getKey() + " = Σ" + entry.getValue());
        }
    }

    String quineMcCluskey(List<Integer> minterms, int numVars) {
        List<Implicant> primeImplicants = findPrimeImplicants(minterms, numVars);
        List<Implicant> essentialImplicants = findEssentialImplicants(primeImplicants, minterms);

        // Упрощенное представление результата
        if (essentialImplicants.isEmpty()) return "0";
        if (essentialImplicants.size() == 1 && essentialImplicants.get(0).mask == (1 << numVars) - 1)
            return "1";

        return essentialImplicants.stream()
                .map(imp -> implicantToString(imp, numVars))
                .collect(Collectors.joining(" + "));
    }

    List<Implicant> findPrimeImplicants(List<Integer> minterms, int numVars) {
        List<Implicant> current = minterms.stream()
                .map(m -> new Implicant(m, 0, m))
                .collect(Collectors.toList());

        List<Implicant> primes = new ArrayList<>();

        while (!current.isEmpty()) {
            List<Implicant> next = new ArrayList<>();
            Set<Integer> matched = new HashSet<>();

            for (int i = 0; i < current.size(); i++) {
                for (int j = i + 1; j < current.size(); j++) {
                    Implicant a = current.get(i);
                    Implicant b = current.get(j);

                    if (a.mask == b.mask && Integer.bitCount(a.value ^ b.value) == 1) {
                        int newMask = a.mask | (a.value ^ b.value);
                        next.add(new Implicant(a.value, newMask, a.minterms | b.minterms));
                        matched.add(i);
                        matched.add(j);
                    }
                }
            }

            for (int i = 0; i < current.size(); i++) {
                if (!matched.contains(i)) {
                    primes.add(current.get(i));
                }
            }

            current = next;
        }

        return primes;
    }

    List<Implicant> findEssentialImplicants(List<Implicant> primes, List<Integer> minterms) {
        Map<Integer, List<Implicant>> coverage = new HashMap<>();
        for (Integer m : minterms) {
            coverage.put(m, new ArrayList<>());
        }

        for (Implicant prime : primes) {
            for (Integer m : minterms) {
                if ((prime.minterms & (1 << m)) != 0) {
                    coverage.get(m).add(prime);
                }
            }
        }

        List<Implicant> essential = new ArrayList<>();
        Set<Integer> covered = new HashSet<>();

        for (Map.Entry<Integer, List<Implicant>> entry : coverage.entrySet()) {
            if (entry.getValue().size() == 1) {
                Implicant essentialImp = entry.getValue().get(0);
                if (!essential.contains(essentialImp)) {
                    essential.add(essentialImp);
                    for (Integer m : minterms) {
                        if ((essentialImp.minterms & (1 << m)) != 0) {
                            covered.add(m);
                        }
                    }
                }
            }
        }

        if (covered.size() < minterms.size()) {
            for (Implicant prime : primes) {
                if (!essential.contains(prime)) {
                    boolean needed = false;
                    for (Integer m : minterms) {
                        if (!covered.contains(m) && (prime.minterms & (1 << m)) != 0) {
                            needed = true;
                            covered.add(m);
                        }
                    }
                    if (needed) {
                        essential.add(prime);
                    }
                }
            }
        }

        return essential;
    }

    String implicantToString(Implicant imp, int numVars) {
        StringBuilder sb = new StringBuilder();
        for (int i = numVars - 1; i >= 0; i--) {
            if ((imp.mask & (1 << i)) == 0) {
                sb.append(((imp.value >> i) & 1) == 1 ? "x" + i : "¬x" + i);
            }
        }
        return sb.toString();
    }

    static class Implicant {
        int value;
        int mask;
        int minterms;

        Implicant(int value, int mask, int minterms) {
            this.value = value;
            this.mask = mask;
            this.minterms = minterms;
        }
    }

    private int[] parseInputs(String inputStr) {
        String[] parts = inputStr.replaceAll("[\\[\\]]", "").split(", ");
        int[] inputs = new int[parts.length];
        for (int i = 0; i < parts.length; i++) {
            inputs[i] = Integer.parseInt(parts[i]);
        }
        return inputs;
    }

    int binaryToDecimal(int[] binary) {
        int result = 0;
        for (int j : binary) {
            result = (result << 1) | j;
        }
        return result;
    }

    void implementInLogicBasis() {
        System.out.println("\nРеализация в базисе " + logicBasis + ":");

        switch (logicBasis) {
            case AND_OR_NOT -> System.out.println("Использование базовых элементов И, ИЛИ, НЕ");
            case NAND -> {
                System.out.println("Все функции реализуются через элементы И-НЕ");
                System.out.println("Правила преобразования:");
                System.out.println("И(a,b) = НЕ(И-НЕ(a,b))");
                System.out.println("ИЛИ(a,b) = И-НЕ(НЕ(a), НЕ(b))");
            }
            case NOR -> {
                System.out.println("Все функции реализуются через элементы ИЛИ-НЕ");
                System.out.println("Правила преобразования:");
                System.out.println("ИЛИ(a,b) = НЕ(ИЛИ-НЕ(a,b))");
                System.out.println("И(a,b) = ИЛИ-НЕ(НЕ(a), НЕ(b))");
            }
            case NAND_NOR -> {
                System.out.println("Комбинация элементов И-НЕ и ИЛИ-НЕ");
                System.out.println("Выбор элементов зависит от конкретной функции");
            }
        }
    }
}