package com.arnopaja.supermac.battle.characters;

import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Nolan Varani
 */
public class MainParty extends Party<Hero> {

    public MainParty() {
        this(new ArrayList<Hero>());
    }

    public MainParty(List<Hero> characters) {
        super(characters);
    }

//    @Override
//    public Hero getRandom()
//    {
//        return characters.get(random.nextInt(4));
////        return characters.get(0);
//    }
    public boolean partyHasFled() {
        for(Hero h:characters)
        {
            if(h.getHasFled()) return true;
        }
        return false;
    }

    public void clearHasFled() {
        for(Hero h:characters) {
            h.setHasFled(false);
        }
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

    public void restoreAll() {
        for (Hero hero : characters) {
            hero.fullRestore();
        }
    }

    public static class Parser extends Party.Parser<MainParty> {
        @Override
        protected MainParty construct(JsonObject object) {
            return new MainParty(getList(object, "characters", Hero.class));
        }
    }
}
