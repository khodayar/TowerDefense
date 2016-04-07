package model.tower;

import java.util.Collection;

import model.Critter;
import model.GridLocation;

/**
 * Ice tower is a subclass of the Tower class and can be placed on the grid during the game. This tower will slow down
 * the critters.
 *
 * @author Team 6
 *
 */
public class IceTower extends Tower {

    /**
     * Default constructor for the IceTower class.
     */
    public IceTower() {
        super();
    }

    /**
     * Constructor for a IceTower placed on the game grid.
     *
     * @param location Location of the tower on the game grid.
     */
    public IceTower(GridLocation gridLocation) {
        super(gridLocation);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void setDetails() {
        this.name = "Ice tower";
        this.specialEffect = "freezing";
        this.iconPath = "icons/ModernTower.png";

        this.initialCost = 5;
        this.levelCost = 4;
        this.power = 1;
        this.range = 3;
        this.rateOfFire = 1;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public GridLocation attack(Collection<Critter> critters, GridLocation endPoint) {

        Critter critterToAttack = this.attackStrategy.attackCritter(this, critters, endPoint);

        if (critterToAttack != null) {
            critterToAttack.takeDamage(this.power, false);
            critterToAttack.freeze();
            return critterToAttack.gridLocation;
        }

        return null;

    }

}
