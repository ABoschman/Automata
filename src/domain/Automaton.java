/*
 * Fontys Hogescholen ICT - Software Development
 * Modelling with Automata in Professional Practice
 * Project developer: Arjan Boschman
 * Startdate: October 2014
 */
package domain;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;

/**
 *
 * @author Arjan
 */
public class Automaton {

    private String name;
    private final ObjectProperty<AutomatonType> typeProperty
            = new SimpleObjectProperty<>();
    private final State startState;
    private final Set<State> states;
    private final ObservableList<Transition> transitions = FXCollections.observableArrayList();
    private final ObservableList<State> terminators = FXCollections.observableArrayList();

    /**
     * Construct a new Automaton. This always creates an Automaton with start
     * state A.
     *
     * @param name The name of the automaton.
     */
    public Automaton(String name) {
        this(name, new State("A"));
    }

    /**
     * Construct a new Automaton.
     *
     * @param name The name of the automaton.
     * @param startState The startstate of the Automaton.
     */
    public Automaton(String name, State startState) {
        this.name = name;
        this.startState = startState;
        this.states = new HashSet<>();
        this.states.add(startState);
        setTypeProperty(AutomatonType.DFA);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ObjectProperty<AutomatonType> getTypeProperty() {
        return typeProperty;
    }

    private void setTypeProperty(AutomatonType type) {
        this.typeProperty.set(type);
    }

    public State getStartState() {
        return startState;
    }

    public Iterator<State> getStates() {
        return states.iterator();
    }

    public boolean containsState(String name) {
        return states.stream().anyMatch(state -> state.getName().equals(name));
    }

    /**
     * Recalculates the {@link AutomatonType} of the Automaton. And saves the
     * result in its internal state.
     */
    private void recalculateType() {
        final boolean isNondeterministic = states.stream().anyMatch(state -> state.hasNonDeterminism());
        final boolean isPushdown = states.stream().anyMatch(state -> state.hasStackTransitions());
        if (isNondeterministic && isPushdown) {
            setTypeProperty(AutomatonType.NPDA);
        } else if (isPushdown) {
            setTypeProperty(AutomatonType.DPDA);
        } else {
            final boolean isIO = states.stream().anyMatch(state -> state.hasIOTransitions());
            if (isNondeterministic && isIO) {
                setTypeProperty(AutomatonType.NIOA);
            } else if (isIO) {
                setTypeProperty(AutomatonType.DIOA);
            } else if (isNondeterministic) {
                setTypeProperty(AutomatonType.NFA);
            } else {
                setTypeProperty(AutomatonType.DFA);
            }
        }
    }

    public void addTransition(char startStateName, char endStateName) {
        addTransition(startStateName, endStateName, Transition.EPSILON);
    }

    public void addTransition(char startStateName, char endStateName, char inputCharacter) {
        addTransition(String.valueOf(startStateName), String.valueOf(endStateName),
                String.valueOf(inputCharacter), Transition.EPSILON, Transition.EPSILON);
    }

    /**
     * Constructs a new Transition and adds it to the Automaton.
     *
     * @param sourceStateName The source state of the transition.
     * @param targetStateName The target state of the transition.
     * @param input The input that is consumed.
     * @param readFromStack The value that is popped from the stack.
     * @param writeToStack The value that is written to the stack.
     * @deprecated This method is deprecated because the object instantiation
     * code must be moved to a factory class.
     */
    @Deprecated
    public void addTransition(String sourceStateName, String targetStateName, String input,
            char readFromStack, char writeToStack) {
        final State targetState = addState(targetStateName);
        final State sourceState = addState(sourceStateName);
        final Transition transition = new Transition(sourceState, targetState, input, readFromStack, writeToStack);
        addTransition(transition);
    }

    public void addTransition(Transition transition) {
        if (transitions.contains(transition)) {
            return;
        }
        transitions.add(transition);
        transition.getSourceState().addTransition(transition);
        recalculateType();
    }

    public void removeTransition(Transition transition) {
        states.stream().
                filter(state -> state.equals(transition.getSourceState())).
                findFirst().get().removeTransition(transition);
        transitions.remove(transition);
        recalculateType();
    }

    public boolean terminatesAt(State state) {
        return terminators.contains(state);
    }

    public void addEndState(char endStateName) {
        addEndState(String.valueOf(endStateName));
    }

    public void addEndState(String endStateName) {
        final State terminator = addState(endStateName);
        terminators.add(terminator);
        terminator.setIsTerminator(true);
    }

    public void removeEndState(String endStateName) {
        removeEndState(addState(endStateName));
    }

    public void removeEndState(State state) {
        terminators.remove(state);
        state.setIsTerminator(false);
    }

    private State addState(String statename) {
        final State newState = states.stream().
                filter(state -> state.getName().equals(statename)).
                findFirst().orElseGet(() -> new State(statename));
        states.add(newState);
        return newState;
    }

    public void addTransitionsObserver(ListView<Transition> lvTransitions) {
        lvTransitions.setItems(transitions);
    }

    public void addTerminatorsObserver(ListView<State> lvTerminators) {
        lvTerminators.setItems(terminators);
    }

    /**
     * Parses a certain input string and checks if it occurs within the language
     * accepted by the automaton.
     *
     * @param input The word/input string to be checked.
     * @return True if the input is accepted.
     */
    public boolean parseInput(String input) {
        Set<Configuration> currentConfigs = new HashSet<>();
        currentConfigs.add(new Configuration(getStartState(), "", input));
        includeEpsilonClosure(currentConfigs);

        for (int i = 0; i < input.length(); i++) {
            currentConfigs = consumeNextInputSymbol(currentConfigs);
            includeEpsilonClosure(currentConfigs);
        }
        //todo state.terminates low prio
        return currentConfigs.stream().anyMatch((config) -> (terminatesAt(config.getState())));
    }

    /**
     * Expands the Set of given Configurations with their epsilon closure.
     *
     * @param configs The Set<Configuration> that is to be expanded with the
     * epsilon closure, consisting of zero or more elements.
     */
    private void includeEpsilonClosure(Set<Configuration> configs) {
        while (true) {
            final Set<Configuration> nextConfigs = new HashSet<>();
            configs.stream().forEach((config) -> nextConfigs.addAll(config.calculateEpsilonClosure()));
            if (!configs.addAll(nextConfigs)) {
                return;
            }
        }
    }

    /**
     * Calculates the Set of Configurations obtained by making each of the given
     * configurations consume its next input symbol. Doesn't include epsilon
     * closure.
     *
     * @param configs The given starting configurations. Will not be modified.
     * @return The Set of Configurations reachable by consuming the next input
     * symbol.
     */
    private Set<Configuration> consumeNextInputSymbol(Set<Configuration> configs) {
        final Set<Configuration> nextConfigs = new HashSet<>();
        configs.stream().forEach((config) -> nextConfigs.addAll(config.consumeOneSymbol()));
        return nextConfigs;
    }

    @Override
    public String toString() {
        return name;
    }

}
