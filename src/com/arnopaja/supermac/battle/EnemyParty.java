package com.arnopaja.supermac.battle;

import java.util.ArrayList;

/**
 * Created by Envy on 2/28/14.
 */
public class EnemyParty extends Party<Enemy> {

    public EnemyParty() {
        super(new ArrayList<Enemy>());
    }

    public boolean containsBoss() {
        for (Enemy enemy : characters) {
            if (enemy.isBoss()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public BattleCharacter getRandom() {
        return(this.get(0));
    }
}
