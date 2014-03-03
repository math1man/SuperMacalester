package com.arnopaja.supermac.battle;

import java.util.ArrayList;

/**
 * Created by Nolan on 2/16/14.
 */
public abstract class Character
{
    protected ArrayList<Spell> spellsList;
    protected String name;
    protected short health,maxHealth;
    protected short mana,maxMana;
    protected byte attack,defense,special,speed;
    protected Armor equippedArmor;
    protected Weapon equippedWeapon;
    //TODO: image file definitions

    //GET
    protected String getName()
    {
        return name;
    }
    protected short getHealth()
    {
        return maxHealth;
    }
    protected short getCurrentHealth()
    {
        return health;
    }
    protected short getMana()
    {
        return maxMana;
    }
    protected short getCurrentMana()
    {
        return mana;
    }
    protected short getAttack()
    {
        return attack;
    }
    protected short getDefense()
    {
        return defense;
    }
    protected short getSpecial()
    {
        return special;
    }
    protected Armor getEquippedArmor()
    {
        return equippedArmor;
    }
    protected Weapon getEquippedWeapon()
    {
        return equippedWeapon;
    }
    protected short getSpeed(){return speed;}

    //SET
    public void setEquippedArmor(Armor a)
    {
        if(equippedArmor == null)
        {
            equippedArmor = a;
        }
        else
        {
            Inventory.add(equippedArmor);
            equippedArmor = a;
            Inventory.remove(a);
        }
    }
    public void setEquippedWeapon(Weapon w)
    {
        if(equippedWeapon == null)
        {
            equippedWeapon = w;
        }
        else
        {
            Inventory.add(equippedWeapon);
            equippedWeapon = w;
            Inventory.remove(w);
        }
    }

    public void modifyHealth(short amount)
    {
        health += amount;
        if(health > maxHealth) health = maxHealth;
        else if(health < 0) health = 0;
        //TODO: Display damage taken/health healed as white or green text, respectively
    }

    public void modifyMana(short amount)
    {
        mana += amount;
        if(mana > maxMana) mana = maxMana;
        else if(mana < 0) mana = 0;
        //TODO: Display mana increase/decrease as blue or orange text, respectively;
    }

    public boolean isFainted()
    {
        return (health == 0);
    }

    public short doAttack()
    {
        //does animation
        //returns attack roll
        return 0;
    }
    public short doSpell()
    {
        //does animation
        //returns damage roll
        //decrements mana
        return 0;
    }
}
