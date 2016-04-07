package model;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Observable;

import model.GameGrid.CASE_TYPES;
import model.tower.ExplosionTower;
import model.tower.FireTower;
import model.tower.IceTower;
import model.tower.Tower;
import model.tower.TowerFactory;

/**
 * This class implements the main gaming logic in which user can buy, sell, upgrade towers. It is also observable so
 * that a view class can be notified of internal changes.
 *
 * @author Team 6
 *
 */
public class Game extends Observable {

    /**
     * Initial amount of money that the player has to buy towers.
     */
    public static final int INITIAL_MONEY = 100;

    /**
     * Initial amount of lives the player has.
     */
    public static final int INITIAL_LIVES = 3;

    /**
     * Number of critters released per wave.
     */
    private static final int CRITTERS_PER_WAVE = 3;

    /**
     * Number of waves the player has to go through before winning the game.
     */
    private static final int WAVES_TO_WIN = 3;

    /**
     * List of available towers that the user can buy.
     */
    public static Tower[] AVAILABLE_TOWERS = {new FireTower(), new IceTower(), new ExplosionTower()};
    public int deadCount = INITIAL_LIVES;

    public GameGrid grid;
    public HashMap<Point, Critter> critters = new HashMap<Point, Critter>();
    public ArrayList<GridLocation> attackedCritters;
    public Path shortestPath;
    public String log = "";
    public boolean startlog = true;
    public String logfile;

    private HashMap<Point, Tower> towers = new HashMap<Point, Tower>();
    private int money;
    private GameThread gameThread;
    private int crittersReleased;
    private int lives;
    private int wave;
    private int killedCritters = 0;

    /**
     * Constructs the Game object with an empty 100x100 grid.
     */
    public Game() {
        this.grid = new GameGrid(100, 100);
        this.money = Game.INITIAL_MONEY;
        this.lives = Game.INITIAL_LIVES;
        this.shortestPath = new Path(this.grid);
        this.wave = 1;
        this.attackedCritters = new ArrayList<GridLocation>();
    }

    /**
     * Gets the current amount of money the player has.
     *
     * @return an integer representing the amount of money the player has
     */
    public int getMoney() {
        return this.money;
    }

    /**
     * Sets the current amount of the money the player has
     *
     * @param money an integer representing amount of money the player has
     */

    public void setMoney(int money) {
        this.money = money;
    }

    /**
     * Buys a news tower and place it at the specified position on the grid.
     *
     * @param tower The tower to buy.
     * @param line Line where to place the new tower.
     * @param column Column where to place the new tower.
     */
    public void buyTower(Tower tower, int line, int column) {
        if (tower.getInitialCost() > this.money) {
            return;
        } else if (this.hasTower(line, column)) {
            return;
        }
        this.money -= tower.getInitialCost();
        Tower newTower = TowerFactory.createTower(tower.getName());
        newTower.setLocation(new GridLocation(line, column));
        log = "tower   [" + newTower.getTowerID() + "] (" + newTower.getName() + ") " + "was bought and placed at ["
                + line + "," + column + "] \n";
        this.addTower(newTower, line, column);
    }

    /**
     * Sells a tower at a specific location of the game grid.
     *
     * @param line Line where to place the new tower.
     * @param column Column where to place the new tower.
     */
    public void sellTower(int line, int column) {
        Tower tower = this.getTower(line, column);
        this.money += tower.refundAmout();
        this.towers.remove(new Point(line, column));
        log = "tower   [" + tower.getTowerID() + "] (" + tower.getName() + ") level (" + tower.getLevel() + ") at ["
                + line + "," + column + "] has been sold and " + tower.refundAmout()
                + " money units has been refunded \n";
        this.setChanged();
        this.notifyObservers();
    }

    /**
     * Adds a tower at the specific location.
     *
     * @param t Type of tower to place.
     * @param line Line where to place the new tower.
     * @param column Column where to place the new tower.
     */
    public void addTower(Tower t, int line, int column) {
        Point location = new Point(line, column);
        this.towers.put(location, t);
        this.setChanged();
        this.notifyObservers();
    }

