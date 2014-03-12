package com.arnopaja.supermac.battle;

import java.util.ArrayList;

/**
 * Created by Nolan on 2/24/14.
 */
public abstract class Party<T extends BattleCharacter>
{
    protected ArrayList<T> characters;
    //Characters will be added to party in different methods, as defined by their non-abstract subclasses
    protected Party()
    {
        characters = new ArrayList<T>();
    }
    public boolean isDefeated()
    {
        for (T character : characters) {
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

    public BattleCharacter[] getBattleParty()
    {
        BattleCharacter battleParty[] = new BattleCharacter[3];
        for(int i=0;i<3;i++) battleParty[i] = characters.get(i);
        return battleParty;
    }

    public int getSize()
    {
        int c = 0;
        BattleCharacter characters[] = getBattleParty();
        for(BattleCharacter hero : characters)
        {
            if(hero != null) c++;
        }
        return c;
    }
}
