import info.gridworld.actor.Actor;
import info.gridworld.actor.Critter;
import info.gridworld.actor.Rock;
import info.gridworld.grid.Location;

import java.util.ArrayList;
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
    private ArrayList<Location> previousLocations;
    private static int NUMBER_OF_REPEATED_LOCATIONS_MAX = 10;
    private int numberOfRepeatedLocationsChecks = 0;

    public RockEaterBarferCritter(int full)
    {
        this.full = full;

        // full cannot be zero
        if (full == 0)
        {
            this.full = 1;
        }


        this.rocks = new ArrayList<>();
        this.previousLocations = new ArrayList<>();
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
     * Returns true if the critter has been going back and forth between the same locations
     * @return
     */
    private boolean hasBeenGoingInSameLocations()
    {
        // make sure we aren't storing too much data on the locations
        numberOfRepeatedLocationsChecks++;
        // if it exceeds a certain number, reset it
        if (numberOfRepeatedLocationsChecks > NUMBER_OF_REPEATED_LOCATIONS_MAX)
        {
            numberOfRepeatedLocationsChecks = 0;
            return false;
        }

        // if there is no data, then return false
        if (this.previousLocations.size() == 0)
        {
            return false;
        }

        int numberOfRepeatedLocations = 0;

        // see how many of the locations were repeated (excluding same references)
        for (Location loc1 : this.previousLocations)
        {
            for (Location loc2 : this.previousLocations)
            {
                if (loc1 == loc2) // if they are the same object reference, don't count this iteration
                {
                    continue;
                }

                // if they represent the same object, they are the same location
                if (loc1.equals(loc2))
                {
                    numberOfRepeatedLocations++;
                }
            }
        }

        // if the number of repeated locations is high enough, then it has been going to the same places over and over again
        if (numberOfRepeatedLocations >= NUMBER_OF_REPEATED_LOCATIONS_MAX)
        {
            // reset the locations counter
            this.previousLocations = new ArrayList<>();
            numberOfRepeatedLocations = 0;
            return true;
        }
        else
        {
            return false;
        }
    }



    /**
     * Select a move location by choosing which location is closest to another RockEaterBarferCritter.
     * If there are no other RockEaterBarferCritter creatures on the board, then this will try to move to the next possible spot closest to the closest piece of food (rock)
     * @param locs
     * @return
     */
    @Override
    public Location selectMoveLocation(ArrayList<Location> locs)
    {
        if (locs.size() == 0)
        {
            return null;
        }

        // if it has been going back and forth in the same move locations for a while now, the transport it to a new random location and remove the Actor in that location
        if (hasBeenGoingInSameLocations())
        {
            // just a variable to hold the random location to be transported to
            Location randomLocationOnGrid = new Location(0,0);
            boolean haveFoundFreeSpot = false;
            // Make sure that the random location CANNOT BE THE LOCATION OF THIS CREATURE OR ANOTHER CREATURE
            // Picks a random location on the grid
            // remember to put the -1 after getNumRows, because when you get a location on the grid, it is numbered 0 - (# of rows - 1)
            while (!haveFoundFreeSpot) {
                randomLocationOnGrid = new Location((int) (Math.random() * (getGrid().getNumRows() - 1)), (int) (Math.random() * (getGrid().getNumCols() - 1)));
                if (getGrid().get(randomLocationOnGrid) instanceof RockEaterBarferCritter)
                {
                    continue;
                }
                else
                {
                    haveFoundFreeSpot = true;
                }
            }

            // remove the actor already in that location
            if (getGrid().get(randomLocationOnGrid) != null)
            {
                getGrid().get(randomLocationOnGrid).removeSelfFromGrid();
            }
            return randomLocationOnGrid;
        }




        /* -------------------------------------------- GET VARIABLES IMPORTANT TO INTELLIGENCE ---------------------------------------------*/
        // get all of the other same critters on board
        ArrayList<Actor> otherRockEaterBarferCrittersOnGrid = new ArrayList<>();
        ArrayList<Actor> otherRocksOnGrid = new ArrayList<>();

        // compile a list of all Rocks on the grid and all RockEaterBarferCritters on the grid
        for (Location loc : getGrid().getOccupiedLocations())
        {
            Actor actorInLocation = getGrid().get(loc);
            // if it is an instance of the class and it is not this object (it is another critter)
            if (actorInLocation instanceof RockEaterBarferCritter && !actorInLocation.equals(this))
            {
                otherRockEaterBarferCrittersOnGrid.add(actorInLocation);
            }
            else if (actorInLocation instanceof Rock)
            {
                otherRocksOnGrid.add(actorInLocation);
            }
        }





        /**
         * if there are no other RockEaterBarferCritters on the grid, pick the location of the nearest piece of food
         */
        if (otherRockEaterBarferCrittersOnGrid.size() == 0)
        {
            // if there are no other rocks on the grid, choose a free space at random
            if (otherRocksOnGrid.size() == 0)
            {
                int locIndex = (int) (Math.random() * locs.size()); // randomly generate the location index
                return locs.get(locIndex);
            }
            // otherwise choose the location of the spot that will bring this closest to the closest rock
            else
            {
                // compile a list of all of the locations of the rocks on the grid
                ArrayList<Location> otherRockLocations = new ArrayList<>(otherRocksOnGrid.stream()
                        .map(Actor::getLocation)
                        .collect(Collectors.toList()));

                // find the location of the nearest rock of all of the rocks on the grid
                double smallestDistanceToRock = 100000000.0;
                Location closestRockToSelf = otherRockLocations.get(0);
                for (Location rockLoc : otherRockLocations)
                {
                    if (distanceApartFrom(rockLoc) < smallestDistanceToRock)
                    {
                        smallestDistanceToRock = distanceApartFrom(rockLoc);
                        closestRockToSelf = rockLoc;
                    }
                }


                // find one of the possible locations that is closest to the closest rock
                double smallestDistanceFromNewSpotToClosestRock = 1000000000.0;
                Location closestToClosestRock = locs.get(0);
                for (Location location : locs)
                {
                    if (distanceBetween(location, closestRockToSelf) < smallestDistanceFromNewSpotToClosestRock)
                    {
                        smallestDistanceFromNewSpotToClosestRock = distanceBetween(location, closestRockToSelf);
                        closestToClosestRock = location;
                    }
                }

                return closestToClosestRock;
            }
        }

        // choose the location that will bring this closest to the nearest RockEaterBarferCritter on the grid
        else
        {
            // find which of the other same critters is closest
            // find which of the possible move locations are closest to the closest critter

            // dist = sqrt((x-x2)(x-x2) + (y-y2)(y-y2)
            double smallestDistance = 10000000.0;
            Actor closestBarfingCritter = new Actor();
            for (Actor a : otherRockEaterBarferCrittersOnGrid)
            {
                // if the distance from this actor is smaller than smallest, then that is the new actor that is closest
                double testingDistance = this.distanceApartFrom(a.getLocation());
                if (testingDistance < smallestDistance)
                {
                    smallestDistance = testingDistance;
                    closestBarfingCritter = a;
                }
            }


            smallestDistance = 10000000.0;
            Location smallestDistanceLocationMove = locs.get(0);
            // for each of the possible move spaces, pick the one that brings this closest to the closest actor
            for (Location loc : locs)
            {
                // find the distance from the location that this would move to to the closest critter from the current location of this critter
                double distanceFromCritter = Math.sqrt(Math.pow(closestBarfingCritter.getLocation().getRow() - loc.getRow(),2.0)
                        + Math.pow(closestBarfingCritter.getLocation().getCol() - loc.getCol(),2.0));
                if (distanceFromCritter < smallestDistance)
                {
                    smallestDistance = distanceFromCritter;
                    smallestDistanceLocationMove = loc;
                }
            }

            return smallestDistanceLocationMove;
        }
    }




    /**
     * Returns the distance between two locations (this and another)
     * @param other
     * @return
     */
    private double distanceApartFrom(Location other)
    {
        return Math.sqrt(Math.pow(this.getLocation().getRow() - other.getRow(), 2.0) + Math.pow(this.getLocation().getCol() - other.getCol(), 2.0));
    }


    private double distanceBetween(Location first, Location other)
    {
        return Math.sqrt(Math.pow(first.getRow() - other.getRow(), 2.0) + Math.pow(first.getCol() - other.getCol(), 2.0));
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
        // don't move if there is no where to move to
        if (loc == null)
        {
            return;
        }

        // store the previous locations
        previousLocations.add(loc);

        // TODO: grid appears to be null after a while......
        if (getGrid() == null)
        {
            return;
        }

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
