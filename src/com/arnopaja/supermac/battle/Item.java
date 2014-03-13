package com.arnopaja.supermac.battle;

import com.arnopaja.supermac.helpers.Dialogue;

/**
 * Created by Nolan on 2/16/14.
 */
public class Item implements Usable {

    private final int universalID;
    private final String name;
    private final int value;
    private final int equippableBitMask;
    // TODO: image definition

    public Item(byte universalID) {
        this.universalID = universalID;
        this.name = null;
        //Generate given its universalID
        this.value = 0;
        this.equippableBitMask = 0;
    }

    @Override
    public Dialogue use(BattleCharacter source, BattleCharacter destination) {
        // TODO: use item
        String dialogue = source + " uses " + this + " on " + destination + "!";
        return new Dialogue(dialogue);
    }

    public String getName() {
        return name;
    }

    public int getValue() {
        return value;
    }

    public int getEquippableBitMask() {
        return equippableBitMask;
    }

    @Override
    public String toString() {
        return name;
    }
}
