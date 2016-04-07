package test;

import org.junit.Test;

import junit.framework.TestCase;
import model.Critter;
import model.GridLocation;

/**
 * This class performs test cases for an object of the Critter class.
 * 
 * @author Team 6
 *
 */
public class CritterTestCase extends TestCase {

    /**
     * Tests the takeDamage method from the Critter class.
     */
    @Test
    public void testTakeDamage() {
        Critter critter = new Critter(new GridLocation(1, 0), 3);
        int damage = critter.getHealthPoints() - 1;

        critter.takeDamage(damage, false);

        assertEquals(critter.getHealthPoints(), 1);
        assertFalse(critter.isDead());

    }

    /**
     * Tests if the critter gets killed when using takeDamage.
     */
    @Test
    public void testKillCritter() {
        Critter critter = new Critter(new GridLocation(1, 0), 3);
        int damage = critter.getHealthPoints() + 1;

        critter.takeDamage(damage, false);

        assertEquals(critter.getHealthPoints(), 0);
        assertTrue(critter.isDead());

    }

    /**
     * Tests if the critter gets frozen when using freeze.
     */
    @Test
    public void testFreezeCritter() {
        Critter critter1 = new Critter(new GridLocation(1, 0), 1);
        Critter critter2 = new Critter(new GridLocation(1, 0), 1);

        critter2.freeze();

        critter1.makeTurn();
        critter2.makeTurn();

        assertTrue(critter1.getMovementPoints() > critter2.getMovementPoints());

    }

    /**
     * Tests if the critter gets burnt by using takeDamage.
     */
    @Test
    public void testBurnCritter() {
        Critter critter = new Critter(new GridLocation(1, 0), 3);

        int damage = critter.getHealthPoints() / 2;
        critter.takeDamage(damage, true);

        // The burning damage should not apply instantly.
        assertEquals(critter.getHealthPoints(), damage);

        critter.makeTurn();

        // Burning damage, but should not kill the critter.
        assertFalse(critter.getHealthPoints() == damage);
        assertFalse(critter.isDead());

        // The burning damage should not last more
        // than one turn.
        critter.makeTurn();
        critter.makeTurn();
        critter.makeTurn();
        critter.makeTurn();
        assertFalse(critter.isDead());

    }

    /**
     * Tests if multiple calls to the takeDamage kills the critter.
     */
    @Test
    public void testBurnCritterMultiple() {
        Critter critter = new Critter(new GridLocation(1, 0), 3);

        int damage = critter.getHealthPoints() / 3;
        critter.takeDamage(damage, true);
        critter.takeDamage(damage, true);

        // The burning damage should not apply instantly.
        assertEquals(critter.getHealthPoints(), damage);

        critter.makeTurn();

        // Both burning damages should be applied. The
        // critter should be dead now.
        assertTrue(critter.isDead());

    }

    /**
     * Tests if getReward method works correctly.
     */
    @Test
    public void testgetRewards() {
        Critter critter = new Critter(new GridLocation(1, 0), 1);

        int reward = critter.getReward();

        assertEquals(reward, Critter.INITIAL_HEALTH_POINTS + (1 * Critter.HEALTH_POINTS_PER_LEVEL));

        critter = new Critter(new GridLocation(1, 0), 2);

        assertTrue(critter.getReward() > reward);

    }

}
