package com.arnopaja.supermac.inventory;

import com.arnopaja.supermac.battle.characters.BattleCharacter;
import com.arnopaja.supermac.helpers.dialogue.Dialogue;
import com.arnopaja.supermac.helpers.dialogue.DialogueText;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * @author Nolan Varani
 */
public class Item extends GenericItem {

    private final int equippableBitMask;
    // TODO: image definition

    protected Item(int id, String name, int value, int equippableBitMask) {
        super(id, name, value);
        this.equippableBitMask = equippableBitMask;
    }

    public Dialogue use(BattleCharacter source, BattleCharacter destination) {
        // TODO: use item
        String dialogue = source + " uses " + this + " on " + destination + "!";
        return new DialogueText(dialogue);
    }

    public int getEquippableBitMask() { return equippableBitMask; }

    public static class Parser extends GenericItem.Parser<Item> {
        @Override
        public Item fromJson(JsonElement element) {
            JsonObject object = element.getAsJsonObject();
            int id = getInt(object, "id");
            String name = getString(object, "name");
            int value = getInt(object, "value");
            int bitmask = getInt(object, "bitmask");
            return new Item(id, name, value, bitmask);
        }

        @Override
        public JsonElement toJson(Item object) {
            JsonObject json = toBaseJson(object);
            addInt(json, "bitmask", object.equippableBitMask);
            return json;
        }
    }
}
