/*
 * Fontys Hogescholen ICT - Software Development
 * Modelling with Automata in Professional Practice
 * Project developer: Arjan Boschman
 * Startdate: October 2014
 */
package domain;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;

/**
 *
 * @author Arjan
 */
public class Configuration {

    private final State state;
    private final String stack;
    private final String input;

    public Configuration(State state, String stack, String input) {
        this.state = state;
        this.stack = stack;
        this.input = input;
    }

    public State getState() {
        return state;
    }

    public String getStack() {
        return stack;
    }

    public String getInput() {
        return input;
    }

    /**
     * @return Returns the top character on the stack. Returns Epsilon if the
     * stack is empty.
     */
    public char peekAtStack() {
        return stack.length() == 0 ? Transition.EPSILON : stack.charAt(0);
    }

    /**
     * @return Retrieves the next character in the input. Returns Epsilon if
     * input is empty.
     */
    public char getNextInputChar() {
        return input.length() == 0 ? Transition.EPSILON : input.charAt(0);
    }

    /**
     * @return Returns true if and only if the Configuration is in a final state
     * and the input is empty.
     */
    public boolean isValidEndPoint() {
        return input.isEmpty() && state.isTerminator();
    }

    /**
     * Calculates the input string after performing the given Transition. Throws
     * an exception if this Configurations cannot use the given Transition.
     *
     * @param trans The Transition that may or may not consume a symbol.
     * @return The input belonging to the Configuration that exists after
     * performing the given Transition.
     */
    public String getNextInput(Transition trans) {
        if (trans.getInputChar() == Transition.EPSILON) {
            return input;
        } else if (trans.getInputChar() == getNextInputChar()) {
            return input.substring(1);
        } else {
            throw new IllegalArgumentException(
                    "This transition is illegal with this configuration! " + trans + " " + this);
        }
    }

    /**
     * Calculates the stack after performing the given Transition. Symbols may
     * be popped, pushed or popped then pushed. Throws an exception if this
     * Configurations cannot use the given Transition.
     *
     * @param trans The Transition that may or may not consume a symbol.
     * @return The stack belonging to the Configuration that exists after
     * performing the given Transition.
     */
    public String getNextStack(Transition trans) {
        final StringBuilder nextStack = new StringBuilder(stack);
        if (trans.readsFromStack()) {
            if (trans.getReadFromStack() != peekAtStack()) {
                throw new IllegalArgumentException(
                        "This transition is illegal with this configuration! " + trans + " " + this);
            }
            nextStack.deleteCharAt(0);
        }
        if (trans.writesToStack()) {
            nextStack.insert(0, trans.getWriteToStack());
        }
        return nextStack.toString();
    }

    /**
     * Calculate all the Configurations reachable by consuming the next input
     * symbol.
     *
     * @return
     */
    public Set<Configuration> consumeOneSymbol() {
        return calcNextConfigs(false);
    }

    /**
     * Calculate all the Configurations reachable by recursively doing
     * epsilon-Transitions.
     *
     * @return
     */
    public Set<Configuration> calculateEpsilonClosure() {
        return calcNextConfigs(true);
    }

    private Set<Configuration> calcNextConfigs(boolean doEpsilon) {
        final Set<Configuration> nextConfigs = new HashSet<>();
        final Iterator<Transition> itTrans = state.getTransitions();
        while (itTrans.hasNext()) {
            final Transition trans = itTrans.next();
            if (trans.readsInput(doEpsilon ? Transition.EPSILON : getNextInputChar())) {
                trans.attemptTransition(this).ifPresent((config) -> nextConfigs.add(config));
//                nextConfigs.add(new Configuration(trans.getTargetState(), "", getNextInput(trans)));
            }
        }
        return nextConfigs;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 37 * hash + Objects.hashCode(this.state);
        hash = 37 * hash + Objects.hashCode(this.stack);
        hash = 37 * hash + Objects.hashCode(this.input);
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
        final Configuration other = (Configuration) obj;
        return Objects.equals(this.state, other.getState())
                && Objects.equals(this.stack, other.getStack())
                && Objects.equals(this.input, other.getInput());
    }

    @Override
    public String toString() {
        return "Configuration{" + "state=" + state + ", stack=" + stack + ", input=" + input + '}';
    }

}
