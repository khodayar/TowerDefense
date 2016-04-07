package model.tower;

import java.util.ArrayList;
import java.util.Collection;

import model.Critter;
import model.GridLocation;
import model.strategy.AttackStrategy;
import model.strategy.AttackStrategyFactory;
import model.strategy.RandomStrategy;

/**
 * Base class for game towers.
 *
 * @author Team 6
 *
 */
public abstract class Tower {

    /**
     * Refund rate of the towers.
     */
    public static double REFUND_RATE = 0.40;

    private static int idCounter = -3;
    private int towerID;

    /**
     * Statistics elements of the tower.
     */
    protected String name;
    protected String iconPath;
    protected int initialCost;
    protected int level = 1;
    protected int levelCost;
    protected int range;
    protected int power;
    protected int rateOfFire;
    protected ArrayList<String> logs = new ArrayList<String>();

    /**
     * Special effect of the tower. Currently only 1 special effect per tower is supported.
     */
    protected String specialEffect;

    /**
     * Attack strategy the tower uses when attacking the critters.
     */
    protected AttackStrategy attackStrategy = new RandomStrategy();

    /**
     * Location of the tower, if it is place on the game grid.
     */
    protected GridLocation location;


    /**
     * Default constructor for the Tower class.
     */
    public Tower() {
        this.setDetails();
        towerID = ++idCounter;
        this.logs.add("Bought the tower");
    }

    /**
     * Constructor for a Tower placed on the game grid.
     *
     * @param location Location of the tower on the game grid.
     */
    public Tower(GridLocation location) {
        this.setDetails();
        towerID = ++idCounter;
        this.location = location;
        this.logs.add("Bought and placed at " + this.location.toString());
    }

    /**
     * Sets the internal details of the tower.
     */
    protected abstract void setDetails();

    /**
     * Causes the tower to attack a critter.
     *
     * @param critters List of critters currently on the game grid.
     * @param endPoint End point of the game grid. Might be used in the targeting strategy.
     *
     * @return The location of the critter that was targeted for attack.
     */
    public abstract GridLocation attack(Collection<Critter> critters, GridLocation endPoint);

    /**
     * Gets the name of the tower.
     */
    public String getName() {
        return name;
    }

    public ArrayList<String> getLogs() {
        return this.logs;
    }

    public int getTowerID() {
        return towerID;
    }

    /**
     * Gets the path of the icon image used to represent the tower.
     *
     */
    public String getIconPath() {
        return iconPath;
    }

    /**
     * Gets the initial cost to buy the tower.
     */
    public int getInitialCost() {
        return initialCost;
    }

    /**
     * Gets the current level of the tower.
     */
    public int getLevel() {
        return level;
    }

    /**
     * Sets the current level of the tower.
     */
    public void setLevel(int level) {
        this.level = level;
    }

    public GridLocation getLocation() {
        return this.location;
    }

    /**
     * Gets the cost to upgrade the level of the tower.
     *
     */
    public int getLevelCost() {
        return levelCost;
    }

    /**
     * Gets the range of attack of the tower.
     */
    public int getRange() {
        return range;
    }

    /**
     * Gets the attack power of the tower.
     */
    public int getPower() {
        return power;
    }

    /**
     * Gets the rate of fire of the tower.
     */
    public int getRateOfFire() {
        return rateOfFire;
    }

    /**
     * Gets the attack's strategy.
     *
     * @return an object of AttackStrategy class representing the attack strategy
     */

    public AttackStrategy getAttackStrategy() {
        return this.attackStrategy;
    }

    /**
     * Sets the attack's strategy.
     *
     * @param attackStrategy an object of AttackStrategy class representing the attack strategy
     */

    public void setAttackStrategy(AttackStrategy attackStrategy) {
        this.logs.add("Setting attack strategy to " + attackStrategy.getName());
        this.attackStrategy = attackStrategy;
    }

    /**
     * Sets the attack's strategy.
     *
     * @param attackStrategy a string representing the attack's strategy
     */

    public void setAttackStrategy(String attackStrategy) {
        this.logs.add("Setting attack strategy to " + attackStrategy);
        this.attackStrategy = AttackStrategyFactory.createStrategy(attackStrategy);
    }

    /**
     * Returns a textual representation of the tower.
     */
    @Override
    public String toString() {
        return "Tower named " + this.name;
    }

    /**
     * Calculates the refund amount of the tower
     *
     * @return The refund amount of the tower.
     */
    public int refundAmout() {
        int totalCost = this.initialCost + (this.level * this.levelCost);
        return (int) (totalCost * Tower.REFUND_RATE);
    }

    /**
     * Upgrades the level of the tower by 1, and adjust its details accordingly
     */
    public void upgradeLevel() {
        this.level++;
        this.power *= 2;
        this.range *= 2;
        this.rateOfFire *= 2;
        this.logs.add("Upgraded to level " + this.level);
    }

    /**
     * Gets the special effects.
     *
     * @return a string representing the special effects
     */
    public String getSpecialEffect() {
        return this.specialEffect;
    }

    /**
     * Sets the location on the grid.
     *
     * @param gridLocation an object of GridLocation class
     */
    public void setLocation(GridLocation gridLocation) {
        this.location = gridLocation;
        this.logs.add("Placed the tower at " + this.location.toString());
    }

}
