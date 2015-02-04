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
public class Contingency {

    private final int processId;
    private final State state;
    private final int timeId;

    public Contingency(int processId, State state, int timeId) {
        this.processId = processId;
        this.state = state;
        this.timeId = timeId;
    }

    public int getProcessId() {
        return processId;
    }

    public State getState() {
        return state;
    }

    public int getTimeId() {
        return timeId;
    }

}
