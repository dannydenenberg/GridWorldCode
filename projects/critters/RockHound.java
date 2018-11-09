import info.gridworld.actor.Actor;
import info.gridworld.actor.Critter;
import info.gridworld.actor.Rock;

import java.util.ArrayList;

/**
 * Created on Nov 9. 2018: Daniel Denenberg
 * RockHound -- moves like a critter but removes any neighbors who are rocks.
 *
 * -- All we have to do is change the processActors method to remove Actors that are instances of the Rock class
 */
public class RockHound extends Critter {

    @Override
    public void processActors(ArrayList<Actor> actors) {
        for (Actor a : actors)
        {
            // if the neighbor is an instance of the class Rock, remove it from the grid
            if (a instanceof Rock)
            {
                a.removeSelfFromGrid();
            }
        }
    }
}
