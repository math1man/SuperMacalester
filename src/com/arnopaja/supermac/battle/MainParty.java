package com.arnopaja.supermac.battle;

/**
 * Created by Envy on 2/24/14.
 */
public class MainParty extends Party
{
    //Main party can only have 3 characters
    public MainParty()
    {
        super();
    }
    public void addCharacter(Hero h)
    {
        characters.add(h);
    }
    public void swapCharacter(byte index1,byte index2)
    {
        Hero temp;
        temp = (Hero) characters.get(index1);
        characters.set(index1,characters.get(index2));
        characters.set(index2,temp);
    }
    public Hero[] getBattleParty()
    {
        Hero battleParty[] = new Hero[3];
        for(int i=0;i<3;i++) battleParty[i] = (Hero) characters.get(i);
        return battleParty;
    }
}
