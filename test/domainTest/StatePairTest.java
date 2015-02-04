/*
 * Fontys Hogescholen ICT - Software Development
 * Modelling with Automata in Professional Practice
 * Project developer: Arjan Boschman
 * Startdate: October 2014
 */
package domainTest;

import domain.State;
import domain.StatePair;
import domain.Transition;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;
import org.testng.annotations.Test;

/**
 *
 * @author Arjan
 */
public class StatePairTest {
    
    private Transition getSingleNonIOTrans() {
        return new Transition(null, null, "a", Transition.EPSILON, Transition.EPSILON);
    }
    
    private Transition getSingleR1Trans() {
        return new Transition(null, null, "R1", Transition.EPSILON, Transition.EPSILON);
    }
    
    private Transition getSingleW1Trans() {
        return new Transition(null, null, "W1", Transition.EPSILON, Transition.EPSILON);
    }
    
    private Transition getSingleW2Trans() {
        return new Transition(null, null, "W2", Transition.EPSILON, Transition.EPSILON);
    }
    
    @Test
    public void deadlocks_neitherHasIO_false() {
        final State state1 = new State(null, getSingleNonIOTrans());
        final State state2 = new State(null, getSingleNonIOTrans());
        final StatePair pair = new StatePair(state1, state2);
        assertFalse(pair.deadlocks());
    }
    
    @Test
    public void deadlocks_state1HasIO_true() {
        final State state1 = new State(null, getSingleR1Trans());
        final State state2 = new State(null, getSingleNonIOTrans());
        final StatePair pair = new StatePair(state1, state2);
        assertTrue(pair.deadlocks());
    }
    
    @Test
    public void deadlocks_state2HasIO_true() {
        final State state1 = new State(null, getSingleNonIOTrans());
        final State state2 = new State(null, getSingleR1Trans());
        final StatePair pair = new StatePair(state1, state2);
        assertTrue(pair.deadlocks());
    }
    
    @Test
    public void deadlocks_mismatchingIOR1W1_true() {
        final State state1 = new State(null, getSingleR1Trans());
        final State state2 = new State(null, getSingleR1Trans());
        final StatePair pair = new StatePair(state1, state2);
        assertTrue(pair.deadlocks());
    }
    
    @Test
    public void deadlocks_mismatchingIODifferentChannels_true() {
        final State state1 = new State(null, getSingleR1Trans());
        final State state2 = new State(null, getSingleW2Trans());
        final StatePair pair = new StatePair(state1, state2);
        assertTrue(pair.deadlocks());
    }
    
    @Test
    public void deadlocks_matchingIO_false() {
        final State state1 = new State(null, getSingleR1Trans());
        final State state2 = new State(null, getSingleW1Trans());
        final StatePair pair = new StatePair(state1, state2);
        assertFalse(pair.deadlocks());
    }
    
    @Test
    public void deadlocks_oneMatchingIOOneMismatch_false() {
        final State state1 = new State(null, getSingleR1Trans(), getSingleW1Trans());
        final State state2 = new State(null, getSingleR1Trans());
        final StatePair pair = new StatePair(state1, state2);
        assertFalse(pair.deadlocks());
    }
    
    @Test
    public void deadlocks_state1HasSingleNonIOState2HasNonIOAndR1_false() {
        final State state1 = new State(null, getSingleNonIOTrans());
        final State state2 = new State(null, getSingleNonIOTrans(), getSingleR1Trans());
        final StatePair pair = new StatePair(state1, state2);
        assertFalse(pair.deadlocks());
    }

}
