/*
 * Fontys Hogescholen ICT - Software Development
 * Modelling with Automata in Professional Practice
 * Project developer: Arjan Boschman
 * Startdate: October 2014
 */
package domain;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 *
 * @author Arjan
 */
public class Transition {

    public static final char EPSILON = '\u03B5';

    private final String input;
    private final char readFromStack;
    private final char writeToStack;
    private final State sourceState;
    private final State targetState;

    public Transition(State sourceState, State targetState, char inputChar, char readFromStack, char writeToStack) {
        this(sourceState, targetState, String.valueOf(inputChar), readFromStack, writeToStack);
    }

    public Transition(State sourceState, State targetState, String input, char readFromStack, char writeToStack) {
        this.sourceState = sourceState;
        this.targetState = targetState;
        this.input = input;
        this.readFromStack = readFromStack;
        this.writeToStack = writeToStack;
    }

    public State getSourceState() {
        return sourceState;
    }

    public State getTargetState() {
        return targetState;
    }

    public char getInputChar() {
        return input.charAt(0);
    }

    public char getReadFromStack() {
        return readFromStack;
    }

    public char getWriteToStack() {
        return writeToStack;
    }

    public String getInput() {
        return input;
    }

    public boolean readsInput(char inputChar) {
        return this.input.equals(String.valueOf(inputChar));
    }

    public boolean takesNoInput() {
        return readsInput(EPSILON);
    }

    public boolean readsFromStack() {
        return readFromStack != EPSILON;
    }

    public boolean writesToStack() {
        return writeToStack != EPSILON;
    }

    /**
     * Checks if this Transition can be run with the given character on top of
     * the stack.
     *
     * @param character The character on top of the stack.
     * @return True if the transition can NOT be used because the required
     * character is not available.
     */
    public boolean cannotTransitionWithThisStackTop(char character) {
        return readFromStack != character && readFromStack != EPSILON;
    }

    public boolean readsFromChannel() {
        return getInputChar() == 'R';
    }

    public boolean writesToChannel() {
        return getInputChar() == 'W';
    }

    /**
     * @return true if this transition reads or writes to a channel.
     */
    public boolean performsIOOperation() {
        return readsFromChannel() || writesToChannel();
    }

    public boolean performsStackOperation() {
        return readsFromStack() || writesToStack();
    }

    public int getChannel() {
        if (input.length() == 1) {
            return 0;
        } else {
            return Integer.valueOf(input.substring(1));
        }
    }

    /**
     *
     * @param readOrWrite Must be either 'R' for read or 'W' for write.
     * @param channelNr
     * @return
     */
    public boolean canCommunicateWith(char readOrWrite, int channelNr) {
        assert (readOrWrite == 'R' || readOrWrite == 'W');
        return getChannel() == channelNr && readOrWrite != getInputChar();
    }

    /**
     * Checks if this Transition is compatible the given Transition. This has to
     * do with IO operations. If both Transitions are IO operations, they are
     * considered compatible if and only if one reads, one writes and they are
     * on the same channel. If only one is an IO Transition, they're never
     * compatible. If they're both regular Transitions, they don't communicate
     * and thus are always considered compatible.
     *
     * @param trans2 The other Transition.
     * @return True if the two Transitions are compatible.
     */
    public boolean isCompatibleWith(Transition trans2) {
        if (this.performsIOOperation() && trans2.performsIOOperation()) {
            return this.canCommunicateWith(trans2.getInputChar(), trans2.getChannel());
        } else {
            return !this.performsIOOperation() && !trans2.performsIOOperation();
        }
    }

    /**
     * Check if this Transition can be executed on the given Configuration. If
     * so, the resulting configuration will be returned as an Optional. If it's
     * not possible, for example because the top stack character or the next
     * input symbol are wrong, an empty Optional will be returned.
     *
     * @param config The starting Configuration.
     * @return An Optional<> wrapping the resulting Configuration. Will be empty
     * if there is no valid resulting Configuration.
     */
    public Optional<Configuration> attemptTransition(Configuration config) {
        if ((takesNoInput() || readsInput(config.getNextInputChar()) || performsIOOperation())
                && (!readsFromStack() || getReadFromStack() == config.peekAtStack())) {
            return Optional.of(new Configuration(getTargetState(), config.getNextStack(this), config.getNextInput(this)));
        } else {
            return Optional.empty();
        }
    }

    /**
     * Used to find all States transitively reachable through this Transition
     * without going through IO Transitions, that have IO Transitions and can
     * therefore block.
     *
     * @param visited A Map of States that are already considered.
     * @param result The Set of States that has been found.
     * @param myDependencies Used for determining if the SourceState is a sink
     * contingency.
     */
    public void findContinThroughTrans(Map<State, IoType> visited,
            Set<State> result, final Set<State> myDependencies) {
        final IoType targetType = getTargetState().collectContingencies(visited, result, myDependencies);
        if (targetType.equals(IoType.HAS_IO)) {
            visited.put(getSourceState(), targetType);
        } else if (targetType.equals(IoType.STILL_CALC)) {
            myDependencies.add(getTargetState());
        }
    }

    @Override
    public String toString() {
        return getSourceState() + " ==> In: " + input + " Pop: " + readFromStack
                + " Push: " + writeToStack + " ==> " + getTargetState();
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + Objects.hashCode(this.input);
        hash = 59 * hash + this.readFromStack;
        hash = 59 * hash + this.writeToStack;
        hash = 59 * hash + Objects.hashCode(this.sourceState);
        hash = 59 * hash + Objects.hashCode(this.targetState);
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
        final Transition other = (Transition) obj;
        return Objects.equals(this.input, other.getInput())
                && this.readFromStack == other.getReadFromStack()
                && this.writeToStack == other.getWriteToStack()
                && Objects.equals(this.sourceState, other.getSourceState())
                && Objects.equals(this.targetState, other.getTargetState());
    }

}
