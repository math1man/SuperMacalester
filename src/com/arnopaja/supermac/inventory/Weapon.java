package com.arnopaja.supermac.inventory;

import com.arnopaja.supermac.helpers.SuperParser;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * @author Nolan Varani
 */
public class Weapon extends GenericItem {

    private final float attackModifier;
    // TODO: image definition

    protected Weapon(int universalID, String name, int value, float attackModifier) {
        super(universalID, name, value);
        this.attackModifier = attackModifier;
    }

    public float getAttackModifier() { return attackModifier; }

    public static class Parser extends SuperParser<Weapon> {
        @Override
        public Weapon fromJson(JsonElement element) {
            JsonObject object = element.getAsJsonObject();
            int id = getInt(object, "id");
            String name = getString(object, "name");
            int value = getInt(object, "value");
            float modifier = getFloat(object, "modifier");
            return new Weapon(id, name, value, modifier);
        }

        @Override
        public JsonElement toJson(Weapon object) {
            JsonObject json = new JsonObject();
            addInt(json, "id", object.getId());
            addString(json, "name", object.getName());
            addInt(json, "value", object.getValue());
            addFloat(json, "modifier", object.attackModifier);
            return json;
        }
    }
}
