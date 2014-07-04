package com.arnopaja.supermac.inventory;

import com.arnopaja.supermac.battle.characters.BattleCharacter;
import com.arnopaja.supermac.helpers.Parsable;
import com.arnopaja.supermac.helpers.SuperParser;
import com.arnopaja.supermac.helpers.dialogue.Dialogue;
import com.arnopaja.supermac.helpers.dialogue.DialogueStep;
import com.arnopaja.supermac.helpers.dialogue.DialogueStyle;
import com.arnopaja.supermac.helpers.load.AssetLoader;
import com.arnopaja.supermac.helpers.load.EffectParser;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Nolan Varani
 */
public class Spell implements Parsable {

    private final int id;
    private final String name;
    private final float damageModifier;
    private final int manaCost;
    private final List<Effect> effects;
    private final Powerup selfPowerup;
    private final Powerup otherPowerup;
    private final String sound;

    protected Spell(int id, String name, float damageModifier, int manaCost, List<Effect> effects, String sound) {
        this.id = id;
        this.name = name;
        this.damageModifier = damageModifier;
        this.manaCost = manaCost;
        this.effects = effects;
        this.selfPowerup = new Powerup(true, effects);
        this.otherPowerup = new Powerup(false, effects);
        this.sound = sound;
        cache.put(id, this);
    }

    public Dialogue use(BattleCharacter source, BattleCharacter destination) {
        String dialogue = source + " casts " + this + " on " + destination + "!\n";
        AssetLoader.playSound(sound);
        source.modifyMana(-manaCost);
        if (isBlack()) {
            int damage = (int) Math.ceil((getDamageModifier() / (1 + destination.getSpecial())) * (1 + source.getSpecial()));
            destination.modifyHealth(damage);
            dialogue += -damage + " damage done.";
            if (destination.isFainted()) {
                dialogue += "\n" + destination + " fell!";
            }
        } else {
            if(destination.isFainted()) {
                    dialogue = destination + " is fainted!";
                } else {
                    int healing = (int) getDamageModifier() * source.getSpecial() / 10; // This way modifiers aren't ridiculous
                    destination.modifyHealth(healing);
                    dialogue += healing + " health restored." ;
                }
        }
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
        if (source.isOutOfMana()) {
            dialogue += "<d>" + source + " is out of mana...";
        }
        return new DialogueStep(dialogue, DialogueStyle.BATTLE_CONSOLE);
    }

    public int getId() {
        return id;
    }

    public boolean isBlack() {
        return damageModifier < 0;
    }

    public boolean isWhite() {
        return damageModifier > 0;
    }

    public boolean isResurrect() {
        return otherPowerup.isResurrect();
    }

    public String getName() {
        return name;
    }

    public float getDamageModifier() {
        return damageModifier;
    }

    public int getManaCost() {
        return manaCost;
    }

    public String getSound() {
        return sound;
    }

    public void playSound() {
        AssetLoader.playSound(sound);
    }

    @Override
    public String toString() {
        return name;
    }

    // CACHE
    private static final Map<Integer, Spell> cache = new HashMap<Integer, Spell>();

    /**
     * Returns whether the specified ID is in the cache
     * @param id
     * @return
     */
    public static boolean isCached(int id) {
        return cache.containsKey(id);
    }

    /**
     * Retrieves the AbstractItem of the specified ID from the cache
     * @param id
     * @return
     */
    public static Spell getCached(int id) {
        return cache.get(id);
    }

    public static class Parser extends SuperParser<Spell> {
        @Override
        public Spell fromJson(JsonElement element) {
            JsonObject object = element.getAsJsonObject();
            int id = getInt(object, "id");
            if (isCached(id)) {
                return getCached(id);
            } else {
                String name = getString(object, "name");
                float modifier = getFloat(object, "modifier");
                int manaCost = getInt(object, "mana");
                String effect = getString(object, "effects", "");
                String sound = getString(object, "sound", null);
                return new Spell(id, name, modifier, manaCost, EffectParser.parse(effect), sound);
            }
        }

        @Override
        public JsonElement toJson(Spell object) {
            JsonObject json = new JsonObject();
            addInt(json, "id", object.getId());
            addString(json, "name", object.getName());
            addFloat(json, "modifier", object.getDamageModifier());
            addInt(json, "mana", object.getManaCost());
            addString(json, "effects", EffectParser.parse(object.effects));
            addString(json, "sound", object.getSound());
            return json;
        }
    }
}