    /**
     * Checks if there is a tower at a specific location.
     *
     * @param line Line where to check for tower.
     * @param column Column where to check for tower.
     *
     * @return True if there is a tower at that location.
     */
    public boolean hasTower(int line, int column) {
        Point location = new Point(line, column);
        return this.towers.get(location) != null;
    }

    /**
     * Returns the tower at a specific location.
     *
     * @param line Line where tower is.
     * @param column Column where the tower is.
     *
     * @return Tower located at the specified location
     */
    public Tower getTower(int line, int column) {
        Point location = new Point(line, column);
        return this.towers.get(location);
    }

    /**
     * Gets the towers.
     *
     * @return towers
     */

    public HashMap<Point, Tower> getTowers() {
        return towers;
    }

    /**
     * Sets the towers.
     *
     * @param towers a hashMap representing the towers and their points
     */

    public void setTowers(HashMap<Point, Tower> towers) {
        this.towers = towers;
    }

    /**
     * Upgrade the level of the tower at a specific location.
     *
     * @param line Line of the tower to upgrade.
     * @param column Column of the tower to upgrade.
     */
    public void upgradeTower(int line, int column) {
        Tower tower = this.getTower(line, column);
        if (this.money >= tower.getLevelCost()) {
            tower.upgradeLevel();
            this.money -= tower.getLevelCost();
            log = "tower   [" + tower.getTowerID() + "] (" + tower.getName() + ") at [" + line + "," + column
                    + "] had been upgraded to " + tower.getLevel() + " which costed " + tower.getLevelCost()
                    + " units \n";
            this.setChanged();
            this.notifyObservers();
        }
    }

    /**
     * Change the attack strategy of the tower at a specific location.
     *
     * @param strategy is the name of the new strategy
     * @param line Line of the tower to upgrade.
     * @param column Column of the tower to upgrade.
     */
    public void changeStrategyTower(String strategy, int line, int column) {
        Tower tower = this.getTower(line, column);
        tower.setAttackStrategy(strategy);
        log="tower   [" + tower.getTowerID() + "] attack strategy has been changed to "+strategy +"\n";
        this.setChanged();
        this.notifyObservers();
    }

    /**
     * Initiates a new wave of critters.
     */
    public void sendWave() {
        if (this.gameThread != null) {
            return;
        }
        this.crittersReleased = 0;
        this.gameThread = new GameThread(this);
        log = "Wave " + this.wave + " started ! \n";
        gameThread.start();
    }

    /**
     * Add a critter on the game grid.
     *
     * @param critter The critter to place on the grid.
     */
    public void addCritter(Critter critter) {
        this.critters.put(critter.gridLocation, critter);
        this.setChanged();
        this.notifyObservers();
    }

    /**
     * Determines if there is a critter at the specified location on the grid.
     *
     * @param location Location to verify if there is a critter
     *
     * @return A boolean indicating if there is a critter at the specified location.
     */
    public boolean hasCritter(GridLocation location) {
        return (this.critters.get(location) != null);
    }

    /**
     * Determines if there a free case for a critter at the specified location.
     *
     * @param location Location to verify if there is a free case for a critter
     *
     * @return A boolean indicating if there is a free case for a critter at the specified location.
     */
    public boolean noCritter(GridLocation location) {
        return (this.grid.getCases()[location.x][location.y] == CASE_TYPES.ROAD && this.critters.get(location) == null);
    }

    /**
     * Makes a game turn. A turn consists of critters moving, towers shooting, the player earning money and losing life
     * points, etc.
     */
    public void makeTurn() {

        this.attackedCritters.clear();

        // This will apply the effects the critters received at the last
        // turn (freezing / burning).
        for (Critter critter : this.critters.values()) {
            critter.makeTurn();
        }

        this.moveCritters();
        this.addNewCritters();
        this.attackCritters();
        this.removeDeadCritters();

        if (this.critters.size() == 0 && this.crittersReleased == Game.CRITTERS_PER_WAVE) {
            this.endTurn();
        }


        if (this.isWon()) {
            log += "\n Player Won the Game !! remaining lives: "+this.getLives()+" ,total money: "+this.getMoney()+", critters killed:"+this.killedCritters+ "\n";
        }

        if (this.isOver() || this.isWon()) {
            GameScore gameScore = new GameScore();
            gameScore.datePlayed = new Date(System.currentTimeMillis());
            gameScore.killedCritters = this.killedCritters;
            gameScore.won = this.isWon();
            this.grid.addGameScore(gameScore);
            this.grid.writeToFile();
        }
        this.setChanged();
        this.notifyObservers();
        log = "";

    }

