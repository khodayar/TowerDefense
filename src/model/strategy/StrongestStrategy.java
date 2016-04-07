package model.strategy;

import java.util.Collection;

import model.Critter;
import model.GridLocation;
import model.tower.Tower;

/**
 * Chooses the strongest (with maximum health points) critter in range to attack.
 *
 * @author Team 6
 *
 */
public class StrongestStrategy extends AttackStrategy {

    /**
     * {@inheritDoc}
     */
    @Override
    public Critter attackCritter(Tower tower, Collection<Critter> critters, GridLocation endPoint) {

        Critter target = null;
        int maximumHealth = Integer.MIN_VALUE;

        for (Critter critter : critters) {

            int distance = GridLocation.distance(tower.getLocation(), critter.gridLocation);
            if (distance > tower.getRange()) {
                continue;
            }

            if (critter.getHealthPoints() > maximumHealth) {
                maximumHealth = critter.getHealthPoints();
                target = critter;
            }
        }
        return target;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return "strongest";
    }
}
