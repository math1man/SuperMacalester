package com.arnopaja.supermac.objects;

import java.util.LinkedList;

/**
 * Created by Nolan on 2/24/14.
 */
public abstract class Party
{
    //Characters will be added to party in different methods, as defined by their non-abstract subclasses
    protected Party()
    {
        characters = new LinkedList<Character>();
    }
    protected LinkedList<Character> characters;
    public boolean isDefeated()
    {
        for(int i=0;i<characters.size();i++)
        {
            if(!characters.get(i).isFainted()) return false;
        }
        return true;
    }
}
