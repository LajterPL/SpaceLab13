package io.github.whiterpl.sl13.atoms.mob;

import io.github.whiterpl.sl13.atoms.Status;

public class Action {

    private final int priority;

    private final Status actionTrigger;

    enum Response {
        WALK,
        ATTACK,
        LOOT,
        AVOID,
        HEAL
    }

    private final Response actionResponse;

    public Action(int priority, Status trigger, Response response) {
        this.priority = priority;
        this.actionTrigger = trigger;
        this.actionResponse = response;
    }

    public int getPriority() {
        return priority;
    }

    public Status getActionTrigger() {
        return actionTrigger;
    }

    public Response getActionResponse() {
        return actionResponse;
    }
}
