package com.arnopaja.supermac.battle;

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

    //get
    public static Item get(InventoryType t, int index) {
        switch(t) {
            case ITEM:
                return itemInventory.get(index);
            default:
                return null;
        }
    }
    //adds
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
