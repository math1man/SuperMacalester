package com.arnopaja.supermac.battle.characters;

import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Nolan Varani
 */
public class MainParty extends Party<Hero> {

    //Main party can only have 3 characters
    public MainParty() {
        this(new ArrayList<Hero>());
    }

    protected MainParty(List<Hero> characters) {
        super(characters);
    }

    public void addCharacter(Hero h) {
        characters.add(h);
    }

    public void swapCharacter(int index1, int index2) {
        Hero temp;
        temp = characters.get(index1);
        characters.set(index1,characters.get(index2));
        characters.set(index2,temp);
    }

    public static class Parser extends Party.Parser<MainParty> {
        @Override
        protected MainParty construct(JsonObject object) {
            return new MainParty(getList(object, "characters", Hero.class));
        }
    }
}
