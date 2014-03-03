package com.arnopaja.supermac.battle;

import java.util.ArrayList;

/**
 * Created by Nolan on 2/24/14.
 */
public abstract class Party
{
    //Characters will be added to party in different methods, as defined by their non-abstract subclasses
    protected Party()
    {
        characters = new ArrayList<Character>();
    }
    protected ArrayList<Character> characters;
    public boolean isDefeated()
    {
        for (Character character : characters) {
            if (!character.isFainted()) return false;
        }
        return true;
    }
}
