package com.arnopaja.supermac.battle;

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
    public int use(BattleCharacter source, BattleCharacter destination) {
        // TODO: use item
        return 0;
    }
}
