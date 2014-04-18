package com.arnopaja.supermac.battle.characters;

import com.arnopaja.supermac.helpers.SuperParser;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.List;

/**
 * TODO should this maybe implement a Collection?
 * Created by Nolan on 2/24/14.
 */
public abstract class Party<T extends BattleCharacter> {

    protected static final int MAX_SIZE = 3;

    protected final List<T> characters;

    protected Party(List<T> characters) {
        this.characters = characters;
    }
    //Characters will be added to party in different methods, as defined by their non-abstract subclasses

    public boolean isDefeated() {
        for (T character : characters) {
            if (!character.isFainted()) return false;
        }
        return true;
    }

    public BattleCharacter get(int index) {
        return characters.get(index);
    }

    public BattleCharacter getRandom() {
        // TODO: ???
        return null;
    }

    public BattleCharacter[] getBattleParty() {
        return characters.toArray(new BattleCharacter[getSize()]);
    }

    public int getSize() {
        return characters.size();
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
