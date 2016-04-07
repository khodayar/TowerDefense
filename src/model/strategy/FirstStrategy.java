package model.strategy;

import java.util.Collection;

import model.Critter;
import model.GridLocation;
import model.tower.Tower;

/**
 * Chooses the first (nearest to the end point) critter in range to attack.
 *
 * @author Team 6
 *
 */
public class FirstStrategy extends AttackStrategy {

    /**
     * {@inheritDoc}
     */
    @Override
    public Critter attackCritter(Tower tower, Collection<Critter> critters, GridLocation endPoint) {

        int minimum = Integer.MAX_VALUE;
        Critter target = null;

        for (Critter critter : critters) {

            int distance = GridLocation.distance(tower.getLocation(), critter.gridLocation);

            if (distance > tower.getRange()) {
                continue;
            }

            distance = GridLocation.distance(endPoint, critter.gridLocation);

            if (distance < minimum) {
                minimum = distance;
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
        return "first";
    }

}
