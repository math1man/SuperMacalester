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

    @Override
    public int use(BattleCharacter source, BattleCharacter destination) {
        System.out.println(source.getName() + " casts " + getName());
        int damage = (int) getDamageModifier() * source.getSpecial();
        damage /= (destination.getSpecial() / 4);
        System.out.println(damage + " damage done!");
        destination.modifyHealth((short) -damage);
        if(destination.isFainted()) System.out.println(destination.getName() + " fell!");
        return damage;
    }
}
