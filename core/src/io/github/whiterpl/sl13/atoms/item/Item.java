package io.github.whiterpl.sl13.atoms.item;

import io.github.whiterpl.sl13.atoms.Atom;
import io.github.whiterpl.sl13.atoms.Status;
import io.github.whiterpl.sl13.atoms.mob.Action;

import java.util.HashSet;
import java.util.Set;

public class Item extends Atom {

    private Set<Slot> legalSlots;
    private Set<Slot> usageSlots;
    private Action usageAction;
    private Action wearAction;
    private Action unwearAction;
    private int weight;

    public Item(String name, String description, char symbol, String colorString, Status... statuses) {
        super(name, description, symbol, colorString, statuses);
        this.legalSlots = new HashSet<>();
        this.usageSlots = new HashSet<>();
        this.weight = 0;
    }

    public Item(String name, String description, char symbol, String colorString) {
        super(name, description, symbol, colorString);
        this.legalSlots = new HashSet<>();
        this.usageSlots = new HashSet<>();
        this.weight = 0;
    }

    // GETTERS AND SETTERS

    public Set<Slot> getLegalSlots() {
        return legalSlots;
    }

    public void setLegalSlots(Set<Slot> legalSlots) {
        this.legalSlots = legalSlots;
    }

    public void addLegalSlot(Slot slot) {
        this.legalSlots.add(slot);
    }

    public Set<Slot> getUsageSlots() {
        return usageSlots;
    }

    public void setUsageSlots(Set<Slot> usageSlots) {
        this.usageSlots = usageSlots;
    }

    public void addUsageSlot(Slot slot) {
        this.usageSlots.add(slot);
    }

    public Action getUsageAction() {
        return usageAction;
    }

    public void setUsageAction(Action usageAction) {
        this.usageAction = usageAction;
    }

    public Action getWearAction() {
        return wearAction;
    }

    public void setWearAction(Action wearAction) {
        this.wearAction = wearAction;
    }

    public Action getUnwearAction() {
        return unwearAction;
    }

    public void setUnwearAction(Action unwearAction) {
        this.unwearAction = unwearAction;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    // METHODS


}
