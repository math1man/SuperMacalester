package com.arnopaja.supermac.battle.characters;

import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Nolan Varani
 */
public class EnemyParty extends Party<Enemy> {

    public EnemyParty() {
        this(new ArrayList<Enemy>());
    }

    protected EnemyParty(List<Enemy> characters) {
        super(characters);
    }

    public boolean containsBoss() {
        for (Enemy enemy : characters) {
            if (enemy.isBoss()) {
                return true;
            }
        }
        return false;
    }

    public static class Parser extends Party.Parser<EnemyParty> {
        @Override
        protected EnemyParty construct(JsonObject object) {
            return new EnemyParty(getList(object, "characters", Enemy.class));
        }
    }
}
