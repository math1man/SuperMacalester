package com.arnopaja.supermac.inventory;

import com.arnopaja.supermac.battle.characters.BattleCharacter;
import com.arnopaja.supermac.helpers.SuperParser;
import com.arnopaja.supermac.helpers.dialogue.Dialogue;
import com.arnopaja.supermac.helpers.dialogue.DialogueStyle;
import com.arnopaja.supermac.helpers.dialogue.DialogueText;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Nolan Varani
 */
public class Spell {

    private final int id;
    private final String name;
    private final float damageModifier;
    private final float manaCost;

    public Spell(int id, String name, float damageModifier, float manaCost) {
        this.id = id;
        this.name = name;
        this.damageModifier = damageModifier;
        this.manaCost = manaCost;
        cache.put(id, this);
    }

    public Dialogue use(BattleCharacter source, BattleCharacter destination) {
        float damage = getDamageModifier() / (destination.getSpecial() / 4) * source.getSpecial();
        int damageDone = (int) destination.modifyHealth(-damage);
        String dialogue = source + " casts " + this + "!\n" +
                damageDone + " damage done.";
        if (destination.isFainted()) {
            dialogue += "\n" + destination + " fell!";
        }
        source.modifyMana(manaCost);
        dialogue += "<d>" + source + " has  " + source.getMana() + " mana.";
        if (source.isOutOfMana()) {
            dialogue += "\n" + source + " is out of mana...";
        }
        return new DialogueText(dialogue, DialogueStyle.BATTLE_CONSOLE);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public float getDamageModifier() {
        return damageModifier;
    }

    public float getManaCost() {
        return manaCost;
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
                float manaCost = getFloat(object, "mana");
                return new Spell(id, name, modifier, manaCost);
            }
        }

        @Override
        public JsonElement toJson(Spell object) {
            JsonObject json = new JsonObject();
            addInt(json, "id", object.id);
            addString(json, "name", object.name);
            addFloat(json, "modifier", object.damageModifier);
            addFloat(json, "mana", object.manaCost);
            return json;
        }
    }
}
