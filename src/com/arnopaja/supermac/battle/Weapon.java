package com.arnopaja.supermac.battle; /**
 * Created by Nolan on 2/16/14.
 */
public class Weapon
{
    private final int universalID;
    private final String name;
    private final int attackModifier;
    private final int value;
    private final int equippableBitMask;
    // TODO: image definition

    public Weapon(int universalID) {
        this.universalID = universalID;
        //Generate given its universalID
        this.name = null;
        this.attackModifier = 0;
        this.value = 0;
        this.equippableBitMask = 0;
    }
}
