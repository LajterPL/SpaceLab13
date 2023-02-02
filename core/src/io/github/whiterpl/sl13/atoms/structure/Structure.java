package io.github.whiterpl.sl13.atoms.structure;

import io.github.whiterpl.sl13.atoms.Atom;
import io.github.whiterpl.sl13.atoms.Status;
import io.github.whiterpl.sl13.atoms.mob.Mob;

import java.util.function.BiConsumer;

public class Structure extends Atom {

    private BiConsumer<Mob, Structure> usageAction;

    public Structure(String name, String description, char symbol, String colorString, Status... statuses) {
        super(name, description, symbol, colorString, statuses);
    }

    public Structure(String name, String description, char symbol, String colorString) {
        super(name, description, symbol, colorString);
    }

    public Structure(Structure copy) {
        super(copy.name, copy.description, copy.symbol, copy.colorString);
        this.usageAction = copy.usageAction;

        copy.getStatuses().forEach(status -> this.addStatus(status));
    }

    public void setUsageAction(BiConsumer<Mob, Structure> action) {
        this.usageAction = action;
    }

    public void interact(Mob mob) {
        usageAction.accept(mob, this);
    }

    public boolean hasInteraction() {
        return usageAction != null;
    }
}
