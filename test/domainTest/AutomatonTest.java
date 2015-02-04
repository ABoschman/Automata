/*
 * Fontys Hogescholen ICT - Software Development
 * Modelling with Automata in Professional Practice
 * Project developer: Arjan Boschman
 * Startdate: October 2014
 */
package domainTest;

import domain.Automaton;
import static org.testng.Assert.assertEquals;
import org.testng.annotations.Test;

/**
 *
 * @author Arjan
 */
public class AutomatonTest {

    private Automaton makeAutomaton() {
        return new Automaton(null);
    }
    
    @Test
    public void containsState_noStatesAdded_startstateContainedInStates() {
        final Automaton fsa = makeAutomaton();
        boolean actual = fsa.containsState("A");
        boolean expected = true;
        assertEquals(actual, expected);
    }

    @Test
    public void parseInput_emptyStringStartIsNotTerminator_returnsFalse() {
        Automaton fsa = makeAutomaton();
        boolean actual = fsa.parseInput("");
        boolean expected = false;
        assertEquals(actual, expected);
    }

    @Test
    public void parseInput_emptyStringStartIsTerminator_returnsTrue() {
        Automaton fsa = makeAutomaton();
        fsa.addEndState('A');
        boolean actual = fsa.parseInput("");
        boolean expected = true;
        assertEquals(actual, expected);
    }

    @Test
    public void parseInput_singleInputcharDeterministic_returnsTrue() {
        Automaton fsa = makeAutomaton();
        fsa.addTransition('A', 'B', 'a');
        fsa.addEndState('B');
        boolean actual = fsa.parseInput("a");
        boolean expected = true;
        assertEquals(actual, expected);
    }

    @Test
    public void parseInput_singleInputcharDeterministic_returnsFalse() {
        Automaton fsa = makeAutomaton();
        fsa.addTransition('A', 'B', 'a');
        boolean actual = fsa.parseInput("a");
        boolean expected = false;
        assertEquals(actual, expected);
    }

    @Test
    public void parseInput_multipleInputcharsDeterministic_returnsTrue() {
        Automaton fsa = makeAutomaton();
        fsa.addTransition('A', 'B', 'a');
        fsa.addTransition('B', 'C', 'b');
        fsa.addEndState('C');
        boolean actual = fsa.parseInput("ab");
        boolean expected = true;
        assertEquals(actual, expected);
    }

    @Test
    public void parseInput_emptyInputNONDeterministic_returnsTrue() {
        Automaton fsa = makeAutomaton();
        fsa.addTransition('A', 'B');
        fsa.addEndState('B');
        boolean actual = fsa.parseInput("");
        boolean expected = true;
        assertEquals(actual, expected);
    }

    @Test
    public void parseInput_multipleEpsilonTransitionsInARow_returnsTrue() {
        Automaton fsa = makeAutomaton();
        fsa.addTransition('A', 'B');
        fsa.addTransition('B', 'C');
        fsa.addEndState('C');
        boolean actual = fsa.parseInput("");
        boolean expected = true;
        assertEquals(actual, expected);
    }

    @Test
    public void parseInput_singleInputcharNONDeterministic_returnsTrue() {
        Automaton fsa = makeAutomaton();
        fsa.addTransition('A', 'B', 'a');
        fsa.addTransition('A', 'C', 'a');
        fsa.addEndState('C');
        boolean actual = fsa.parseInput("a");
        boolean expected = true;
        assertEquals(actual, expected);
    }

    @Test
    public void parseInput_multipleInputcharsNONDeterministic_returnsTrue() {
        Automaton fsa = makeAutomaton();
        fsa.addTransition('A', 'B', 'a');
        fsa.addTransition('A', 'C', 'a');
        fsa.addTransition('C', 'D', 'b');
        fsa.addEndState('D');
        boolean actual = fsa.parseInput("ab");
        boolean expected = true;
        assertEquals(actual, expected);
    }
    
    @Test
    public void parseInput_passesExitState_returnsFalse() {
        Automaton fsa = makeAutomaton();
        fsa.addTransition('A', 'B', 'a');
        fsa.addEndState('A');
        boolean actual = fsa.parseInput("a");
        boolean expected = false;
        assertEquals(actual, expected);
    }
    
    @Test
    public void parseInput_loopEpsilon_doesntGetStuck() {
        Automaton fsa = makeAutomaton();
        fsa.addTransition('A', 'B');
        fsa.addTransition('B', 'A');
        fsa.addTransition('A', 'C', 'a');
        fsa.addEndState('C');
        boolean actual = fsa.parseInput("a");
        boolean expected = true;
        assertEquals(actual, expected);
    }
    
}
