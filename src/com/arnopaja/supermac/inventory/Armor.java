package com.arnopaja.supermac.inventory;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * @author Nolan Varani
 */
public class Armor extends GenericItem {

    private final float defenseModifier;
    private final int equippableBitMask;
    //TODO: image definition

    protected Armor(int id, String name, int value, float defenseModifier, int equippableBitMask) {
        super(id, name, value);
        this.defenseModifier = defenseModifier;
        this.equippableBitMask = equippableBitMask;
    }

    public float getDefenseModifier() { return defenseModifier; }
    public int getEquippableBitMask() { return equippableBitMask; }

    public static class Parser extends GenericItem.Parser<Armor> {
        @Override
        public Armor fromJson(JsonElement element) {
            JsonObject object = element.getAsJsonObject();
            int id = getInt(object, "id");
            String name = getString(object, "name");
            int value = getInt(object, "value");
            float modifier = getFloat(object, "modifier");
            int bitmask = getInt(object, "bitmask");
            return new Armor(id, name, value, modifier, bitmask);
        }

        @Override
        public JsonElement toJson(Armor object) {
            JsonObject json = toBaseJson(object);
            addFloat(json, "modifier", object.defenseModifier);
            addInt(json, "bitmask", object.equippableBitMask);
            return json;
        }
    }
}
