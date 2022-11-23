package io.github.whiterpl.sl13.atoms.mob;

import io.github.whiterpl.sl13.atoms.Status;
import io.github.whiterpl.sl13.atoms.item.Item;
import io.github.whiterpl.sl13.atoms.region.Region;
import io.github.whiterpl.sl13.atoms.region.Tile;

import java.util.*;
import java.util.stream.Stream;

public class ActionManager {

    private List<Action> priorityTable;

    public ActionManager() {
        priorityTable = new ArrayList<>();
    }

    //METHODS

    public void addAction(int priority, Status trigger, Action.Response response) {
        priorityTable.add(new Action(priority, trigger, response));
    }

    public Stream<Action> getActions(Mob mob, Region region) {
        Set<Status> foundStatuses = new HashSet<>();

        for (int x = 0; x < Region.WIDTH; x++) {
            for (int y = 0; y < Region.HEIGHT; y++) {
                if (Region.getDistance(mob.getX(), mob.getY(), x, y) <= mob.getDetectionRange()) {
                    Tile tileInRange = region.getTile(x, y);

                    if(tileInRange.getMob() != null) {
                        foundStatuses.addAll(tileInRange.getMob().getStatuses());
                    }

                    if(tileInRange.getStructure() != null) {
                        foundStatuses.addAll(tileInRange.getStructure().getStatuses());
                    }

                    for (Item item : tileInRange.getItems()) {
                        foundStatuses.addAll(item.getStatuses());
                    }
                }
            }
        }

        return priorityTable.stream()
                .filter(action -> foundStatuses.contains(action.getActionTrigger()) || action.getActionTrigger() == null);
    }
}
