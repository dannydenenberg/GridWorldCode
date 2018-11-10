import info.gridworld.actor.Actor;
import info.gridworld.actor.Critter;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Created Nov 10, 2018: Daniel Denenberg
 * Looks at all of the neighbors within two steps of its current location. It counts the number of critters in those locations. If there are fewer than c critters,
 * this's color gets brighter, otherwise this's color gets darker. The value of c is set in the constructor.
 */
public class BlusterCritter extends Critter {
    /* Percent of the current color will be darkened by */
    /* Added: 10-31-18 */
    // lose 5% of color value in each step
    public static final double DARKENING_FACTOR = 0.05;

    private int c;

    // set the 'c' value in the constructor
    public BlusterCritter(int c)
    {
        this.c = c;
    }

    @Override
    public void processActors(ArrayList<Actor> actors) {

        ArrayList<Actor> twoStepNeighbors = this.getActors();
        // find the number of critters by filtering them out of all of the neighbor actors
        int numberOfCritters = twoStepNeighbors.stream().filter(a -> a instanceof Critter).toArray().length;

        // if the number of critters is less than c, make this brighter, otherwise, make this darker.
        if (numberOfCritters < c)
        {
            brighten();
        }
        else
        {
            darken();
        }
    }

    /**
     * Get All of the actors within two steps of this critter. That would be 24 spots
     * @return
     */
    @Override
    public ArrayList<Actor> getActors() {
        ArrayList<Actor> directNeighbors = getGrid().getNeighbors(getLocation());

        // Use a set to store all of the neighbors within two steps. You can get this by getting all of the neighbors of the neighbors withing one step and putting them into a set so there are no duplicates.
        // HashSet is the best data structure to use because it insures no duplicates.
        Set<Actor> neighborsWithinTwoSteps = new HashSet<>();

        for (Actor a : directNeighbors)
        {
            // add all of the one step neighbors
            neighborsWithinTwoSteps.add(a);

            // get all of the neighbors of the one step neighbors
            neighborsWithinTwoSteps.addAll(a.getGrid().getNeighbors(a.getLocation()));
        }

        return new ArrayList(neighborsWithinTwoSteps);
    }


    // -------------------- WEIRD INHERITANCE HAPPENING ----------------------//
    public void darken()
    {
        Color c = getColor();
        int red = (int) (c.getRed() * (1 - DARKENING_FACTOR));
        int green = (int) (c.getGreen() * (1 - DARKENING_FACTOR));
        int blue = (int) (c.getBlue() * (1 - DARKENING_FACTOR));

        setColor(new Color(red, green, blue));
    }


    public void brighten()
    {
        Color c = getColor();
        int red = (int) (c.getRed() * (1 + DARKENING_FACTOR)); // if you multiply by something over 1, it will increase the color content
        int green = (int) (c.getGreen() * (1 + DARKENING_FACTOR));
        int blue = (int) (c.getBlue() * (1 + DARKENING_FACTOR));

        setColor(new Color(red, green, blue));
    }
}
