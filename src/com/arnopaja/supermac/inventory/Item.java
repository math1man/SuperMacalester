package com.arnopaja.supermac.inventory;

import com.arnopaja.supermac.battle.characters.BattleCharacter;
import com.arnopaja.supermac.helpers.SuperParser;
import com.arnopaja.supermac.helpers.dialogue.Dialogue;
import com.arnopaja.supermac.helpers.dialogue.DialogueStep;
import com.arnopaja.supermac.helpers.dialogue.DialogueStyle;
import com.arnopaja.supermac.helpers.load.AssetLoader;
import com.arnopaja.supermac.helpers.load.EffectParser;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.List;

/**
 * @author Nolan Varani
 */
public class Item extends GenericItem {

    private final List<Effect> effects;
    private final String sound;
    private Powerup selfPowerup;
    private Powerup otherPowerup;

    protected Item(int id, String name, int value, List<Effect> effects, String sound) {
        super(id, name, value);
        this.effects = effects;
        this.sound = sound;
        this.selfPowerup = new Powerup(true, effects);
        this.otherPowerup = new Powerup(false, effects);
    }

    public Dialogue use(BattleCharacter source, BattleCharacter destination) {
        String dialogue = source + " uses " + this + " on " + destination + "!";
        AssetLoader.playSound(sound);
        String temp = otherPowerup.applyTo(destination);
        if (temp.isEmpty()) {
            dialogue += "\nIt had no effect!";
        } else {
            dialogue += temp;
        }
        temp = selfPowerup.applyTo(source);
        if (!temp.isEmpty()) {
            dialogue += "<d>Side effects:" + temp;
        }
        return new DialogueStep(dialogue, DialogueStyle.BATTLE_CONSOLE);
    }

    public Powerup getSelfPowerup() {
        return selfPowerup;
    }

    public Powerup getOtherPowerup() {
        return otherPowerup;
    }

    public boolean isResurrect() {
        return otherPowerup.isResurrect();
    }

    public String getSound() {
        return sound;
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
            String effect = getString(object, "effects", "");
            String sound = getString(object, "sound", null);
            Item item = new Item(id, name, value, EffectParser.parse(effect), sound);
            cache(item);
            return item;
        }

        @Override
        public JsonElement toJson(Item object) {
            JsonObject json = new JsonObject();
            addInt(json, "id", object.getId());
            addString(json, "name", object.getName());
            addInt(json, "value", object.getValue());
            addString(json, "effects", EffectParser.parse(object.effects));
            addString(json, "sound", object.getSound());
            return json;
        }
    }
}
