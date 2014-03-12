package com.arnopaja.supermac.battle;

/**
 * Created by Envy on 2/28/14.
 */
public class EnemyParty extends Party<Enemy>
{
    public EnemyParty()
    {
        super();
    }
    public boolean containsBoss()
    {
        for (Enemy enemy : characters) {
            if (enemy.checkIfBoss()) {
                return true;
            }
        }
        return false;
    }
    @Override
    public BattleCharacter getRandom()
    {
        return(this.get(0));
    }
}
