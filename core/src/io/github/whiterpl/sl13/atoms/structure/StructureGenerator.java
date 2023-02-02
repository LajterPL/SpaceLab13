package io.github.whiterpl.sl13.atoms.structure;

import io.github.whiterpl.sl13.atoms.Status;
import io.github.whiterpl.sl13.atoms.mob.Action;
import io.github.whiterpl.sl13.atoms.mob.Mob;

import java.util.function.BiConsumer;

public class StructureGenerator {

    private static StructureGenerator structureGeneratorInstance;

    private static Structure wall;
    private static Structure door;
    private static Structure stairDown;
    private static Structure stairUp;

    public static void initialize() {
        wall = new Structure("Wall", "Solid metal wall", '#', "e2e1e8");
        wall.addStatus(Status.BLOCK_PASSING);

        door = new Structure("Door", "Automatic pneumatic metal door, activated with a button", '+', "f2ee02");
        door.addStatus(Status.BLOCK_PASSING);
        door.setUsageAction((mob, door) -> {
            if(door.hasStatus(Status.BLOCK_PASSING)) {
                door.removeStatus(Status.BLOCK_PASSING);
                door.setSymbol('-');
            } else {
                door.addStatus(Status.BLOCK_PASSING);
                door.setSymbol('+');
            }
        });

        stairDown = new Structure("Staircase Down", "Emergency staircase leading down", '>', "e2e1e8");

        stairUp = new Structure("Staircase Up", "Emergency staircase leading up", '<', "e2e1e8");
    }

    public static Structure getWall() {
        return new Structure(wall);
    }

    public static Structure getDoor() {
        return new Structure(door);
    }

    public static Structure getStairUp() {
        return new Structure(stairUp);
    }

    public static Structure getStairDown() {
        return new Structure(stairDown);
    }
}
