package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.Before;
import org.junit.Test;

import model.Game;
import model.tower.ExplosionTower;
import model.tower.Tower;

/**
 * This class performs tests for objects of Game class.
 * 
 * @author Team 6
 *
 */
public class GameTestCase {

    private Game game;

    /**
     * This method runs before each test method. It initiates an object of Game class.
     * 
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        this.game = new Game();
    }

    /**
     * Adds a tower in location(0,0), and checks the hasTower method.
     */
    @Test
    public void addTower() {
        Tower tower = new ExplosionTower();
        this.game.addTower(tower, 0, 0);
        assertTrue(this.game.hasTower(0, 0));
        assertFalse(this.game.hasTower(0, 1));
    }

    /**
     * Checks the buyTower method.
     */
    @Test
    public void buyTower() {
        Tower tower = new ExplosionTower();
        int towerCost = tower.getInitialCost();
        int initialGameMoney = this.game.getMoney();

        this.game.buyTower(tower, 0, 0);
        assertTrue(this.game.hasTower(0, 0));
        assertEquals(this.game.getMoney(), initialGameMoney - towerCost);

    }

    /**
     * Checks the sellTower method.
     */
    @Test
    public void sellTower() {
        Tower tower = new ExplosionTower();
        int towerCost = tower.getInitialCost();
        int initialGameMoney = this.game.getMoney();

        this.game.buyTower(tower, 0, 0);
        this.game.sellTower(0, 0);
        assertEquals(this.game.getMoney(), initialGameMoney - towerCost + tower.refundAmout());
        assertTrue(this.game.getMoney() < initialGameMoney);
    }

    /**
     * Tests if we can load a game from a file.
     */
    @Test
    public void testLoadGame() {
        Game game = new Game();
        game.loadGame("game.txt");
        assertEquals(game.getKilledCritters(), 0);
        assertEquals(game.getMoney(), 100);
        assertEquals(game.getLives(), 3);
        assertEquals(game.getWave(), 1);
    }

    /**
     * Tests if we can save a game to a file.
     */
    @Test
    public void testSaveGame() {
        Game game = new Game();

        game.saveGame("game_saved.txt");
        File testfile = new File("game_saved.txt");
        assertTrue("The file coud not be written", testfile.exists());
        testfile.delete();

    }

    /**
     * Tests if we can save a game to a file.
     */
    @Test
    public void testSaveLoadGame() {
        Game game = new Game();

        game.loadGame("game.txt");

        game.endTurn();

        game.saveGame("saved.txt");
        game.loadGame("saved.txt");

        assertEquals(game.getWave(), 2);

        File testfile = new File("saved.txt");
        assertTrue("The file coud not be written", testfile.exists());
        testfile.delete();

    }

}
