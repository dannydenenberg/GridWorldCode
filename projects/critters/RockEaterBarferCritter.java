import info.gridworld.actor.Actor;
import info.gridworld.actor.Critter;
import info.gridworld.actor.Rock;
import info.gridworld.grid.Location;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created Nov 10, 2018: Daniel Denenberg.
 * To move, the critter picks a random empty location from one of its neighbors if there are no rocks.
 * If there are rocks in neighbors of the critter, it picks one, eats it by adding it to an eaten array, removes it from the board, and moves to its location.
 * If the critter has eaten 'full' number of rocks, then it begins barfing them up like this: if there is a free neighboring spot, the critter barfs the rock up to that spot (makes it visible and places
 * it in that spot -- also removes it from the eaten array) and moves to another free spot.
 *
 *
 * The getActors method will be overwritten to either return all of the neighboring rocks, OR, if there aren't any, all of the neighboring free spaces.
 * If there are no free spaces or rocks,
 */
public class RockEaterBarferCritter extends Critter {
    // saves all of the rocks which are in the critters stomach (have been eaten and not yet barfed up)
    private ArrayList<Rock> rocks;
    private int full;

    public RockEaterBarferCritter(int full)
    {
        this.full = full;
        this.rocks = new ArrayList<>();
    }

    @Override
    public ArrayList<Actor> getActors() {

    }


    @Override
    public ArrayList<Location> getMoveLocations() {
        ArrayList<Actor> neighbors = this.getGrid().getNeighbors(this.getLocation());

        // get all of the neighboring rocks
        List<Actor> rocks = neighbors.stream().filter(a -> a instanceof Rock).collect(Collectors.toList());

        // if there are no neighbors who are rocks, then return the adjacent empty spaces, otherwise return the rocks
        if (rocks.size() == 0)
        {
            return getGrid().getEmptyAdjacentLocations(getLocation());
        }
        else
        {
            ArrayList<Location> rockLocs = new ArrayList<>();
            for (Actor a: rocks)
            {
                rockLocs.add(a.getLocation());
            }
            return rockLocs;
        }
    }
}
