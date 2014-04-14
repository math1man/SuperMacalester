package com.arnopaja.supermac.inventory;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * Created by Nolan on 2/16/14.
 */
public class Weapon extends AbstractItem {

    private final int attackModifier;
    private final int equippableBitMask;
    // TODO: image definition

    public Weapon(int universalID, String name, int value, int attackModifier, int equippableBitMask) {
        super(universalID, name, value);
        this.attackModifier = attackModifier;
        this.equippableBitMask = equippableBitMask;
    }

    public int getAttackModifier() { return attackModifier; }
    public int getEquippableBitMask() { return equippableBitMask; }

    public static class Parser extends AbstractItem.Parser<Weapon> {
        @Override
        public Weapon convert(JsonElement element) {
            JsonObject object = element.getAsJsonObject();
            int id = object.getAsJsonPrimitive("id").getAsInt();
            String name = object.getAsJsonPrimitive("name").getAsString();
            int value = object.getAsJsonPrimitive("value").getAsInt();
            int modifier = object.getAsJsonPrimitive("modifier").getAsInt();
            int bitmask = object.getAsJsonPrimitive("bitmask").getAsInt();
            return new Weapon(id, name, value, modifier, bitmask);
        }
    }
}
