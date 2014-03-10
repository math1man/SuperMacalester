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
        characters = new ArrayList<BattleCharacter>();
    }
    protected ArrayList<BattleCharacter> characters;
    public boolean isDefeated()
    {
        for (BattleCharacter character : characters) {
            if (!character.isFainted()) return false;
        }
        return true;
    }
    public BattleCharacter get(int index)
    {
        return characters.get(index);
    }
    public BattleCharacter getRandom()
    {
        return null;
    }
}
