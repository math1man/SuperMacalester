package com.arnopaja.supermac.battle.characters;

import com.arnopaja.supermac.inventory.Armor;
import com.arnopaja.supermac.inventory.Inventory;
import com.arnopaja.supermac.inventory.Spell;
import com.arnopaja.supermac.inventory.Weapon;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Nolan Varani
 */
public abstract class BattleCharacter {

    protected final String name;
    protected Boolean isDefending;
    protected final BattleClass battleClass;
    protected int level;
    protected int maxHealth, maxMana;
    protected int attack, defense, special, speed;
    protected int currentHealth, currentMana;
    protected Armor equippedArmor;
    protected Weapon equippedWeapon;
    protected List<Spell> spellsList;
    //TODO: image definitions

    protected BattleCharacter(String name, BattleClass battleClass, int level, int currentHealth, int currentMana) {
        this.name = name;
        this.spellsList = new ArrayList<Spell>();
        this.battleClass = battleClass;
        setLevel(level);
        this.currentHealth = currentHealth == -1 ? maxHealth : currentHealth;
        this.currentMana = currentMana == -1 ? maxMana : currentMana;
        this.isDefending = false;
    }

    public void addSpells(Spell spell){
        this.spellsList.add(spell);
    }

    public void modifyHealth(int amount) {
        currentHealth += amount;
        if(currentHealth > maxHealth) currentHealth = maxHealth;
        else if(currentHealth < 0) currentHealth = 0;
    }

    public boolean isFainted() {
        return currentHealth == 0;
    }

    public void modifyMana(int amount) {
        currentMana += amount;
        if(currentMana > maxMana) currentMana = maxMana;
        else if(currentMana < 0) currentMana = 0;
    }

    public boolean isOutOfMana() {
        return currentMana == 0;
    }

    public boolean hasMana(float amount) {
        return currentMana >= amount / maxMana;
    }

    //GET
    public String getName() { return name; }
    public BattleClass getBattleClass() { return battleClass; }
    public int getLevel() { return level; }
    public float getMaxHealth() { return maxHealth; }
    public float getMaxMana() { return maxMana; }
    public int getAttack() { return attack; }
    public int getDefense() {
        if(!isDefending) return defense;
        else return defense * 2;
    }
    public int getSpecial() { return special; }
    public int getSpeed() { return speed; }
    public float getHealth() { return currentHealth; }
    public float getMana() { return currentMana; }
    public Armor getEquippedArmor() { return equippedArmor; }
    public Weapon getEquippedWeapon() { return equippedWeapon; }
    public List<Spell> getSpellsList() { return spellsList; }

    public Spell getSpell(int index) {
        return spellsList.get(index);
    }

    /**
     * Sets the specified armor if it is present in the inventory,
     * and returns the currently equipped armor to the inventory.
     * Returns whether or not the armor was equipped
     * @param a
     * @return
     */
    public boolean setEquippedArmor(Armor a) {
        if (equippedArmor != null) {
            Inventory.getMain().store(equippedArmor);
        }
        if (Inventory.getMain().take(a)) {
            equippedArmor = a;
            return true;
        }
        return false;
    }

    /**
     * Sets the specified weapon if it is present in the inventory,
     * and returns the currently equipped weapon to the inventory.
     * Returns whether or not the weapon was equipped
     * @param w
     * @return
     */
    public boolean setEquippedWeapon(Weapon w) {
        if (equippedWeapon != null) {
            Inventory.getMain().store(equippedWeapon);
        }
        if (Inventory.getMain().take(w)) {
            equippedWeapon = w;
            return true;
        }
        return false;
    }

    public void levelUp() {
        level++;
        updateStats();
    }

    public void setLevel(int level) {
        this.level = level;
        updateStats();
    }

    public void setIsDefending(Boolean a)
    {
        this.isDefending = a;
    }

    public Boolean getIsDefending()
    {
        return this.isDefending;
    }

    private void updateStats() {
        int statMultiplier = level - 1; // the starting level is level 1, but this should not increase stats
        attack = battleClass.getBaseAttack() + statMultiplier;
        defense = battleClass.getBaseDefense() + statMultiplier;
        special = battleClass.getBaseSpecial() + statMultiplier;
        speed = battleClass.getBaseSpeed();
        maxHealth = battleClass.getBaseMaxHealth() + battleClass.getVitality() * statMultiplier;
        maxMana = battleClass.getBaseMaxMana() + battleClass.getIntelligence() * statMultiplier;
    }

    @Override
    public String toString() {
        return getName();
    }
}
