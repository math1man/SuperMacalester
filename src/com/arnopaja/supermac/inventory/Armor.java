package com.arnopaja.supermac.inventory;

/**
 * Created by Nolan on 2/16/14.
 */
public class Armor extends AbstractItem {

    private final int defenseModifier;
    private final int equippableBitMask;
    //TODO: image definition

    public Armor(int universalID) {
        // Generate given its universalID
        super(universalID, null, 0);
        this.defenseModifier = 0;
        this.equippableBitMask = 0;
    }

    public int getDefenseModifier() { return defenseModifier; }

}
