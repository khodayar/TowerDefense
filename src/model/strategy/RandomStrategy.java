package model.strategy;

import java.util.ArrayList;
import java.util.Collection;

import model.Critter;
import model.GridLocation;
import model.tower.Tower;

/**
 * Random strategy. Returns a random critter in range.
 *
 * @author Team 6
 *
 */
public class RandomStrategy extends AttackStrategy {

    /**
     * {@inheritDoc}
     */
    @Override
    public Critter attackCritter(Tower tower, Collection<Critter> critters, GridLocation endPoint) {

        ArrayList<Critter> crittersInRange = new ArrayList<Critter>();

        for (Critter critter : critters) {
            if (GridLocation.distance(tower.getLocation(), critter.gridLocation) <= tower.getRange()) {
                crittersInRange.add(critter);
            }
        }

        if (crittersInRange.isEmpty()) {
            return null;
        }

        int randomIndex = (int) (Math.random() * crittersInRange.size());
        return crittersInRange.get(randomIndex);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return "random";
    }
}
