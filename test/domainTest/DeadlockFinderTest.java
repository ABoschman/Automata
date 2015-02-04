/*
 * Fontys Hogescholen ICT - Software Development
 * Modelling with Automata in Professional Practice
 * Project developer: Arjan Boschman
 * Startdate: October 2014
 */
package domainTest;

import domain.Automaton;
import domain.DeadlockFinder;
import domain.State;
import domain.Transition;
import static org.testng.Assert.assertEquals;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 *
 * @author Arjan
 */
public class DeadlockFinderTest {

    private Automaton autoA;
    private Automaton autoB;
    private DeadlockFinder dlFinder;

    private Automaton makeAutomaton(String startState) {
        return new Automaton(null, new State(startState));
    }

    @BeforeMethod
    public void before() {
        this.autoA = makeAutomaton("A");
        this.autoB = makeAutomaton("P");
        this.dlFinder = new DeadlockFinder(autoA, autoB);
    }

    @Test
    public void hasDeadlock_noTransitions_alwaysFalse() {
        final boolean actual = dlFinder.hasDeadlock();
        final boolean expected = false;
        assertEquals(actual, expected);
    }

    @Test
    public void hasDeadlock_noIOTransactions_alwaysFalse() {
        autoA.addTransition('A', 'B');
        autoB.addTransition('P', 'Q');
        final boolean actual = dlFinder.hasDeadlock();
        final boolean expected = false;
        assertEquals(actual, expected);
    }

    @Test
    public void hasDeadlock_singleWriteNoReadOnAutoA_deadlockSoReturnsTrue() {
        autoA.addTransition("A", "B", "W1", Transition.EPSILON, Transition.EPSILON);
        final boolean actual = dlFinder.hasDeadlock();
        final boolean expected = true;
        assertEquals(actual, expected);
    }

    @Test
    public void check_readAndWrite_noDeadlockSoReturnsFalse() {
        autoA.addTransition("A", "B", "R1", Transition.EPSILON, Transition.EPSILON);
        autoB.addTransition("P", "Q", "W1", Transition.EPSILON, Transition.EPSILON);
        final boolean actual = dlFinder.hasDeadlock();
        final boolean expected = false;
        assertEquals(actual, expected);
    }
    
    @Test
    public void check_loopingSink_deadlocksSoReturnsTrue() {
        autoA.addTransition("A", "B", "R1", Transition.EPSILON, Transition.EPSILON);
        autoA.addTransition('A', 'C');
        autoA.addTransition('C', 'D');
        autoA.addTransition('D', 'C');
        
        autoB.addTransition("P", "Q", "W1", Transition.EPSILON, Transition.EPSILON);
        
        final boolean actual = dlFinder.hasDeadlock();
        final boolean expected = true;
        assertEquals(actual, expected);
    }

    @Test
    public void check_loopingSinkWithEscape_noDeadlocksSoReturnsFalse() {
        autoA.addTransition("A", "B", "R1", Transition.EPSILON, Transition.EPSILON);
        autoA.addTransition('A', 'C');
        autoA.addTransition('C', 'D');
        autoA.addTransition('D', 'C');
        autoA.addTransition('D', 'A');
        
        autoB.addTransition("P", "Q", "W1", Transition.EPSILON, Transition.EPSILON);

        final boolean actual = dlFinder.hasDeadlock();
        final boolean expected = false;
        assertEquals(actual, expected);
    }
    
    @Test
    public void check_multipleTransitionsOnlyOnePairMatches_noDeadlockSoReturnsFalse() {
        autoA.addTransition("A", "B", "R1", Transition.EPSILON, Transition.EPSILON);
        autoA.addTransition("A", "B", "R2", Transition.EPSILON, Transition.EPSILON);
        autoA.addTransition("A", "B", "W1", Transition.EPSILON, Transition.EPSILON);
        autoA.addTransition("A", "B", "W2", Transition.EPSILON, Transition.EPSILON);
        
        autoB.addTransition("P", "Q", "W1", Transition.EPSILON, Transition.EPSILON);
        final boolean actual = dlFinder.hasDeadlock();
        final boolean expected = false;
        assertEquals(actual, expected);
    }
    
    @Test(timeOut = 2000L)
    public void check_loopingIOTransitions_returnsFalseDoesntHang() {
        autoA.addTransition("A", "B", "R1", Transition.EPSILON, Transition.EPSILON);
        autoA.addTransition("B", "C", "R2", Transition.EPSILON, Transition.EPSILON);
        autoA.addTransition("C", "A", "R3", Transition.EPSILON, Transition.EPSILON);
        
        autoB.addTransition("P", "P", "W1", Transition.EPSILON, Transition.EPSILON);
        autoB.addTransition("P", "P", "W2", Transition.EPSILON, Transition.EPSILON);
        autoB.addTransition("P", "P", "W3", Transition.EPSILON, Transition.EPSILON);
        
        final boolean actual = dlFinder.hasDeadlock();
        final boolean expected = false;
        assertEquals(actual, expected);
    }
    
    

}
