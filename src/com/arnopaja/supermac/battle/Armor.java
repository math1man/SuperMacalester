package com.arnopaja.supermac.battle;

/**
 * Created by Nolan on 2/16/14.
 */
public class Armor {

    private final int universalID;
    private final String name;
    private final int defenseModifier;
    private final int value;
    private final int equippableBitMask;
    //TODO: image definition

    public Armor(int universalID) {
        //Generate given its universalID
        this.universalID = universalID;
        this.name = null;
        this.defenseModifier = 0;
        this.value = 0;
        this.equippableBitMask = 0;
    }

    public String getName() { return name; }
    public int getDefenseModifier() { return defenseModifier; }
    public int getValue() { return value; }

    @Override
    public String toString() {
        return getName();
    }
}
