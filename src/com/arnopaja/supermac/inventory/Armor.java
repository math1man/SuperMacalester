package com.arnopaja.supermac.inventory;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * Created by Nolan on 2/16/14.
 */
public class Armor extends AbstractItem {

    private final int defenseModifier;
    private final int equippableBitMask;
    //TODO: image definition

    public Armor(int id, String name, int value, int defenseModifier, int equippableBitMask) {
        super(id, name, value);
        this.defenseModifier = defenseModifier;
        this.equippableBitMask = equippableBitMask;
    }

    public int getDefenseModifier() { return defenseModifier; }
    public int getEquippableBitMask() { return equippableBitMask; }

    public static class Parser extends AbstractItem.Parser<Armor> {
        @Override
        public Armor convert(JsonElement element) {
            JsonObject object = element.getAsJsonObject();
            int id = object.getAsJsonPrimitive("id").getAsInt();
            String name = object.getAsJsonPrimitive("name").getAsString();
            int value = object.getAsJsonPrimitive("value").getAsInt();
            int modifier = object.getAsJsonPrimitive("modifier").getAsInt();
            int bitmask = object.getAsJsonPrimitive("bitmask").getAsInt();
            return new Armor(id, name, value, modifier, bitmask);
        }
    }
}
