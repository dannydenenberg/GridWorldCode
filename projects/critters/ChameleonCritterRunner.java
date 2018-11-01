import info.gridworld.actor.ActorWorld;

import java.awt.*;

public class ChameleonCritterRunner {
    public static void main(String[] args) {
        ActorWorld world = new ActorWorld();
        world.add(new ChameleonCritter());

        world.show();
    }
}
