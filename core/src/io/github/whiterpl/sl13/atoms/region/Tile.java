package io.github.whiterpl.sl13.atoms.region;

import io.github.whiterpl.sl13.atoms.Status;
import io.github.whiterpl.sl13.atoms.item.Item;
import io.github.whiterpl.sl13.atoms.mob.Mob;
import io.github.whiterpl.sl13.atoms.structure.Structure;

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

    public void addItem(Item item) {
        this.items.add(item);
    }

    public void removeItem(Item item) {
        this.items.remove(item);
    }

    public boolean hasStatus(Status status) {
        if (structure != null && structure.hasStatus(status)) return true;
        if (mob != null && mob.hasStatus(status)) return true;
        for (Item i : items) {
            if (i.hasStatus(status)) return true;
        }
        return false;
    }
}
