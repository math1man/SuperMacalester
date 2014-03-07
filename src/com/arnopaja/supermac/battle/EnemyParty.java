package com.arnopaja.supermac.battle;

/**
 * Created by Envy on 2/28/14.
 */
public class EnemyParty extends Party
{
    public EnemyParty()
    {
        super();
    }
    public boolean containsBoss()
    {
        Enemy temp;
        for(int i=0;i<characters.size();i++)
        {
            temp = (Enemy) characters.get(i);
            if(temp.checkIfBoss())
            {
                return true;
            }
        }
        return false;
    }
    public byte getSize()
    {
        return (byte) characters.size();
    }
    @Override
    public Character getRandom()
    {
        return(this.get(0));
    }
}
