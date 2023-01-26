package io.github.whiterpl.sl13.atoms.item;

import java.util.Locale;

public enum Slot {
    HEAD,
    CHEST,
    HANDS,
    LEGS,
    FEET,
    LEFT_HAND,
    RIGHT_HAND,
    BACKPACK;


    @Override
    public String toString() {

        String name = super.toString();

        name = name.replace('_', ' ').toLowerCase(Locale.ROOT);

        return name;
    }
}
