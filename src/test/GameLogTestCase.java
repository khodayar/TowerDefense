package test;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import model.Critter;
import model.Game;
import model.tower.Tower;
import model.tower.TowerFactory;

/**
 * This class performs tests on objects of the GameLog class.
 * 
 * @author Team 6
 *
 */
public class GameLogTestCase {

    Game testGame = new Game();
    Tower testTower = TowerFactory.createTower("Ice tower");
    Critter testCritter = new Critter(null, 0);

    /**
     * This method run before each test method. Buys a new tower and place at coordinates (0, 0) on the game grid.
     */
    @Before
    public void beforeClass() {
        testGame.buyTower(testTower, 0, 0);
    }

    /**
     * Tests the buyTower log.
     */
    @Test
    public void testbuyTower() {
        String expectedLog = "tower   [2] (Ice tower) was bought and placed at [0,0] ";
        assertTrue("Log is not correct", testGame.log.contains(expectedLog));
    }

    /**
     * Tests the upgradeTower log.
     */
    @Test
    public void testUpgradeTower() {
        testGame.upgradeTower(0, 0);
        String expectedLog = "tower   [4] (Ice tower) at [0,0] had been upgraded to 2 which costed 4 units ";
        assertTrue("", testGame.log.contains(expectedLog));
    }

    /**
     * Tests the sellTower log.
     */
    @Test
    public void testsellTower() {
        testGame.sellTower(0, 0);
        String expectedLog =
                        "tower   [6] (Ice tower) level (1) at [0,0] has been sold and 3 money units has been refunded ";
        assertTrue(testGame.log.contains(expectedLog));

    }

    /**
     * Tests the sendWave log.
     */
    @Test
    public void testsendWave() {
        testGame.sendWave();
        String expectedLog = "Wave 1 started ! ";
        assertTrue("", testGame.log.contains(expectedLog));
    }

}
