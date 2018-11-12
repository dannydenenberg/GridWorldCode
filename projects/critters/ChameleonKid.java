import info.gridworld.actor.*;
import info.gridworld.grid.Location;


import java.util.ArrayList;
import java.util.List;

// F

/**
 * Created on Nov 9, 2018: Daniel Denenberg
 * -- IF there are no actors, darken, otherwise the color of this to be of an actor immediately in front or behind.
 *
 * Because we have to find the actors in front or behind this, all we have to do is modify the getActors function.
 *
 * We don't have to change the darken function, because it will still darken if there are no actors. WE just have to change what actors we give them.
 */
public class ChameleonKid extends ChameleonCritter {

    /* Percent of the current color will be darkened by */
    /* Added: 11-9-18 */
    // lose 0.1% of color value in each step
    public static final double DARKENING_FACTOR = 0.001;

    @Override
    public ArrayList<Actor> getActors() {
        // Get ALL of the neighbors
        ArrayList<Actor> neighbors = getGrid().getNeighbors(getLocation());

        //------------- find the neighbors immediately in front or behind ---------------//
        ArrayList<Actor> neighborsInFrontOrBehind = new ArrayList<>();

        // this current location
        Location loc = this.getLocation();

        for (Actor a : neighbors)
        {
            // get the neighbor's location
            Location neighborLocation = a.getLocation();

            // check if they have the same column as this (if they are in front or behind)
            if (neighborLocation.getCol() == loc.getCol())
            {
                // if so, add them to the list of actors
                neighborsInFrontOrBehind.add(a);
            }

        }

        // return the neighbors in front or behind
        return neighborsInFrontOrBehind;
    }
}
