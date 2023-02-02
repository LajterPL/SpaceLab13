package io.github.whiterpl.sl13.atoms.mob;

import io.github.whiterpl.sl13.Game;
import io.github.whiterpl.sl13.atoms.Direction;
import io.github.whiterpl.sl13.atoms.Status;

import java.util.function.Consumer;
import java.util.function.Predicate;

public class Action {

    private final int priority;

    private final Predicate<Mob> requirement;

    private final Consumer<Mob> actionEvent;

    public enum GenericAction {
        ATTACK,
        WALK_RANDOM
    }

    public Action(int priority, Predicate<Mob> requirement, Consumer<Mob> action) {
        this.priority = priority;
        this.requirement = requirement;
        this.actionEvent = action;
    }

    public Action(int priority, GenericAction type) {
        this.priority = priority;

        switch (type) {
            case WALK_RANDOM:
                this.requirement = m -> {
                    if (m.hasStatus(Status.PARALYZED)) return false;

                    for (Direction direction : Direction.values()) {
                        if (!m.canInteract(direction)) continue;

                        if (direction != Direction.NONE && !Game.getPlayerController().getActiveRegion()
                                .getTile(m.position.x + direction.getXMod(), m.position.y + direction.getYMod())
                                .hasStatus(Status.BLOCK_PASSING)) return true;
                    }

                    return false;

                };

                this.actionEvent = m -> {

                    boolean mobMoved = m.move(Game.getPlayerController().getActiveRegion(), Direction.getRandomDirection());

                    while(!mobMoved) {
                        mobMoved = m.move(Game.getPlayerController().getActiveRegion(), Direction.getRandomDirection());
                    }
                };
                break;
            default:
                this.requirement = null;
                this.actionEvent = null;
        }
    }

    public Action(int priority, GenericAction type, Status trigger) {
        this.priority = priority;

        switch (type) {
            case ATTACK:
                this.requirement = m -> m.sees(trigger, Game.getPlayerController().getActiveRegion());

                this.actionEvent = m -> {

                    for (Direction direction : Direction.values()) {
                        if (m.canInteract(direction) && Game.getPlayerController().getActiveRegion()
                                .getTile(m.getX() + direction.getXMod(), m.getY() + direction.getYMod())
                                .hasStatus(trigger)) {
                            m.attackMelee(Game.getPlayerController().getActiveRegion(), direction);
                            return;
                        }
                    }

                    //TODO: Pathfinding do triggera
                    m.move(Game.getPlayerController().getActiveRegion(), Direction.getRandomDirection());
                };

                break;
            default:
                this.requirement = null;
                this.actionEvent = null;
        }
    }

    public int getPriority() {
        return priority;
    }

    public boolean isPossible(Mob mob) {
        return this.requirement.test(mob);
    }

    public void execute(Mob mob) {
        this.actionEvent.accept(mob);
    }



}
