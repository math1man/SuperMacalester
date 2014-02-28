package com.arnopaja.supermac.battle;

/**
 * Created by Envy on 2/24/14.
 */
public class Hero extends Character
{
    private BattleClass myBattleClass;
    public Hero()
    {
        super();
    }
    public void levelUp()
    {
        this.attack++;
        this.defense++;
        this.special++;
        this.health += myBattleClass.getVitality();
        if(this.mana > 0)
        {
            this.mana += myBattleClass.getIntelligence();
        }
    }
}
