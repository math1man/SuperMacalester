package com.arnopaja.supermac.inventory;

import com.arnopaja.supermac.battle.characters.BattleCharacter;
import com.arnopaja.supermac.helpers.dialogue.DialogueText;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * Created by Nolan on 2/16/14.
 */
public class Item extends AbstractItem {

    private final int equippableBitMask;
    // TODO: image definition

    private Item(int id, String name, int value, int equippableBitMask) {
        super(id, name, value);
        this.equippableBitMask = equippableBitMask;
    }

    public DialogueText use(BattleCharacter source, BattleCharacter destination) {
        // TODO: use item
        String dialogue = source + " uses " + this + " on " + destination + "!";
        return new DialogueText(dialogue);
    }

    public int getEquippableBitMask() { return equippableBitMask; }

    public static class Parser extends AbstractItem.Parser<Item> {
        @Override
        public Item convert(JsonElement element) {
            JsonObject object = element.getAsJsonObject();
            int id = object.getAsJsonPrimitive("id").getAsInt();
            String name = object.getAsJsonPrimitive("name").getAsString();
            int value = object.getAsJsonPrimitive("value").getAsInt();
            int bitmask = object.getAsJsonPrimitive("bitmask").getAsInt();
            return new Item(id, name, value, bitmask);
        }
    }
}
