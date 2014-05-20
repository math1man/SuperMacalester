package com.arnopaja.supermac.battle.characters;

import com.arnopaja.supermac.helpers.Parsable;
import com.arnopaja.supermac.inventory.*;

import java.util.Collection;

/**
 * @author Nolan Varani
 */
public abstract class BattleCharacter implements Parsable {

    protected final String name;
    protected final BattleClass battleClass;
    protected int level;
    protected int maxHealth, maxMana;
    protected int attack, defense, special, speed;
    protected int currentHealth, currentMana;
    protected Armor equippedArmor;
    protected Weapon equippedWeapon;
    protected SpellBook spellBook;
    protected boolean hasFled;
    protected boolean isDefending;
    // TODO: allow for multiple effects
    protected Effect powerup;

    protected BattleCharacter(String name, BattleClass battleClass, int level, int currentHealth, int currentMana) {
        this.name = name;
        this.battleClass = battleClass;
        setLevel(level);
        this.currentHealth = currentHealth == -1 ? maxHealth : currentHealth;
        this.currentMana = currentMana == -1 ? maxMana : currentMana;
        this.hasFled = false;
        this.isDefending = false;
        this.spellBook = new SpellBook();
        this.powerup = null;
    }

    public void addSpell(Spell spell) {
        spellBook.add(spell);
    }

    public void addSpells(Collection<Spell> spells) {
        spellBook.addAll(spells);
    }

    public void modifyHealth(int amount) {
        currentHealth += amount;
        if (currentHealth > maxHealth) {
            currentHealth = maxHealth;
        } else if (currentHealth < 0) {
            currentHealth = 0;
        }
    }

    public boolean isFainted() {
        return currentHealth == 0;
    }

    public void resurrect() {
        currentHealth = maxHealth;
    }

    public void modifyMana(int amount) {
        currentMana += amount;
        if(currentMana > maxMana) currentMana = maxMana;
        else if(currentMana < 0) currentMana = 0;
    }

    public boolean isOutOfMana() {
        return currentMana == 0;
    }

    public boolean hasMana(int amount) {
        return currentMana >= amount;
    }

    public void fullRestore() {
        currentHealth = maxHealth;
        currentMana = maxMana;
    }

    //GET
    public String getName() { return name; }
    public BattleClass getBattleClass() { return battleClass; }
    public int getLevel() { return level; }
    public int getMaxHealth() { return maxHealth; }
    public int getMaxMana() { return maxMana; }
    public int getAttack() {
        if(this.isPoweredUp() && powerup.isAttack()) return attack + powerup.value;
        else return attack;
    }
    public int getDefense() {
        int modifiedDefense = defense;
        if (isDefending) modifiedDefense *= 2;
        if (this.isPoweredUp() && powerup.isDefense()) modifiedDefense += powerup.value;
        return modifiedDefense;
    }
    public int getSpecial() {
        if(this.isPoweredUp() && powerup.isSpecial()) return special + powerup.value;
        else return special;
    }
    public int getSpeed() {
        if(this.isPoweredUp() && powerup.isSpeed()) return speed + powerup.value;
        return speed;
    }
    public int getHealth() { return currentHealth; }
    public int getMana() { return currentMana; }
    public Armor getEquippedArmor() { return equippedArmor; }
    public Weapon getEquippedWeapon() { return equippedWeapon; }
    public SpellBook getSpellBook() { return spellBook; }

    public Spell getSpell(int id) {
        return spellBook.get(id);
    }

    public boolean hasSpells() {
        return !spellBook.isEmpty();
    }

    public boolean hasEquippedArmor() {
        return equippedArmor != null;
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

    public boolean hasEquippedWeapon() {
        return equippedWeapon != null;
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

    public void flee() {
        hasFled = true;
    }

    public void clearFlee() {
        hasFled = false;
    }

    public boolean hasFled() {
        return hasFled;
    }

    public void defend() {
        isDefending = true;
    }

    public void clearDefend() {
        isDefending = false;
    }

    public boolean isDefending() {
        return isDefending;
    }

    public void setPowerup(Effect e) {
        if (e.isStatus()) this.powerup = e;
        else System.out.println("An invalid effect was passed as a powerup!");
    }

    public boolean isPoweredUp()
    {
        return powerup != null;
    }

    public Effect getPowerup()
    {
        return powerup;
    }

    public void clearPowerup()
    {
        powerup = null;
    }

    protected void updateStats() {
        int statMultiplier = level - 1; // the starting level is level 1, but this should not increase stats
        attack = battleClass.getBaseAttack() + statMultiplier;
        defense = battleClass.getBaseDefense() + statMultiplier;
        special = battleClass.getBaseSpecial() + statMultiplier;
        speed = battleClass.getBaseSpeed();
        maxHealth = battleClass.getBaseMaxHealth() + battleClass.getVitality() * statMultiplier;
        maxMana = battleClass.getBaseMaxMana() + battleClass.getIntelligence() * statMultiplier;
    }

    public String dump() {
        return "BattleCharacter{" +
                "name=" + name +
                ", health=" + currentHealth +
                ", mana=" + currentMana +
                '}';
    }

    @Override
    public String toString() {
        return getName();
    }
}
