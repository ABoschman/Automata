/*
 * Fontys Hogescholen ICT - Software Development
 * Modelling with Automata in Professional Practice
 * Project developer: Arjan Boschman
 * Startdate: October 2014
 */
package domain;

/**
 *
 * @author Arjan
 */
public enum AutomatonType {

    DFA("Deterministic Finite Automaton", true, false, false),
    NFA("Nondeterministic Finite Automaton", false, false, false),
    DPDA("Deterministic Push Down Automaton", true, false, true),
    NPDA("Nondeterministic Push Down Automaton", false, false, true),
    DIOA("Deterministic IO Automaton", true, true, false),
    NIOA("Nondeterministic IO Automaton", false, true, false);

    private final String description;
    private final boolean isDeterministic;
    private final boolean performsIOOperation;
    private final boolean performsStackOperation;

    public String getDescription() {
        return description;
    }

    public boolean isDeterministic() {
        return isDeterministic;
    }

    public boolean performsIOOperation() {
        return performsIOOperation;
    }

    public boolean performsStackOperation() {
        return performsStackOperation;
    }

    private AutomatonType(String description, boolean isDeterministic, boolean performsIOOperation, boolean performsStackOperation) {
        this.description = description;
        this.isDeterministic = isDeterministic;
        this.performsIOOperation = performsIOOperation;
        this.performsStackOperation = performsStackOperation;
    }

}
