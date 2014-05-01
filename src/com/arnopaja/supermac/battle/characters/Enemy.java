package com.arnopaja.supermac.battle.characters;

import com.arnopaja.supermac.helpers.SuperParser;
import com.arnopaja.supermac.inventory.Item;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * @author Nolan Varani
 */
public class Enemy extends BattleCharacter {

    private final Item item;
    private final boolean isBoss;

    public Enemy(String name, BattleClass battleClass, int level, Item item, boolean isBoss) {
        super(name, battleClass, level, -1, -1);
        this.item = item;
        this.isBoss = isBoss;
    }

    public Item getItem() {
        return item;
    }

    public boolean isBoss() {
        return isBoss;
    }

    public static class Parser extends SuperParser<Enemy> {
        @Override
        public Enemy fromJson(JsonElement element) {
            JsonObject object = element.getAsJsonObject();
            String name = getString(object, "name");
            BattleClass battleClass = getObject(object, BattleClass.class);
            int level = getInt(object, "level");
            Item item = null;
            if (has(object, Item.class)) {
                item = getObject(object, Item.class);
            }
            boolean isBoss = false;
            if (object.has("boss")) {
                isBoss = getBoolean(object, "boss");
            }
            return new Enemy(name, battleClass, level, item, isBoss);
        }

        @Override
        public JsonElement toJson(Enemy object) {
            JsonObject json = new JsonObject();
            addString(json, "name", object.name);
            addObject(json, object.battleClass, BattleClass.class);
            addInt(json, "level", object.level);
            addObject(json, object.item, Item.class);
            addBoolean(json, "boss", object.isBoss);
            return json;
        }
    }
}
