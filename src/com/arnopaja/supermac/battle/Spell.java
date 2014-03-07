package com.arnopaja.supermac.battle;

/**
 * Created by Envy on 2/28/14.
 */
public class Spell implements Usable
{
    private String name;
    private byte damageModifier;
    private byte universalID;
    public Spell(byte universalID)
    {
        //Generate given its universalID
    }
    public byte getDamageModifier()
    {
        return damageModifier;
    }
    public String getName()
    {
        return name;
    }
}
