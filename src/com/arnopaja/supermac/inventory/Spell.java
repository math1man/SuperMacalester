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
    private final int manaCost;
    private boolean isBlack; //TRUE = BLACK, FALSE = WHITE

    public Spell(int id, String name, float damageModifier, int manaCost, boolean offensive) {
        this.id = id;
        this.name = name;
        this.damageModifier = damageModifier;
        this.manaCost = manaCost;
        this.isBlack = offensive;
        cache.put(id, this);
    }

    public Dialogue use(BattleCharacter source, BattleCharacter destination) {
        int damage;
        String dialogue;
        if(isBlack)
        {
            damage = (int) getDamageModifier() / (destination.getSpecial() / 4) * source.getSpecial();
            destination.modifyHealth(-damage);
            dialogue = source + " casts " + this + "!\n" +
                    damage + " damage done.";
            if (destination.isFainted()) {
                dialogue += "\n" + destination + " fell!";
            }
            source.modifyMana(-manaCost);
            dialogue += "<d>" + source + " has  " + source.getMana() + " mana.";
            if (source.isOutOfMana()) {
                dialogue += "\n" + source + " is out of mana...";
            }
        }
        else
        {
            //TODO: Make sure people can't come back from the dead
            damage = (int) getDamageModifier() * source.getSpecial();
            destination.modifyHealth(damage);
            dialogue = source + " casts " + this + "!\n" +
                    damage + " health restored to " + destination.getName() + "." ;
            source.modifyMana(-manaCost);
            dialogue += "<d>" + source + " has  " + source.getMana() + " mana.";
            if (source.isOutOfMana()) {
                dialogue += "\n" + source + " is out of mana...";
            }
        }
        return new DialogueText(dialogue, DialogueStyle.BATTLE_CONSOLE);
    }

    public int getId() {
        return id;
    }

    public boolean isBlack() { return isBlack; }

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
                int manaCost = getInt(object, "mana");
                boolean offensive = getBoolean(object, "type");
                return new Spell(id, name, modifier, manaCost,offensive);
            }
        }

        @Override
        public JsonElement toJson(Spell object) {
            JsonObject json = new JsonObject();
            addInt(json, "id", object.id);
            addString(json, "name", object.name);
            addFloat(json, "modifier", object.damageModifier);
            addInt(json, "mana", object.manaCost);
            return json;
        }
    }
}
