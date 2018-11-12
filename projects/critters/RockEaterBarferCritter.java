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

// I haven't implemented barfing up rocks yet
public class RockEaterBarferCritter extends Critter {
    // saves all of the rocks which are in the critters stomach (have been eaten and not yet barfed up)
    private ArrayList<Actor> rocks;
    private int full;

    public RockEaterBarferCritter(int full)
    {
        this.full = full;

        // full cannot be zero
        if (full == 0)
        {
            this.full = 1;
        }


        this.rocks = new ArrayList<>();
    }

    /**
     * Not sure what to use this for
     * Returning all neighboring rocks
     * @return
     */
    @Override
    public ArrayList<Actor> getActors()
    {
        // there can be a one liner to get all of the neighboring rocks
        return new ArrayList<>(this.getGrid()
                .getNeighbors(getLocation())
                .stream()
                .filter(neighbor -> neighbor instanceof Rock)
                .collect(Collectors.toList()));
    }

    /**
     * Not sure what to use this for
     * @param actors
     */
    @Override
    public void processActors(ArrayList<Actor> actors)
    {
        //  nothing to do here right now
    }


    /**
     * Select a move location randomly
     * @param locs
     * @return
     */
    @Override
    public Location selectMoveLocation(ArrayList<Location> locs)
    {
        int locIndex = (int) (Math.random() * locs.size()); // randomly generate the location index
        return locs.get(locIndex);
    }

    @Override
    public ArrayList<Location> getMoveLocations()
    {
        ArrayList<Actor> neighbors = this.getGrid().getNeighbors(this.getLocation());

        // get all of the neighboring rocks
        List<Actor> neighboringRocks = neighbors.stream().filter(a -> a instanceof Rock).collect(Collectors.toList());

        // if there are no neighbors who are rocks, then return the adjacent empty spaces
        // or if the critter is full (the `rocks` array is as long as `full`)
        if (neighboringRocks.size() == 0 || rocks.size() > full)
        {
            return getGrid().getEmptyAdjacentLocations(getLocation());
        }
        else // otherwise return the neighboring rocks
        {
            ArrayList<Location> rockLocs = new ArrayList<>();
            for (Actor a: neighboringRocks)
            {
                rockLocs.add(a.getLocation());
            }
            return rockLocs;
        }
    }

    /**
     * If there is a rock in the location, remove it from the grid and add it to the rocks array of what this creature has eaten so far
     * Otherwise, just move to that spot on the grid
     * @param loc
     */
    @Override
    public void makeMove(Location loc)
    {
        Actor contentsOfLocation = getGrid().get(loc);


        // if there is a rock there
        if (contentsOfLocation instanceof Rock)
        {
            // add the rock to the eaten rocks
            rocks.add(contentsOfLocation);

            // JUST FOR TESTING PURPOSES
            //System.out.println("I just ate a rock at " + contentsOfLocation.getLocation());

            // remove the rock from the grid
            contentsOfLocation.removeSelfFromGrid();

            // move to the new location
            moveTo(loc);
        }
        // if the critter is full and the space is blank, then barf up a rock in the place he just was
        else if (rocks.size() > full)
        {
            // store the location where the rock will be barfed to
            Location selfLocationBeforeMove = getLocation();

            // make room for the rock
            moveTo(loc);

            // store and remove the rock the critter is barfing up from the rocks list
            Actor rockToPutInSpace = rocks.get(0);
            rocks.remove(0);

            // barf the rock in the space where the critter had moved FROM
            rockToPutInSpace.putSelfInGrid(getGrid(), selfLocationBeforeMove);

        }
        else // otherwise, if the space that the critter is moving to is blank, just move there
        {
            moveTo(loc);
        }


    }
}
