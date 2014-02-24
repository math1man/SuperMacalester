package com.arnopaja.supermac.objects;

/**
 * Created by Envy on 2/24/14.
 */
public class MainParty extends Party
{
    //Main party can only have 3 characters
    public MainParty(Hero a,Hero b,Hero c)
    {
        //null for no character
        super();
        characters.add(a);
        characters.add(b);
        characters.add(c);
    }
    public void setCharacter(byte position,Hero h)
    {
        if(position > 2 || position < 0)
        {
            //big error
        }
        else
        {
            //place h at given position in party
        }
    }
}
