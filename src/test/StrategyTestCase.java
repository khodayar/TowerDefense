package test;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.Test;

import junit.framework.TestCase;
import model.Critter;
import model.GridLocation;
import model.strategy.AttackStrategy;
import model.strategy.FirstStrategy;
import model.strategy.NearestStrategy;
import model.strategy.StrongestStrategy;
import model.strategy.WeakestStrategy;
import model.tower.FireTower;
import model.tower.Tower;

/**
 * This class performs tests for different attack strategies.
 * 
 * @author team 6
 *
 */
public class StrategyTestCase extends TestCase {

    /**
     * Checks if Nearest strategy works correctly.
     */
    @Test
    public void testNearestStrategy() {
        AttackStrategy strategy = new NearestStrategy();

        Tower tower = new FireTower(new GridLocation(5, 5));

        Collection<Critter> critters = new ArrayList<Critter>();
        critters.add(new Critter(new GridLocation(1, 1), 1));
        critters.add(new Critter(new GridLocation(2, 2), 1));
        critters.add(new Critter(new GridLocation(3, 3), 1));
        critters.add(new Critter(new GridLocation(4, 4), 1));

        Critter closest = strategy.attackCritter(tower, critters, new GridLocation(0, 0));
        assertEquals(closest.gridLocation.x, 4);
        assertEquals(closest.gridLocation.y, 4);

    }

    /**
     * Checks if random strategy works correctly.
     */
    @Test
    public void testRandomStrategy() {
        AttackStrategy strategy = new NearestStrategy();

        Tower tower = new FireTower(new GridLocation(5, 5));

        Collection<Critter> critters = new ArrayList<Critter>();
        critters.add(new Critter(new GridLocation(4, 4), 1));

        Critter unlucky = strategy.attackCritter(tower, critters, new GridLocation(0, 0));
        assertEquals(unlucky.gridLocation.x, 4);
        assertEquals(unlucky.gridLocation.y, 4);

    }

    /**
     * Checks if random strategy works correctly when no critters are in range.
     */
    @Test
    public void testRandomStrategyNoCritters() {
        AttackStrategy strategy = new NearestStrategy();

        Tower tower = new FireTower(new GridLocation(5, 5));

        Collection<Critter> critters = new ArrayList<Critter>();
        Critter unlucky = strategy.attackCritter(tower, critters, new GridLocation(0, 0));

        assertNull(unlucky);

    }

    /**
     * Checks if nearest strategy works correctly.
     */
    @Test
    public void testNearestStrategyNoCritters() {
        AttackStrategy strategy = new NearestStrategy();

        Tower tower = new FireTower(new GridLocation(5, 5));

        Collection<Critter> critters = new ArrayList<Critter>();
        Critter closest = strategy.attackCritter(tower, critters, new GridLocation(0, 0));
        assertNull(closest);
    }

    /**
     * Checks if nearest strategy works correctly when no critters are in range.
     */
    @Test
    public void testNearestStrategyNoCrittersInRange() {
        AttackStrategy strategy = new NearestStrategy();

        Tower tower = new FireTower(new GridLocation(5, 5));

        // Creating a location just out of range for the tower.
        int range = tower.getRange();
        GridLocation outOfRange = new GridLocation(tower.getLocation().x, tower.getLocation().y + range + 1);

        Collection<Critter> critters = new ArrayList<Critter>();
        critters.add(new Critter(outOfRange, 1));

        Critter closest = strategy.attackCritter(tower, critters, new GridLocation(0, 0));
        assertNull(closest);
    }

    /**
     * Checks if weakest strategy works correctly.
     */
    @Test
    public void testWeakestStrategy() {
        AttackStrategy strategy = new WeakestStrategy();

        Tower tower = new FireTower(new GridLocation(5, 5));

        ArrayList<Critter> critters = new ArrayList<Critter>();
        Critter weakest = new Critter(new GridLocation(4, 4), 1);
        Critter strongest = new Critter(new GridLocation(6, 6), 1);

        weakest.takeDamage(20, false);

        critters.add(strongest);
        critters.add(weakest);

        Critter closest = strategy.attackCritter(tower, critters, new GridLocation(0, 0));
        assertEquals(closest.gridLocation.x, 4);
        assertEquals(closest.gridLocation.y, 4);

    }

    /**
     * Checks if weakest strategy works correctly when no critters are in range.
     */
    @Test
    public void testWeakestStrategyNoCrittersInRange() {
        AttackStrategy strategy = new WeakestStrategy();

        Tower tower = new FireTower(new GridLocation(5, 5));

        // Creating a location just out of range for the tower.
        int range = tower.getRange();
        GridLocation outOfRange = new GridLocation(tower.getLocation().x, tower.getLocation().y + range + 1);

        Collection<Critter> critters = new ArrayList<Critter>();
        critters.add(new Critter(outOfRange, 1));

        Critter closest = strategy.attackCritter(tower, critters, new GridLocation(0, 0));
        assertNull(closest);
    }

    /**
     * Checks if strongest strategy works correctly.
     */
    @Test
    public void testStrongestStrategy() {
        AttackStrategy strategy = new StrongestStrategy();

        Tower tower = new FireTower(new GridLocation(5, 5));

        ArrayList<Critter> critters = new ArrayList<Critter>();
        Critter weakest = new Critter(new GridLocation(4, 4), 1);
        Critter strongest = new Critter(new GridLocation(6, 6), 1);

        weakest.takeDamage(20, false);

        critters.add(strongest);
        critters.add(weakest);

        Critter closest = strategy.attackCritter(tower, critters, new GridLocation(0, 0));
        assertEquals(closest.gridLocation.x, 6);
        assertEquals(closest.gridLocation.y, 6);

    }

    /**
     * Checks if strongest strategy works correctly when no critters are in range.
     */
    @Test
    public void testStrongestStrategyNoCritters() {
        AttackStrategy strategy = new StrongestStrategy();

        Tower tower = new FireTower(new GridLocation(5, 5));

        Collection<Critter> critters = new ArrayList<Critter>();
        Critter closest = strategy.attackCritter(tower, critters, new GridLocation(0, 0));
        assertNull(closest);
    }

    /**
     * Checks if first strategy works correctly.
     */
    @Test
    public void testFirstStrategy() {
        AttackStrategy strategy = new FirstStrategy();

        Tower tower = new FireTower(new GridLocation(3, 3));

        Collection<Critter> critters = new ArrayList<Critter>();
        critters.add(new Critter(new GridLocation(1, 1), 1));
        critters.add(new Critter(new GridLocation(2, 2), 1));
        critters.add(new Critter(new GridLocation(3, 3), 1));
        critters.add(new Critter(new GridLocation(4, 4), 1));

        // Even though this critter is the first, it is out of range for the tower.
        // It should not be returned.
        GridLocation outOfRange = new GridLocation(19, 19);
        critters.add(new Critter(outOfRange, 1));

        Critter closest = strategy.attackCritter(tower, critters, new GridLocation(20, 20));
        assertEquals(closest.gridLocation.x, 4);
        assertEquals(closest.gridLocation.y, 4);

    }

    /**
     * Checks if first strategy works correctly when no critters are in range.
     */
    @Test
    public void testFirstStrategyNoCritters() {
        AttackStrategy strategy = new FirstStrategy();

        Tower tower = new FireTower(new GridLocation(5, 5));

        Collection<Critter> critters = new ArrayList<Critter>();
        Critter closest = strategy.attackCritter(tower, critters, new GridLocation(0, 0));
        assertNull(closest);
    }

}
