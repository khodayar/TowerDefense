package model.strategy;

import java.util.Collection;

import model.Critter;
import model.GridLocation;
import model.tower.Tower;

/**
 * Chooses the weakest (with minimum health points) critter in range to attack.
 *
 * @author Team 6
 *
 */
public class WeakestStrategy extends AttackStrategy {

    /**
     * {@inheritDoc}
     */
    @Override
    public Critter attackCritter(Tower tower, Collection<Critter> critters, GridLocation endPoint) {

        System.out.println("location of the tower in attack" + tower.getLocation());
        Critter target = null;
        int minimumHealth = Integer.MAX_VALUE;

        for (Critter critter : critters) {

            int distance = GridLocation.distance(tower.getLocation(), critter.gridLocation);
            if (distance > tower.getRange()) {
                System.out.println("out of range" + tower.getRange());
                continue;
            }

            if (critter.getHealthPoints() < minimumHealth) {
                minimumHealth = critter.getHealthPoints();
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
        return "weakest";
    }

}
