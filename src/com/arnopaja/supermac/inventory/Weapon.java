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
        public Weapon fromJson(JsonElement element) {
            JsonObject object = element.getAsJsonObject();
            int id = getInt(object, "id");
            String name = getString(object, "name");
            int value = getInt(object, "value");
            int modifier = getInt(object, "modifier");
            int bitmask = getInt(object, "bitmask");
            return new Weapon(id, name, value, modifier, bitmask);
        }

        @Override
        public JsonElement toJson(Weapon object) {
            JsonObject json = toBaseJson(object);
            addInt(json, "modifier", object.attackModifier);
            addInt(json, "bitmask", object.equippableBitMask);
            return json;
        }
    }
}
