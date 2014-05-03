package com.arnopaja.supermac.inventory;

import com.arnopaja.supermac.battle.characters.BattleCharacter;
import com.arnopaja.supermac.helpers.EffectParser;
import com.arnopaja.supermac.helpers.SuperParser;
import com.arnopaja.supermac.helpers.dialogue.Dialogue;
import com.arnopaja.supermac.helpers.dialogue.DialogueStyle;
import com.arnopaja.supermac.helpers.dialogue.DialogueText;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.List;

/**
 * @author Nolan Varani
 */
public class Item extends GenericItem {

    private List<Effect> effects;

    protected Item(int id, String name, int value, List<Effect> effects) {
        super(id, name, value);
        this.effects = effects;
    }

    public Dialogue use(BattleCharacter source, BattleCharacter destination) {
        // TODO: use item. Add new methods and variables to BattleCharacter to enable temporary powerups to attack, defense, speed, special
        String dialogue = source + " uses " + this + " on " + destination + "!";
        return new DialogueText(dialogue, DialogueStyle.BATTLE_CONSOLE);
    }

    public List<Effect> getEffects() {
        return effects;
    }

    public boolean isHealing() {
        for (Effect effect : effects) {
            if (effect.isHealing()) {
                return true;
            }
        }
        return false;
    }

    public static class Parser extends SuperParser<Item> {
        @Override
        public Item fromJson(JsonElement element) {
            JsonObject object = element.getAsJsonObject();
            int id = getInt(object, "id");
            if (isCached(id, Item.class)) {
                return getCached(id, Item.class);
            }
            String name = getString(object, "name");
            int value = getInt(object, "value");
            String effect = ""; // getString(object, "effects"); TODO: update items.txt with effects
            Item item = new Item(id, name, value, EffectParser.parse(effect));
            cache(item);
            return item;
        }

        @Override
        public JsonElement toJson(Item object) {
            JsonObject json = new JsonObject();
            addInt(json, "id", object.getId());
            addString(json, "name", object.getName());
            addInt(json, "value", object.getValue());
            addString(json, "effects", EffectParser.toString(object.getEffects()));
            return json;
        }
    }
}
