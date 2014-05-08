package com.arnopaja.supermac.inventory;

import com.arnopaja.supermac.battle.characters.BattleCharacter;
import com.arnopaja.supermac.helpers.load.SuperParser;
import com.arnopaja.supermac.helpers.dialogue.Dialogue;
import com.arnopaja.supermac.helpers.dialogue.DialogueStyle;
import com.arnopaja.supermac.helpers.dialogue.DialogueText;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.Map;

/**
 * TODO: implement effects?
 * @author Nolan Varani
 */
public class Spell {

    private static enum Type { BLACK, WHITE, RESURRECT }

    private final int id;
    private final String name;
    private final float damageModifier;
    private final int manaCost;
    private final Type type;

    public Spell(int id, String name, float damageModifier, int manaCost) {
        this.id = id;
        this.name = name;
        this.damageModifier = damageModifier;
        this.manaCost = manaCost;
        if (damageModifier > 0) {
            type = Type.WHITE;
        } else if (damageModifier < 0) {
            type = Type.BLACK;
        } else {
            type = Type.RESURRECT;
        }
        cache.put(id, this);
    }

    public Dialogue use(BattleCharacter source, BattleCharacter destination) {
        // TODO: we need to check that source has enough mana
        String dialogue = source + " casts " + this + " on " + destination + "!\n";

        switch (type) {
            case BLACK:
                int damage = (int) Math.ceil((getDamageModifier() / (1 + destination.getSpecial())) * (1 + source.getSpecial()));
                destination.modifyHealth(damage);
                dialogue += damage + " damage done.";
                if (destination.isFainted()) {
                    dialogue += "\n" + destination + " fell!";
                }
                source.modifyMana(-manaCost);
                if (source.isOutOfMana()) {
                    dialogue += "<d>" + source + " is out of mana...";
                }
                break;
            case RESURRECT:
                source.modifyMana(-manaCost);
                if(destination.isFainted()) {
                    destination.resurrect();
                    dialogue += destination + " has been resurrected!" ;
                } else {
                    dialogue += "It has no effect on " + destination + "." ;
                }
                break;
            case WHITE:
                if(destination.isFainted()) {
                    dialogue = source + " cannot cast " + this + " on " + destination + " as it would have no effect!";
                } else {
                    int healing = (int) getDamageModifier() * source.getSpecial() / 10; // This way modifiers aren't ridiculous
                    destination.modifyHealth(healing);
                    dialogue += healing + " health restored." ;
                    source.modifyMana(-manaCost);
                    if (source.isOutOfMana()) {
                        dialogue += "<d>" + source + " is out of mana...";
                    }
                }
                break;
        }
        return new DialogueText(dialogue, DialogueStyle.BATTLE_CONSOLE);
    }

    public int getId() {
        return id;
    }

    public boolean isBlack() {
        return type == Type.BLACK;
    }

    public boolean isWhite() {
        return type == Type.WHITE;
    }

    public boolean isResurrect() {
        return type == Type.RESURRECT;
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
                return new Spell(id, name, modifier, manaCost);
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
