package com.arnopaja.supermac.battle.characters;

import com.arnopaja.supermac.battle.Spell;
import com.arnopaja.supermac.inventory.Armor;
import com.arnopaja.supermac.inventory.Inventory;
import com.arnopaja.supermac.inventory.Weapon;

import java.util.List;

/**
 * Created by Nolan on 2/16/14.
 */
public abstract class BattleCharacter {

    protected final String name;
    protected float health, maxHealth, mana, maxMana;
    protected int attack, defense, special, speed;
    protected Armor equippedArmor;
    protected Weapon equippedWeapon;
    protected List<Spell> spellsList;
    //TODO: image definitions


    protected BattleCharacter(String name) {
        this.name = name;
    }

    public float modifyHealth(float amount) {
        float before = health;
        health += amount;
        if (health > maxHealth) {
            health = maxHealth;
        } else if (health < 0) {
            health = 0;
        }
        return health - before;
    }

    public float modifyMana(float amount) {
        float before = mana;
        mana += amount;
        if (mana > maxMana) {
            mana = maxMana;
        } else if (mana < 0) {
            mana = 0;
        }
        return mana - before;
        //TODO: Display mana increase/decrease as blue or orange text, respectively;
    }

    public boolean isOutOfMana() {
        return mana == 0;
    }

    public boolean hasMana(float amount) {
        return mana >= amount;
    }

    public boolean isFainted() {
        return health == 0;
    }

    //GET
    public String getName() { return name; }
    public float getHealth() { return health; }
    public float getMaxHealth() { return maxHealth; }
    public float getMana() { return mana; }
    public float getMaxMana() { return maxMana; }
    public int getAttack() { return attack; }
    public int getDefense() { return defense; }
    public int getSpecial() { return special; }
    public int getSpeed() { return speed; }
    public Armor getEquippedArmor() { return equippedArmor; }
    public Weapon getEquippedWeapon() { return equippedWeapon; }

    public Spell getSpell(int index) {
        return spellsList.get(index);
    }

    //SET
    public void setEquippedArmor(Armor a) {
        if (equippedArmor != null) {
            Inventory.getMain().store(equippedArmor);
            equippedArmor = a;
        }
        equippedArmor = a;
        Inventory.getMain().take(a);
    }

    public void setEquippedWeapon(Weapon w) {
        if (equippedWeapon != null) {
            Inventory.getMain().store(equippedWeapon);
        }
        equippedWeapon = w;
        Inventory.getMain().take(w);
    }

    @Override
    public String toString() {
        return getName();
    }
}
