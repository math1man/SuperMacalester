package com.arnopaja.supermac.battle;

import java.util.ArrayList;

/**
 * Created by Envy on 2/24/14.
 */
public class MainParty extends Party<Hero> {

    //Main party can only have 3 characters
    public MainParty() {
        super(new ArrayList<Hero>());
    }

    public void addCharacter(Hero h) {
        characters.add(h);
    }

    public void swapCharacter(byte index1,byte index2) {
        Hero temp;
        temp = characters.get(index1);
        characters.set(index1,characters.get(index2));
        characters.set(index2,temp);
    }
}
