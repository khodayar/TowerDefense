package model;

/**
 * Class representing a game critter.
 *
 * @author Team 6
 *
 */
public class Critter {

    /**
     * Path of the image representing the critter.
     */
    public static String ICON_PATH = "icons/critter.jpg";

    /**
     * Value after which the critter moves.
     */
    public static int MOVEMENT_THRESHOLD = 100;

    /**
     * Initial health points of a new Critter.
     */
    public static int INITIAL_HEALTH_POINTS = 30;

    /**
     * Initial speed of a new Critter.
     */
    public static int INITIAL_SPEED = 30;


    /**
     * Reward for killing the critter, relative to it's health points.
     */
    public static int REWARD_RATIO = 2;

    /**
     * Additional health points a critter gains at every level.
     */
    public static int HEALTH_POINTS_PER_LEVEL = 10;

    /**
     * Additional speed a critter gains at every level.
     */
    public static int SPEED_PER_LEVEL = 10;


    public static int idCount = 0;

    public int critterID;
    public GridLocation gridLocation;

    private int healthPoints;
    private int level;
    private int burningDamage = 0;
    private int movementPoints = 0;
    private boolean isFrozen = false;

    /**
     * Constructor method for a Critter.
     *
     * @param gridLocation Location of the critter on the grid.
     * @param level Level of the critter to create.
     */
    public Critter(GridLocation gridLocation, int level) {
        this.gridLocation = gridLocation;
        this.healthPoints = Critter.HEALTH_POINTS_PER_LEVEL * level;
        this.level = level;
        this.critterID = ++idCount;
    }

    /**
     * Constructor method for a Critter, using an existing Critter instance.
     *
     * @param critter Critter instance to copy.
     */
    public Critter(Critter critter) {
        this.gridLocation = critter.gridLocation;
        this.healthPoints = Critter.HEALTH_POINTS_PER_LEVEL * critter.level;
        this.level = critter.level;
        this.critterID = critter.critterID;
    }

    /**
     * Indicates whether the critter is dead or not.
     *
     * @return true if the critter has any remaining health point, false otherwise.
     */
    public boolean isDead() {
        return this.healthPoints <= 0;
    }

    /**
     * Attacks the current critter with the specified damage. The critter's health can never go below 0.
     *
     * @param damage Damage to deal to the critter.
     * @param burning Boolean specifying if the damage is burning (if it lasts after the current turn.)
     */
    public void takeDamage(int damage, boolean burning) {
        this.healthPoints = Math.max(this.healthPoints - damage, 0);
        if (burning) {
            this.burningDamage += damage / 2;
        }
    }

    /**
     * Freezes down the current critter.
     */
    public void freeze() {
        this.isFrozen = true;
    }

    /**
     * Resets the effects associated with the critter. This is somewhat similar to making the critter turn-aware.
     */
    public void makeTurn() {
        int speed = Critter.INITIAL_SPEED + (this.level * Critter.SPEED_PER_LEVEL);
        if (this.isFrozen) {
            this.movementPoints += (speed / 2);
            this.isFrozen = false;
        } else {
            this.movementPoints += speed;
        }
        this.healthPoints = Math.max(this.healthPoints - this.burningDamage, 0);
        this.burningDamage = 0;
    }

    /**
     * Get the reward associated with killing the critter.
     *
     * @return An integer representing the reward as money.
     */
    public int getReward() {
        return Critter.INITIAL_HEALTH_POINTS + (Critter.HEALTH_POINTS_PER_LEVEL * this.level);
    }

    /**
     * Change the location of the critter.
     *
     * @param newLocation New location of the critter on the grid.
     */
    public void setLocation(GridLocation newLocation) {
        this.gridLocation = newLocation;
    }

    /**
     * Gets the health points of the critter.
     *
     * @return An integer representing the health points of the critter.
     */
    public int getHealthPoints() {
        return healthPoints;
    }

    /**
     * Gets the level.
     * 
     * @return an integer representing the speed
     */

    public int getLevel() {
        return level;
    }

    /**
     * Sets the level.
     * 
     * @param level an integer representing the speed
     */

    public void setLevel(int level) {
        this.level = level;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        String template = "Critter level %s at position %s with %s remaining health points";
        return String.format(template, this.level, this.gridLocation.toString(), this.healthPoints);
    }

    /**
     * Checks if movement threshold is reached.
     * 
     * @return true if critter can still be moved otherwise false
     */

    public boolean shouldMove() {
        return this.movementPoints >= Critter.MOVEMENT_THRESHOLD;
    }

    /**
     * Decreases the movement points by movement threshold.
     */

    public void move() {
        this.movementPoints -= Critter.MOVEMENT_THRESHOLD;
    }

    /**
     * returns the movement points.
     * 
     * @return an integer representing the movement points
     */

    public int getMovementPoints() {
        return this.movementPoints;
    }

    /**
     * Checks if the critter is frozen.
     * 
     * @return true if the critter is frozen otherwise false
     */
    public boolean isFrozen() {
        // TODO Auto-generated method stub
        return this.isFrozen;
    }

    /**
     * Returns the critter's speed.
     * 
     * @return an integer representing the critter's speed
     */

    public int getSpeed() {
        return Critter.INITIAL_SPEED + (Critter.SPEED_PER_LEVEL * this.level);
    }

}
