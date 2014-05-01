package com.arnopaja.supermac.battle.characters;

/**
 * @author Nolan Varani
 */
public enum BattleClass {     // hp  m   a   d   s   speed
    COMP_SCI(SpecialType.BLACK,  30, 10, 5,  10, 25, 15),
    ECON(SpecialType.WHITE,      40, 5,  20, 15, 10, 5),
    NAT_SCI(SpecialType.RED,     35, 5,  10, 10, 15, 10),
    HUMANITIES(SpecialType.NONE, 30, 0,  20, 10, 5,  20);

    public static enum SpecialType { BLACK, WHITE, RED, NONE }

    private final SpecialType specialType;
    private final int baseMaxHealth, baseMaxMana;
    private final int baseAttack, baseDefense, baseSpecial, baseSpeed;

    private BattleClass(SpecialType specialType, int baseMaxHealth, int baseMaxMana,
              int baseAttack, int baseDefense, int baseSpecial, int baseSpeed) {
        this.specialType = specialType;
        this.baseMaxHealth = baseMaxHealth;
        this.baseMaxMana = baseMaxMana;
        this.baseAttack = baseAttack;
        this.baseDefense = baseDefense;
        this.baseSpecial = baseSpecial;
        this.baseSpeed = baseSpeed;
    }

    public SpecialType getSpecialType() {
        return specialType;
    }

    public int getBaseMaxHealth() {
        return baseMaxHealth;
    }

    public int getBaseMaxMana() {
        return baseMaxMana;
    }

    public int getBaseAttack() {
        return baseAttack;
    }

    public int getBaseDefense() {
        return baseDefense;
    }

    public int getBaseSpecial() {
        return baseSpecial;
    }

    public int getBaseSpeed() {
        return baseSpeed;
    }

    public int getVitality() {
        return (int) Math.round(baseMaxHealth / 8.0);
    }

    public int getIntelligence() {
        return (int) Math.round(baseMaxMana / 8.0);
    }
}
