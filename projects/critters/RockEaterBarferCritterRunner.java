import info.gridworld.actor.ActorWorld;
import info.gridworld.actor.Rock;
import info.gridworld.grid.Location;
import info.gridworld.world.World;

public class RockEaterBarferCritterRunner {
    public static void main(String[] args)
    {
        ActorWorld world = new ActorWorld();

        world.add(new Location(5,5), new RockEaterBarferCritter(5));
        world.add(new Location(5,6), new Rock());
        world.add(new Location(6,6), new Rock());
        world.add(new Location(6,5), new Rock());
        world.add(new Location(5,4), new Rock());
        world.add(new Location(4,6), new Rock());

        world.show();
    }
}
