package test;

import org.junit.Test;

import junit.framework.TestCase;
import model.GridLocation;

/**
 * This class performs test for objects of GridLocation class using JUnit3.
 * 
 * @author Team 6
 *
 */
public class GridLocationTestCase extends TestCase {

    /**
     * Tests the default constructor.
     */
    @Test
    public void testDefaultConstructor() {
        GridLocation gridLocation = new GridLocation();
        TestCase.assertEquals(-1, gridLocation.x);
        TestCase.assertEquals(-1, gridLocation.y);
    }

    /**
     * Tests the class' constructor.
     */
    @Test
    public void testConstructor() {
        GridLocation gridLocation = new GridLocation(4, 5);
        TestCase.assertEquals(4, gridLocation.x);
        TestCase.assertEquals(5, gridLocation.y);
    }

    /**
     * Tests the toString method.
     */
    @Test
    public void testToString() {
        GridLocation gridLocation = new GridLocation(2, 3);
        String result = gridLocation.toString();
        String expected = "GridLocation (2, 3)";
        TestCase.assertEquals(result, expected);
    }

    /**
     * Tests the distance method.
     */
    public void testDistance() {
        GridLocation gridLocation1 = new GridLocation(2, 3);
        GridLocation gridLocation2 = new GridLocation(4, 5);
        TestCase.assertEquals(GridLocation.distance(gridLocation1, gridLocation2), 4);

        GridLocation gridLocation3 = new GridLocation(2, 7);
        GridLocation gridLocation4 = new GridLocation(4, 5);
        TestCase.assertEquals(GridLocation.distance(gridLocation3, gridLocation4), 4);

        GridLocation gridLocation5 = new GridLocation(2, 2);
        GridLocation gridLocation6 = new GridLocation(2, 2);
        TestCase.assertEquals(GridLocation.distance(gridLocation5, gridLocation6), 0);
    }

    /**
     * Tests the nearby method.
     */
    @Test
    public void testNearby() {
        GridLocation gridLocation1 = new GridLocation(2, 3);
        GridLocation gridLocation2 = new GridLocation(2, 4);
        assertTrue(GridLocation.nearby(gridLocation1, gridLocation2));

        GridLocation gridLocation3 = new GridLocation(2, 7);
        GridLocation gridLocation4 = new GridLocation(4, 5);
        assertFalse(GridLocation.nearby(gridLocation3, gridLocation4));

        GridLocation gridLocation5 = new GridLocation(2, 7);
        GridLocation gridLocation6 = new GridLocation(3, 7);
        assertTrue(GridLocation.nearby(gridLocation5, gridLocation6));

        GridLocation gridLocation7 = new GridLocation(2, 2);
        GridLocation gridLocation8 = new GridLocation(2, 2);
        assertFalse(GridLocation.nearby(gridLocation7, gridLocation8));
    }

}
