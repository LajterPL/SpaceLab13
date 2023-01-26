package io.github.whiterpl.sl13.atoms.item;

import io.github.whiterpl.sl13.atoms.Status;
import io.github.whiterpl.sl13.atoms.mob.Mob;
import io.github.whiterpl.sl13.atoms.mob.MobGenerator;
import io.github.whiterpl.sl13.atoms.region.Tile;
import io.github.whiterpl.sl13.atoms.mob.Skill;

import java.util.*;

public class Equipment {
    private final Map<Slot, Item> wornItems;
    private final List<Item> allItems;

    public Equipment() {
        this.wornItems = new EnumMap<>(Slot.class);
        this.allItems = new ArrayList<>();
    }

    // GETTERS & SETTER

    public Map<Slot, Item> getWornItems() {
        return wornItems;
    }

    public List<Item> getAllItems() {
        return allItems;
    }


    // METHODS

    public boolean takeItem(Mob mob, Item item) {

        if (!item.getLegalSlots().contains(Slot.BACKPACK)) return false;

        if (MobGenerator.calculateMaxWeightLimit(mob.getSkills()[Skill.STRENGTH.getIndex()]) - this.getTotalWeight() < item.getWeight()) return false;

        this.allItems.add(item);

        return true;
    }

    public boolean dropItem(Tile tile, Item item) {

        if (item.hasStatus(Status.CANT_DROP) || tile.hasStatus(Status.CANT_DROP)) return false;

        if (allItems.contains(item)) {
            takeOffItem(item);
            allItems.remove(item);
            tile.addItem(item);
            return true;
        }

        return false;
    }

    public static boolean wearItem(Mob mob, Item item, Slot slot) {

        if (!item.getLegalSlots().contains(slot)) return false;

        mob.getEquipment().wornItems.put(slot, item);

        return true;
    }

    public boolean wearItem(Mob mob, Item item) {

        Optional<Slot> autoSlot = item.getUsageSlots().stream()
                                .filter(s -> wornItems.get(s) == null)
                                .findFirst();

        autoSlot.ifPresent(s -> wearItem(mob, item, s));

        return autoSlot.isPresent();
    }

    public void takeOffItem(Item item) {
        wornItems.keySet().stream()
                .filter(slot -> item.getLegalSlots().contains(slot))
                .forEach(slot -> {
                    if (wornItems.get(slot) == item) {
                        wornItems.put(slot, null);
                    }
                });
    }

    public int getTotalWeight() {
        return allItems.stream().mapToInt(Item::getWeight).sum();
    }
}
