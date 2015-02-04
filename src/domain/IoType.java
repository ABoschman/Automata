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
public enum IoType {

    /**
     * USed to denote that a certain State has IO-Operations or can reach
     * another State through non-blocking Transitions that does.
     */
    HAS_IO,
    /**
     * Used to denote that a certain State has no direct IO-Operations but might
     * be able to reach States through non-blocking Transitions that do.
     */
    MAYBE_IO,
    /**
     * Used to denote that a certain State is currently recursively calculating
     * whether it can reach a State that has IO-Operations.
     *
     */
    STILL_CALC,
    /**
     * Denotes that a certain State is a sink state. This means that it doesn't
     * have IO-Operations and its Transitions, if there are any, all lead to
     * other sink states.
     */
    IS_SINK;

}
