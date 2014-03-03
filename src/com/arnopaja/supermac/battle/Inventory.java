package com.arnopaja.supermac.battle;import java.util.LinkedList;

/**
 * Created by Nolan on 2/16/14.
 */
public class Inventory
{
    private static LinkedList<Armor> armorInventory;
    private static LinkedList<Weapon> weaponInventory;
    private static LinkedList<Item> itemInventory;
    private static LinkedList<SpecialItem> specialItemInventory;
    public Inventory()
    {
        armorInventory = new LinkedList<Armor>();
        weaponInventory = new LinkedList<Weapon>();
        itemInventory = new LinkedList<Item>();
        specialItemInventory = new LinkedList<SpecialItem>();
    }
    //adds
    public static void add(Armor a)
    {
        armorInventory.addLast(a);
    }
    public static void add(Weapon w)
    {
        weaponInventory.addLast(w);
    }
    public static void add(Item i)
    {
        itemInventory.addLast(i);
    }
    public static void add(SpecialItem s)
    {
        specialItemInventory.addLast(s);
    }
    //checks
    public static boolean check(Armor a)
    {
        return armorInventory.contains(a);
    }
    public static boolean check(Weapon w)
    {
        return weaponInventory.contains(w);
    }
    public static boolean check(Item i)
    {
        return itemInventory.contains(i);
    }
    public static boolean check(SpecialItem s)
    {
        return specialItemInventory.contains(s);
    }
    //removes
    public static void remove(Armor a)
    {
        armorInventory.remove(a);
    }
    public static void remove(Weapon w)
    {
        weaponInventory.remove(w);
    }
    public static void remove(Item i)
    {
        itemInventory.remove(i);
    }
    public static void remove(SpecialItem s)
    {
        specialItemInventory.remove(s);
    }
}
