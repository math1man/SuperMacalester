package com.arnopaja.supermac.battle.characters;

import com.arnopaja.supermac.helpers.load.SuperParser;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author Nolan Varani
 */
public abstract class Party<T extends BattleCharacter> {

    protected static final Random random = new Random();

    protected final List<T> characters;

    protected Party(List<T> characters) {
        this.characters = new ArrayList<T>(characters);
    }
    //Characters will be added to party in different methods, as defined by their non-abstract subclasses

    public boolean isDefeated() {
        for (T character : characters) {
            if (!character.isFainted()) return false;
        }
        return true;
    }

    public int getIndex(BattleCharacter b)
    {
        return this.characters.indexOf(b);
    }

    public T get(int index) {
        return characters.get(index);
    }

    public T getRandom() {
        List<T> charlist = getActiveParty();
        int i = random.nextInt(charlist.size());
        return charlist.get(i);
    }

    public int size() {
        return characters.size();
    }

    public List<T> getActiveParty() {
        List<T> active = new ArrayList<T>();
        for(int i=0; i<Math.min(4, characters.size()); i++) {
            T character = characters.get(i);
            if(!character.isFainted()){
                active.add(character);
            }
        }
        return active;
    }

    public void clearDefend() {
        for (T bc : characters) {
            bc.clearDefend();
        }
    }

    public String status() {
        StringBuilder sb = new StringBuilder();
        for (T bc : getActiveParty()) {
            sb.append(bc).append(": HP: ").append(bc.getHealth())
                    .append(", MP: ").append(bc.getMana()).append("\n");
        }
        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString();
    }

    public static abstract class Parser<U extends Party> extends SuperParser<U> {
        @Override
        public U fromJson(JsonElement element) {
            JsonObject object = element.getAsJsonObject();
            return construct(object);
        }

        @Override
        public JsonElement toJson(U object) {
            JsonObject json = new JsonObject();
            addList(json, "characters", object.characters, BattleCharacter.class);
            return json;
        }

        protected abstract U construct(JsonObject object);
    }
}
