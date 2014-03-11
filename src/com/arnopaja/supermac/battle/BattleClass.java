package com.arnopaja.supermac.battle;

/**
 * Created by Envy on 2/26/14.
 */
public enum BattleClass {
    COMPSCI(Special.BLACK, 30, 10, 5, 10, 25, 15),
    ECON(Special.WHITE, 40, 5, 20, 15, 10, 5),
    NATSCI(Special.RED, 35, 5, 10, 10, 15, 10),
    HUMANITIES(Special.NONE, 30, 0, 20, 10, 5, 20);

    public enum Special { BLACK, WHITE, RED, NONE }

    private final Special special;
    private final int baseHealth;
    private final int baseMana;
    private final int baseAttack, baseDefense, baseSpecial, baseSpeed;

    private BattleClass(Special special, int baseHealth, int baseMana,
              int baseAttack, int baseDefense, int baseSpecial, int baseSpeed) {
        this.special = special;
        this.baseHealth = baseHealth;
        this.baseMana = baseMana;
        this.baseAttack = baseAttack;
        this.baseDefense = baseDefense;
        this.baseSpecial = baseSpecial;
        this.baseSpeed = baseSpeed;
    }

    public Special getSpecial() {
        return special;
    }

    public int getBaseHealth() {
        return baseHealth;
    }

    public int getBaseMana() {
        return baseMana;
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
        return (int) Math.round(baseHealth / 8.0);
    }

    public int getIntelligence() {
        return (int) Math.round(baseMana / 8.0);
    }
}
