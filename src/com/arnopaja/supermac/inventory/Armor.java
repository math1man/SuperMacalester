package com.arnopaja.supermac.inventory;

import com.arnopaja.supermac.helpers.SuperParser;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * @author Nolan Varani
 */
public class Armor extends GenericItem {

    private final float defenseModifier;
    //TODO: image definition

    protected Armor(int id, String name, int value, float defenseModifier) {
        super(id, name, value);
        this.defenseModifier = defenseModifier;
    }

    public float getDefenseModifier() { return defenseModifier; }

    public static class Parser extends SuperParser<Armor> {
        @Override
        public Armor fromJson(JsonElement element) {
            JsonObject object = element.getAsJsonObject();
            int id = getInt(object, "id");
            if (isCached(id, Armor.class)) {
                return getCached(id, Armor.class);
            }
            String name = getString(object, "name");
            int value = getInt(object, "value");
            float modifier = getFloat(object, "modifier");
            return new Armor(id, name, value, modifier);
        }

        @Override
        public JsonElement toJson(Armor object) {
            JsonObject json = new JsonObject();
            addInt(json, "id", object.getId());
            addString(json, "name", object.getName());
            addInt(json, "value", object.getValue());
            addFloat(json, "modifier", object.defenseModifier);
            return json;
        }
    }
}
