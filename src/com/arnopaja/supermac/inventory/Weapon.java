package com.arnopaja.supermac.inventory;

/**
 * Created by Nolan on 2/16/14.
 */
public class Weapon extends AbstractItem {

    private final int attackModifier;
    private final int equippableBitMask;
    // TODO: image definition

    public Weapon(int universalID) {
        // Generate given its universalID
        super(universalID, null, 0);
        this.attackModifier = 0;
        this.equippableBitMask = 0;
    }
}
