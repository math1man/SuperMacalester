package com.arnopaja.supermac.battle;

import com.arnopaja.supermac.helpers.Dialogue;

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
    public Dialogue use(BattleCharacter source, BattleCharacter destination) {
        int damage = (int) getDamageModifier() * source.getSpecial();
        damage /= (destination.getSpecial() / 4);
        destination.modifyHealth((short) -damage);
        String dialogue = source.getName() + " casts " + getName() + "!" +
                "\n" + damage + " damage done!";
        if(destination.isFainted()) {
            dialogue += "\n" + destination.getName() + " fell!";
        }
        return new Dialogue(dialogue);
    }
}
