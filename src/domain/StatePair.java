/*
 * Fontys Hogescholen ICT - Software Development
 * Modelling with Automata in Professional Practice
 * Project developer: Arjan Boschman
 * Startdate: October 2014
 */
package domain;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 *
 * @author Arjan
 */
public class StatePair {

    private final State state1;
    private final State state2;

    /**
     * A pair of two States from different Automata.
     *
     * @param state1
     * @param state2
     */
    public StatePair(State state1, State state2) {
        this.state1 = state1;
        this.state2 = state2;
    }

    /**
     * Checks if every single transition in state1 is a mismatch with every
     * single Transition in state2.
     *
     * @return True if this StatePair deadlocks.
     */
    public boolean deadlocks() {
        return state1.getTransitionsStream()
                .allMatch((Transition trans1) -> {
                    return state2.getTransitionsStream()
                    .allMatch((Transition trans2) -> {
                        return !trans1.isCompatibleWith(trans2);
                    });
                });
    }

    /**
     * The Set of Contingencies reachable in one time period by both Automata,
     * provided the Automata start out in state1 and state2 respectively.
     *
     * @param time The time slot these Contingencies are reached at.
     * @return The Set of Contingencies.
     */
    public Set<Contingency> gatherContingencies(int time) {
        final Set<Contingency> contingencies = new HashSet<>();
        final Set<State> ioStates1 = state1.collectContingencies();
        ioStates1.stream().forEach((state) -> contingencies.add(new Contingency(1, state, time)));
        
        final Set<State> ioStates2 = state2.collectContingencies();
        ioStates2.stream().forEach((state) -> contingencies.add(new Contingency(2, state, time)));
        
        return contingencies;
    }
    
    /**
     * Of the Cartesian product of transitions of state1 and state2, filter all
     * matching IO transitions. Gather the resulting StatePairs.
     *
     * @return The StatePairs that could be reached after going through an IO
     * transaction.
     */
    public Set<StatePair> doIOOperations() {
        final Set<StatePair> pairs = new HashSet<>();
        state1.getTransitionsStream()
                .filter((trans1) -> trans1.performsIOOperation())
                .forEach((trans1) -> {
                    state2.getTransitionsStream()
                    .filter((trans2) -> trans1.isCompatibleWith(trans2))
                    .forEach((trans2) -> {
                        pairs.add(new StatePair(trans1.getTargetState(), trans2.getTargetState()));
                    });
                });
        return pairs;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 71 * hash + Objects.hashCode(this.state1);
        hash = 71 * hash + Objects.hashCode(this.state2);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final StatePair other = (StatePair) obj;
        if (!Objects.equals(this.state1, other.state1)) {
            return false;
        }
        if (!Objects.equals(this.state2, other.state2)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "StatePair{" + "state1=" + state1 + ", state2=" + state2 + '}';
    }
    
}
