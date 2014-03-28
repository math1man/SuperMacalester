package com.arnopaja.supermac.inventory;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Nolan on 2/16/14.
 */
public class Inventory {

    public static enum InventoryType { ARMOR, WEAPON, ITEM, SPECIALITEM }

    // TODO: Why do these have to be LinkedLists?
    // TODO: Why not make an interface called Retrievable and just have one List for all of these?
    private static List<Armor> armorInventory = new LinkedList<Armor>();
    private static List<Weapon> weaponInventory = new LinkedList<Weapon>();
    private static List<Item> itemInventory = new LinkedList<Item>();
    private static List<SpecialItem> specialItemInventory = new LinkedList<SpecialItem>();

    /**
     * This method has some advanced shit in it.
     * Basically what it says is "get the item from the inventory that matches
     * this specified class, and return it as the correct class".  For example,
     * if you wanted to retrieve the 3rd item (index 2) in the armor inventory,
     * you would call "get(2, Armor.class)". This would then return an instance
     * of the Armor class that is the requested armor.  The benefit of this is
     * that the returned instance will always be of the correct class.
     *
     * @param index
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T extends AbstractItem> T get(int index, Class<T> clazz) {
        if (clazz == Armor.class) {
            return clazz.cast(armorInventory.get(index));
        } else if (clazz == Weapon.class) {
            return clazz.cast(weaponInventory.get(index));
        } else if (clazz == Item.class) {
            return clazz.cast(itemInventory.get(index));
        } else if (clazz == SpecialItem.class) {
            return clazz.cast(specialItemInventory.get(index));
        }
        return null;
    }

    //adds
    public static void add(AbstractItem item) {
        if (item instanceof Armor) {
            add((Armor) item);
        } else if (item instanceof Weapon) {
            add((Weapon) item);
        } else if (item instanceof Item) {
            add((Item) item);
        } else if (item instanceof SpecialItem) {
            add((SpecialItem) item);
        }
    }

    public static void add(Armor a) {
        armorInventory.add(a);
    }

    public static void add(Weapon w) {
        weaponInventory.add(w);
    }

    public static void add(Item i) {
        itemInventory.add(i);
    }

    public static void add(SpecialItem s) {
        specialItemInventory.add(s);
    }

    //contains
    public static boolean contains(Armor a) {
        return armorInventory.contains(a);
    }

    public static boolean contains(Weapon w) {
        return weaponInventory.contains(w);
    }

    public static boolean contains(Item i) {
        return itemInventory.contains(i);
    }

    public static boolean contains(SpecialItem s) {
        return specialItemInventory.contains(s);
    }

    //removes
    public static void remove(Armor a) {
        armorInventory.remove(a);
    }

    public static void remove(Weapon w) {
        weaponInventory.remove(w);
    }

    public static void remove(Item i) {
        itemInventory.remove(i);
    }

    public static void remove(SpecialItem s) {
        specialItemInventory.remove(s);
    }
}
