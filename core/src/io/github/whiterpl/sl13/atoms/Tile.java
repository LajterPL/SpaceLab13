package io.github.whiterpl.sl13.atoms;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Tile {
    Structure structure;
    Mob mob;
    List<Item> items;

    public Tile() {
        items = new ArrayList<>();
    }

    public Tile(Structure structure, Mob mob, Item... items) {
        this();
        this.structure = structure;
        this.mob = mob;
        this.items.addAll(Arrays.asList(items));
    }

    public Structure getStructure() {
        return structure;
    }

    public Mob getMob() {
        return mob;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setStructure(Structure structure) {
        this.structure = structure;
    }

    public void setMob(Mob mob) {
        this.mob = mob;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public boolean blocksPassage() {
        if (structure != null && structure.statuses.contains(Status.BLOCK_PASSING)) return true;
        return mob != null && mob.statuses.contains(Status.BLOCK_PASSING);
    }
}
