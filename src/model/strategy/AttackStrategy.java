package model.strategy;

import java.util.Collection;

import model.Critter;
import model.GridLocation;
import model.tower.Tower;

/**
 * Base class for an attack strategy
 *
 * @author Team 6
 *
 */
public abstract class AttackStrategy {

    /**
     * Selects a critter to attack.
     *
     * @param towerLocation Location of the tower that intends to shoot.
     * @param critters Critters that are currently on the grid.
     * @return The critter that the tower should shoot according to the strategy.
     */
    public abstract Critter attackCritter(Tower tower, Collection<Critter> critters, GridLocation endPoint);

    /**
     * Gets the name of the attack strategy.
     *
     * @return The name of the attack strategy.
     */
    public abstract String getName();

}
