import info.gridworld.actor.Actor;
import info.gridworld.actor.ActorWorld;
import info.gridworld.actor.Rock;
import info.gridworld.grid.Location;
import info.gridworld.world.World;

public class RockEaterBarferCritterRunner {
    public static void main(String[] args)
    {
        test1();
    }

    public static void test1()
    {
        ActorWorld world = new ActorWorld();


        for (int i = 0; i < 3; i++) {
            world.add(new RockEaterBarferCritter(((int)(Math.random() * 30 + 10))));
        }

        for (int i = 0; i < 30; i++)
        {
            world.add(new Rock());
        }

        world.show();

    }


    public static void test2()
    {
        ActorWorld world = new ActorWorld();

        world.add(new Location(2,2), new RockEaterBarferCritter(5));
        world.add(new Location(2,3), new RockEaterBarferCritter(5));

        world.show();
    }
}
