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
    private Powerup powerup;

    protected Item(int id, String name, int value, List<Effect> effects, String sound) {
        super(id, name, value);
        this.effects = effects;
        this.sound = sound;
        this.powerup = new Powerup(effects);
    }

    public Dialogue use(BattleCharacter source, BattleCharacter destination) {
        AssetLoader.playSound(sound);
        String dialogue = source + " uses " + this + " on " + destination + "!";
        boolean affected = false;
        if (isResurrect() && destination.isFainted()) {
            destination.resurrect();
            dialogue += "\n" + destination + " has been resurrected!";
            affected = true;
        }
        if (isHealth()) {
            int health = powerup.getValue(Effect.Type.HEALTH);
            destination.modifyHealth(health);
            if (health > 0) {
                dialogue += "\n" + health + " health restored!";
            } else {
                dialogue += "\n" + (-health) + " damage done!";
            }
            affected = true;
        }
        if (isMana()) {
            int mana = powerup.getValue(Effect.Type.MANA);
            destination.modifyMana(mana);
            if (mana > 0) {
                dialogue += "\n" + mana + " mana restored!";
            } else {
                dialogue += "\n" + (-mana) + " mana burned!";
            }
            affected = true;
        }
        if (isStatus()) {
            dialogue += "<d>" + powerup.statusDialogue();
            destination.powerup(powerup);
            affected = true;
        }
        if (!affected) dialogue += "\nIt had no effect!";
        return new DialogueStep(dialogue, DialogueStyle.BATTLE_CONSOLE);
    }

    public List<Effect> getEffects() {
        return effects;
    }

    public boolean isHealing() {
        return powerup.getValue(Effect.Type.HEALTH) > 0;
    }

    public boolean isResurrect() {
        return powerup.isResurrect();
    }

    public boolean isHealth() {
        return powerup.has(Effect.Type.HEALTH);
    }

    public boolean isMana() {
        return powerup.has(Effect.Type.MANA);
    }

    public boolean isStatus() {
        return powerup.isStatus();
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
            addString(json, "effects", EffectParser.parse(object.getEffects()));
            return json;
        }
    }
}
