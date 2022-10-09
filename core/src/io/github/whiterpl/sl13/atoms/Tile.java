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
}
