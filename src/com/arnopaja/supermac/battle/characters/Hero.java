package com.arnopaja.supermac.battle.characters;

/**
 * @author Nolan Varani
 */
public class Hero extends BattleCharacter {

    // TODO: shouldn't every BattleCharacter have a BattleClass?
    private BattleClass myBattleClass;

    public Hero(String name) {
        super(name);
    }

    public void levelUp() {
        attack++;
        defense++;
        special++;
        // TODO: should speed be increased?

        // TODO: shouldn't these increase maxHealth and maxMana?
        this.health += myBattleClass.getVitality();
        if(this.mana > 0) {
            this.mana += myBattleClass.getIntelligence();
        }
    }
}
