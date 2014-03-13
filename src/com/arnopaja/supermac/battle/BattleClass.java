package com.arnopaja.supermac.battle;

/**
 * Created by Envy on 2/26/14.
 */
public enum BattleClass {
    COMP_SCI(SpecialType.BLACK, 30, 10, 5, 10, 25, 15),
    ECON(SpecialType.WHITE, 40, 5, 20, 15, 10, 5),
    NAT_SCI(SpecialType.RED, 35, 5, 10, 10, 15, 10),
    HUMANITIES(SpecialType.NONE, 30, 0, 20, 10, 5, 20);

    public static enum SpecialType { BLACK, WHITE, RED, NONE }

    private final SpecialType specialType;
    private final int baseMaxHealth;
    private final int baseMaxMana;
    private final int baseAttack, baseDefense, baseSpecial, baseSpeed;
    private int level;

    private BattleClass(SpecialType specialType, int baseMaxHealth, int baseMaxMana,
              int baseAttack, int baseDefense, int baseSpecial, int baseSpeed) {
        this.specialType = specialType;
        this.baseMaxHealth = baseMaxHealth;
        this.baseMaxMana = baseMaxMana;
        this.baseAttack = baseAttack;
        this.baseDefense = baseDefense;
        this.baseSpecial = baseSpecial;
        this.baseSpeed = baseSpeed;
        this.level = 0; // Level starts off at 0 for math simplicity
    }

    public int getMaxHealth() {
        return round(baseMaxHealth * (1 + level / 8.0));
    }

    public int getMaxMana() {
        return round(baseMaxMana * (1 + level / 8.0));
    }

    public int getAttack() {
        return baseAttack + level;
    }

    public int getDefense() {
        return baseAttack + level;
    }

    public int getSpecial() {
        return baseAttack + level;
    }

    public int getSpeed() {
        return baseSpeed; // TODO: should speed get modified?
    }

    public int getVitality() {
        return round(getMaxHealth() / 8.0);
    }

    public int getIntelligence() {
        return round(getMaxMana() / 8.0);
    }

    public void levelUp() {
        level++;
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

    public int getBaseVitality() {
        return (int) Math.round(baseMaxHealth / 8.0);
    }

    public int getBaseIntelligence() {
        return (int) Math.round(baseMaxMana / 8.0);
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    private static int round(double value) {
        return (int) Math.round(value);
    }
}
