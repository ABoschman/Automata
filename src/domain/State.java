/*
 * Fontys Hogescholen ICT - Software Development
 * Modelling with Automata in Professional Practice
 * Project developer: Arjan Boschman
 * Startdate: October 2014
 */
package domain;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

/**
 *
 * @author Arjan
 */
public class State {

    private final String name;
    private final Set<Transition> transitions = new HashSet<>();
    private boolean isTerminator = false;

    public State(String name) {
        this.name = name;
    }

    public State(String name, Transition... transitions) {
        this.name = name;
        this.transitions.addAll(Arrays.asList(transitions));
    }

    public boolean isTerminator() {
        return isTerminator;
    }

    public void setIsTerminator(boolean isTerminator) {
        this.isTerminator = isTerminator;
    }

    public String getName() {
        return name;
    }

    public void addTransition(Transition transition) {
        transitions.add(transition);
    }

    public Iterator<Transition> getTransitions() {
        return transitions.iterator();
    }

    public Stream<Transition> getTransitionsStream() {
        return transitions.stream();
    }

    @Override
    public String toString() {
        return name;
    }

    public void removeTransition(Transition transition) {
        transitions.remove(transition);
    }

    public boolean hasTransition(Transition transition) {
        return transitions.contains(transition);
    }

    public boolean containsTransition(final char readOrWrite, final int channelNr) {
        return transitions.stream().anyMatch(trans -> {
            return trans.canCommunicateWith(readOrWrite, channelNr);
        });
    }

    public boolean hasIOTransitions() {
        return transitions.stream().anyMatch(trans -> trans.performsIOOperation());
    }

    public boolean hasStackTransitions() {
        return transitions.stream().anyMatch(trans -> trans.performsStackOperation());
    }

    public boolean hasNonDeterminism() {
        return transitions.stream().anyMatch((Transition trans) -> {
            if (trans.takesNoInput()) {
                return true;
            }
            return transitions.stream().filter((Transition otherI) -> {
                return otherI.getInput().equals(trans.getInput())
                        && !otherI.equals(trans);
            }).anyMatch((Transition otherII) -> {
                return otherII.getReadFromStack() == trans.getReadFromStack()
                        || !otherII.readsFromStack() || !trans.readsFromStack();
            });
        });
    }

    /**
     * Collect all states, reachable from this state without going through IO
     * transitions, that have at least one IO transition.
     *
     * @return The Set of States that were found.
     */
    public Set<State> collectContingencies() {
        final Set<State> result = new HashSet<>();
        collectContingencies(new HashMap<>(), result, new HashSet<>());
        return result;
    }

    /**
     * Recursive method, only to be called from within the recursion. To start
     * the search, use {@link
     * #collectContingencies()}
     *
     * @param visited A Map of States that are already considered.
     * @param result The Set of States that has been found.
     * @param myDependencies Used for determining if this State is a sink
     * contingency.
     * @return
     */
    IoType collectContingencies(
            Map<State, IoType> visited, Set<State> result, Set<State> dependencies) {
        if (visited.containsKey(this)) {
            return visited.get(this);
        } else if (hasIOTransitions()) {
            result.add(this);
            visited.put(this, IoType.HAS_IO);
            collectContingenciesIgnoreDependencies(visited, result);
            return IoType.HAS_IO;
        } else {
            visited.put(this, IoType.STILL_CALC);
            return collectContingenciesMindDependencies(visited, result, dependencies);
        }
    }
    
    private void collectContingenciesIgnoreDependencies(Map<State, IoType> visited, Set<State> result) {
        transitions.stream()
                .filter((trans) -> !trans.performsIOOperation())
                .forEach((nonIoTrans) -> {
                    nonIoTrans.findContinThroughTrans(visited, result, new HashSet<>());
                });
    }

    private IoType collectContingenciesMindDependencies(Map<State, IoType> visited, Set<State> result, Set<State> dependencies) {
        final Set<State> myDependencies = new HashSet<>();
        transitions.stream()
                .filter((trans) -> !trans.performsIOOperation())
                .forEach((nonIoTrans) -> {
                    nonIoTrans.findContinThroughTrans(visited, result, myDependencies);
                });
        if(visited.get(this).equals(IoType.HAS_IO)) {
            return IoType.HAS_IO;
        }
        myDependencies.remove(this);
        if (myDependencies.isEmpty()) {
            result.add(new SinkState());
            return IoType.IS_SINK;
        } else {
            dependencies.addAll(myDependencies);
            return IoType.MAYBE_IO;
        }
    }

}
