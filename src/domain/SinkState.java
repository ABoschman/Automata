/*
 * Fontys Hogescholen ICT - Software Development
 * Modelling with Automata in Professional Practice
 * Project developer: Arjan Boschman
 * Startdate: October 2014
 */
package domain;

/**
 * Sink state. Represents the contingency where the Automaton has ended up in a
 * part of its execution where there is no way to get back to a state with IO
 * transitions anymore. Sink states are defined by two rules. 1: They don't have
 * IO-operations. 2: Their Transitions, if there are any, all lead to other sink
 * states.
 *
 * @author Arjan
 */
public class SinkState extends State {

    /**
     * Constructs a Sink State. Has one epsilon transition that leads nowhere,
     * purely for the algorithm to determine a possible Deadlock.
     */
    public SinkState() {
        super("Sink", new Transition(null, null, Transition.EPSILON, Transition.EPSILON, Transition.EPSILON));
    }

}
