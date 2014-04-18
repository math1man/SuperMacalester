package com.arnopaja.supermac.battle.characters;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * @author Nolan Varani
 */
public class Hero extends BattleCharacter {

    public Hero(String name, BattleClass battleClass) {
        this(name, battleClass, 1);
    }

    public Hero(String name, BattleClass battleClass, int level) {
        this(name, battleClass, level, 1, 1);
    }

    public Hero(String name, BattleClass battleClass, int level, float fractionHealth, float fractionMana) {
        super(name, battleClass, level, fractionHealth, fractionMana);
    }

    public static class Parser extends BattleCharacter.Parser<Hero> {
        @Override
        public Hero fromJson(JsonElement element) {
            JsonObject object = element.getAsJsonObject();
            String name = getString(object, "name");
            BattleClass battleClass = getObject(object, BattleClass.class);
            int level = getInt(object, "level");
            float health = 1;
            if (object.has("health")) {
                health = getFloat(object, "health");
            }
            float mana = 1;
            if (object.has("mana")) {
                mana = getFloat(object, "mana");
            }
            return new Hero(name, battleClass, level, health, mana);
        }

        @Override
        public JsonElement toJson(Hero object) {
            JsonObject json = toBaseJson(object);
            addFloat(json, "health", object.fractionHealth);
            addFloat(json, "mana", object.fractionMana);
            return json;
        }
    }
}
