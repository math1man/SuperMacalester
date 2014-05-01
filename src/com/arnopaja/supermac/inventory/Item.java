package com.arnopaja.supermac.inventory;

import com.arnopaja.supermac.battle.characters.BattleCharacter;
import com.arnopaja.supermac.helpers.SuperParser;
import com.arnopaja.supermac.helpers.dialogue.Dialogue;
import com.arnopaja.supermac.helpers.dialogue.DialogueStyle;
import com.arnopaja.supermac.helpers.dialogue.DialogueText;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * TODO: Bitmasks and effects! (parse in)
 * @author Nolan Varani
 */
public class Item extends GenericItem {

    public static enum ItemType
    {
        HEAL,DAMAGE,POWERUP_ATK,POWERUP_DEF,POWERUP_SPD,POWERUP_SPC; //Resurrect = HEAL w/ modifier = POSITIVE_INFINITY
    }
    private ItemType type;
    public ItemType getType()
    {
        return type;
    }
    public int getModifier()
    {
        return modifier;
    }
    private int modifier; //The item's effective power, regardless of what ItemType it is
    protected Item(int id, String name, int value,int m, ItemType t) {
        super(id, name, value);
        this.type = t;
        this.modifier = m;
    }
    public Dialogue use(BattleCharacter source, BattleCharacter destination) {
        // TODO: use item. Add new methods and variables to BattleCharacter to enable temporary powerups to attack, defense, speed, special
        String dialogue = source + " uses " + this + " on " + destination + "!";
        return new DialogueText(dialogue, DialogueStyle.BATTLE_CONSOLE);
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
            int mod = getInt(object, "modifier");
            //TODO: parse string version of ItemType to ItemType
            Item item = new Item(id, name, value, mod, null); //REMOVE THE NULL FOR THE FINAL VERSION!
            cache(item);
            return item;
        }

        @Override
        public JsonElement toJson(Item object) {
            JsonObject json = new JsonObject();
            addInt(json, "id", object.getId());
            addString(json, "name", object.getName());
            addInt(json, "value", object.getValue());
            addInt(json, "modifier", object.getModifier());
            addString(json, "type", object.getType().toString());
            return json;
        }
    }
}
