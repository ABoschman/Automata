/*
 * Fontys Hogescholen ICT - Software Development
 * Modelling with Automata in Professional Practice
 * Project developer: Arjan Boschman
 * Startdate: October 2014
 */
package domain;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * A class that considers two Automata and is capable of running a static
 * analysis on them to discover paths through the automata that deadlock.
 *
 * @author Arjan
 */
public class DeadlockFinder {

    private final Automaton process1;
    private final Automaton process2;
    private final UniqueIdIssuer idIssuer = new UniqueIdIssuer();
    private final Map<Integer, Set<Contingency>> contingenciesMap = new HashMap<>();
    private final Set<StatePair> resolvedPairs = new HashSet<>();
    private String log = "";

    /**
     * Constructs a new DeadlockFinder. Usable by calling {@link #hasDeadlock}.
     * Calling hasDeadlock() more than once has undefined behaviour.
     *
     * @param process1 One of the automaton tracked.
     * @param process2 The other automaton.
     */
    public DeadlockFinder(Automaton process1, Automaton process2) {
        this.process1 = process1;
        this.process2 = process2;
    }

    public String getLog() {
        return log;
    }

    /**
     * Search the automaton pair for potential deadlock situations. This method
     * can only be called once in the lifetime of this object.
     *
     * @return True if a possibility for deadlock is detected.
     */
    public boolean hasDeadlock() throws IllegalStateException {
        if (idIssuer.hasIssuedIds()) {
            throw new IllegalStateException("This DeadlockDetector has already "
                    + "been used and is now closed.");
        }
        setupContingencies(idIssuer.getUniqueId());
        return checkForDeadlocks();
    }

    /**
     * Executes the actual algorithm for finding deadlocks. During the execution
     * of this method, data will be added to the contingencies map and the
     * resolvedPairs set.
     *
     * @return Immediately returns true if a deadlocking situation is detected.
     * Returns false if all possible paths through both automata have been
     * considered but no possible deadlocks were found.
     */
    private boolean checkForDeadlocks() {
        int counter = 0;
        while (contingenciesMap.containsKey(counter)) {
            final Set<StatePair> unresolvedPairs = getAllPairs(contingenciesMap.get(counter));
            if (resolvePairs(unresolvedPairs)) {
                counter++;
            } else {
                return true;
            }
        }
        return false;
    }

    /**
     * For a set of all contingencies in a certain time slot, return all as of
     * yet unresolved StatePairs.
     *
     * @param contingencies The complete set of contingencies from both automata
     * concerning a certain time slot.
     * @return The StatePairs following from the given contingencies, minus the
     * ones that have already been considered.
     */
    private Set<StatePair> getAllPairs(Set<Contingency> contingencies) {
        final Set<StatePair> pairs = new HashSet<>();
        contingencies.stream()
                .filter((cont) -> cont.getProcessId() == 1)
                .forEach((cont1) -> {
                    contingencies.stream()
                    .filter((cont) -> cont.getProcessId() == 2)
                    .forEach((cont2) -> {
                        final StatePair pair = makeStatePair(cont1, cont2);
                        if (!resolvedPairs.contains(pair)) {
                            pairs.add(pair);
                        }
                    });
                });
        return pairs;
    }

    private StatePair makeStatePair(Contingency con1, Contingency con2) {
        return new StatePair(con1.getState(), con2.getState());
    }

    /**
     * Attempt to resolve all pairs. If one of the pairs cannot be resolved,
     * return false. Adds sets of contingencies to map.
     *
     * @param unresolvedPairs The StatePairs that are to be resolved into zero
     * or more sets of contingencies.
     * @return True if all pairs could be resolved. False if one of them
     * deadlocks.
     */
    private boolean resolvePairs(Set<StatePair> unresolvedPairs) {
        return unresolvedPairs.stream()
                .allMatch((pair) -> resolvePair(pair));
    }

    /**
     * Check if the Automata cn advance past the StatePair or if there is a dead
     * lock situation. If the pair deadlocks, method returns false. Otherwise,
     * follows through all the IO operations, if there are any, and gathers sets
     * of contingencies to add to the contingenciesMap.
     *
     * @param unresolvedPair A point in both Automata's runtimes where they have
     * IO transitions. At these points a deadlock might be detected.
     * @return True if the provided StatePair could be resolved, false if a
     * deadlock was detected.
     */
    private boolean resolvePair(StatePair unresolvedPair) {
        if (unresolvedPair.deadlocks()) {
            log = "Deadlocks at: " + unresolvedPair.toString();
            return false;
        }
        final Set<StatePair> paths = unresolvedPair.doIOOperations();
        paths.stream().forEach((statepair) -> {
            final int time = idIssuer.getUniqueId();
            contingenciesMap.put(time, statepair.gatherContingencies(time));
        });
        resolvedPairs.add(unresolvedPair);
        return true;
    }
    
    /**
     * Sets up the contingencies map with all the contingencies possible at time
     * zero.
     */
    private void setupContingencies(int time) {
        contingenciesMap.put(time, getInitialContingencies(time));
    }

    /**
     * At the start of execution, Automata 1 and 2 are in a StatePair with their
     * starting states. This method returns all Contingencies reachable from the
     * starting time.
     *
     * @param time The starting time.
     * @return The Set of contingencies reachable from the starting states.
     */
    private Set<Contingency> getInitialContingencies(int time) {
        final StatePair startStatePair = new StatePair(process1.getStartState(), process2.getStartState());
        return startStatePair.gatherContingencies(time);
    }
}