    /**
     * Attack the critters with the towers on the grid.
     */
    private synchronized void attackCritters() {
        // Towers attacking if the turn is not over.
        GridLocation attackedLocation = null;
        for (Tower tower : this.towers.values()) {
            ArrayList<Critter> aliveCritters = new ArrayList<Critter>();
            for (Critter critter : this.critters.values()) {
                if (!critter.isDead()) {
                    aliveCritters.add(critter);
                }
            }
            attackedLocation = tower.attack(aliveCritters, this.grid.exitPoint());
            if (tower.attack(aliveCritters, this.grid.entryPoint()) != null) {
                log += "tower   [" + this.getTower(tower.getLocation().x, tower.getLocation().y).getTowerID() + "] at "
                        + tower.getLocation() + " Attacked a critter at "
                        + tower.attack(aliveCritters, this.grid.entryPoint()) + "\n";
                log += "critter [" + this.critters.get(tower.attack(aliveCritters, this.grid.entryPoint())).critterID
                        + "] attacked by tower ["
                        + this.getTower(tower.getLocation().x, tower.getLocation().y).getTowerID()
                        + "] and lost one life \n";

            }
            if (attackedLocation != null) {
                this.attackedCritters.add(attackedLocation);
            }
        }

    }

    /**
     * Add new critters on the grid, coming from the entry point.
     */
    private synchronized void addNewCritters() {
        if (Game.CRITTERS_PER_WAVE > this.crittersReleased) {

            GridLocation start = this.shortestPath.getShortestPath().get(0);

            // This means a critter is blocking the entry.
            if (this.critters.get(start) != null) {
                return;
            }

            Critter critty = new Critter(start, this.wave);
            this.addCritter(critty);
            log = "critter [" + critty.critterID + "]  (level " + critty.getLevel() + ") entered the map \n";
            this.crittersReleased++;

        }
    }

    /**
     * Move the critters forward on the grid.
     */
    private synchronized void moveCritters() {
        // log="";
        ArrayList<GridLocation> shortestPath = this.shortestPath.getShortestPath();

        // We go through the shortest path in reverse order. This is
        // to make sure that moving a critter forward does not overwrite
        // another critter that was on the next location on the path.
        for (int i = shortestPath.size() - 1; i >= 0; i--) {

            GridLocation pathLocation = shortestPath.get(i);
            Critter critter = this.critters.get(pathLocation);

            // No critter to more forward on the path at this location,
            // or the critter is frozen!
            if (critter == null || !critter.shouldMove()) {
                continue;
            }
            critter.move();

            GridLocation nextLocation = this.shortestPath.getNextLocation(critter.gridLocation);
            if (nextLocation != null) {
                if (deadCount == this.lives)
                    log = "critter [" + critter.critterID + "] is at location :" + nextLocation + "\n";
                else {
                    log += "critter [" + critter.critterID + "] is at location :" + nextLocation + "\n";
                    deadCount--;
                }
            }

            // The critter has reached the exit!
            if (nextLocation == null) {
                this.critters.remove(critter.gridLocation);
                this.lives--;
                log += "critter [" + critter.critterID + "] passed the exit ! total health is now  " + this.getLives()
                + "\n";
                if (this.lives == 0) {
                    log += "Player has lost all of the lives , GAME IS OVER \n";
                    log += "Record: waves: " + this.wave + "  , money: " + this.getMoney() + " units";
                }

                // There is another location the critter can move to, and it is free.
            } else if (critters.get(nextLocation) == null) {
                this.critters.remove(critter.gridLocation);
                critter.setLocation(nextLocation);
                this.addCritter(critter);
            }

        }

    }

