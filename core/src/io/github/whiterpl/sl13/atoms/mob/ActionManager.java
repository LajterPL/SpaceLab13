package io.github.whiterpl.sl13.atoms.mob;

import io.github.whiterpl.sl13.atoms.Status;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ActionManager {

    private final List<Action> priorityTable;

    public ActionManager() {
        priorityTable = new ArrayList<>();
    }

    //METHODS

    public void addAction(int priority, Predicate<Mob> requirement, Consumer<Mob> action) {
        priorityTable.add(new Action(priority, requirement, action));
    }

    public void addAction(int priority, Action.GenericAction genericAction) {
        priorityTable.add(new Action(priority, genericAction));
    }

    public void addAction(int priority, Action.GenericAction genericAction, Status status) {
        priorityTable.add(new Action(priority, genericAction, status));
    }

    public void addAction(Action action) {
        priorityTable.add(action);
    }

    public List<Action> getPossibleActions(Mob mob) {
        return priorityTable.stream().filter(a -> a.isPossible(mob)).collect(Collectors.toList());
    }
}
