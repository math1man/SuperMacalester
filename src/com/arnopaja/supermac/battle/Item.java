package com.arnopaja.supermac.battle;

import com.arnopaja.supermac.helpers.Dialogue;

/**
 * Created by Nolan on 2/16/14.
 */
public class Item implements Usable
{
    //TODO: image definition
    private String name;
    private short value;
    private short equippableBitMask;
    private byte universalID;
    public Item(byte universalID)
    {
        //Generate given its universalID
    }

    @Override
    public Dialogue use(BattleCharacter source, BattleCharacter destination) {
        // TODO: use item
        String dialogue = source.getName() + " uses " + name + " on " + destination.getName() + "!";
        return new Dialogue(dialogue);
    }

    @Override
    public String toString() {
        return name;
    }
}