    /**
     * Remove the critters killed by the towers.
     */
    private synchronized void removeDeadCritters() {
        HashMap<Point, Critter> critters = new HashMap<Point, Critter>();
        for (Critter critter : this.critters.values()) {
            if (!critter.isDead()) {
                critters.put(critter.gridLocation, critter);
            } else {
                log += "critter [" + critter.critterID + "] has been neutralized at " + critter.gridLocation + "\n";
                this.money += critter.getReward();
                this.killedCritters++;
            }
        }
        this.critters = critters;
    }

    /**
     * Get the remaining lives of the player.
     *
     * @return An integer representing the life count of the player.
     */
    public int getLives() {
        return lives;
    }

    /**
     * Sets the remaining lives of the player.
     *
     * @param lives an integer representing the remaining lives of the player.
     */

    public void setLives(int lives) {
        this.lives = lives;
    }

    /**
     * Determines if the current game is over. That is, if the player has no more lives.
     *
     * @return true is the game is over, false otherwise.
     */
    public boolean isOver() {
        return this.lives <= 0;
    }

    /**
     * Determines if the player has won the game
     *
     * @return True if the number of waves required wave for winning is reached.
     */
    public boolean isWon() {
        return this.wave > Game.WAVES_TO_WIN;
    }

    /**
     * Determines if a current game turn is happening.
     *
     * @return True if the game is in a turn, false otherwise.
     */
    public boolean isMakingTurn() {
        return this.gameThread != null;
    }

    /**
     * Ends the current game turn.
     */
    public void endTurn() {
        if (this.gameThread != null) {
            this.gameThread.stopThread();
            this.gameThread = null;
        }
        this.wave++;
        this.critters.clear();
        this.crittersReleased = 0;
    }

    /**
     * Saves the current game to a file.
     *
     * @param filePath a string representing the file name of the saving game
     */
    public void saveGame(String filePath) {

        PrintWriter printWriter;

        try {

            printWriter = new PrintWriter(filePath);

            printWriter.println(this.grid.filePath);

            // health and money
            printWriter.println(this.getLives());
            printWriter.println(this.getMoney());
            printWriter.println(this.getWave());
            printWriter.println(this.getKilledCritters());

            printWriter.println(this.towers.size());
            for (Tower t : this.getTowers().values()) {
                printWriter.print(t.getLocation().x + ",");
                printWriter.print(t.getLocation().y + ",");
                printWriter.print(t.getName() + ",");
                printWriter.print(t.getLevel() + ",");
                printWriter.print(t.getAttackStrategy().getName() + ",");
                printWriter.println();
            }

            printWriter.close();

        } catch (FileNotFoundException exception) {
            exception.printStackTrace();
        }

    }

    /**
     * Loads the saved game.
     *
     * @param filePath a string representing the file name of the saved game
     */
    public void loadGame(String filePath) {

        HashMap<Point, Tower> towers = new HashMap<Point, Tower>();
        String[] tokens;

        try {

            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filePath)));
            String line = null;

            // read the 1st line, dimensions of the map
            String mapFilePath = br.readLine();
            this.grid = new GameGrid();
            this.grid.readFromFile(mapFilePath, false);

            this.lives = Integer.parseInt(br.readLine());
            this.money = Integer.parseInt(br.readLine());
            this.wave = Integer.parseInt(br.readLine());
            this.killedCritters = Integer.parseInt(br.readLine());

            int towerCount = Integer.parseInt(br.readLine());

            for (int i = 0; i < towerCount; i++) {

                line = br.readLine();
                tokens = line.split(",");

                int x = Integer.parseInt(tokens[0]);
                int y = Integer.parseInt(tokens[1]);
                Tower tower = TowerFactory.createTower(tokens[2]);
                int level = Integer.parseInt(tokens[3]);
                String strategyName = tokens[4];

                tower.setLocation(new GridLocation(x, y));
                tower.setLevel(level);
                tower.setAttackStrategy(strategyName);

                towers.put(tower.getLocation(), tower);
            }

            br.close();

        } catch (IOException exception) {
            exception.printStackTrace();
        }

        this.setTowers(towers);

    }

    /**
     * Gets the number of killed critters.
     *
     * @return The number of killed critters in the game.
     */
    public int getKilledCritters() {
        return this.killedCritters;
    }

    /**
     * Gets the current wave.
     *
     * @return The serial number of the current wave.
     */
    public int getWave() {
        return this.wave;
    }

}
