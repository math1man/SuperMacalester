package com.arnopaja.supermac.battle;

/**
 * Created by Envy on 2/28/14.
 */
public class BattleAction
{
    public BattleAction(Character a,Character b,ACTIONTYPE t,short index)
    {
        switch(t)
        {
            case DEFEND:
                priority = 255;
                source = a;
                destination = null;
                break;
            case FLEE:
                priority = a.getSpeed();
                source = a;
                destination = null;
                break;
            case ATTACK:
            case MAGIC:
            case ITEM:
                source = a;
                destination = b;
                priority = a.getSpeed();
                myIndex = index;
                break;
            default:
                //big error
                break;
        }
    }
    public enum ACTIONTYPE
    {
        ATTACK,MAGIC,ITEM,DEFEND,FLEE;
    }
    private Character source;
    private Character destination;
    private short priority;
    private short myIndex;
    private ACTIONTYPE type;
    public short getPriority(){return priority;}
    public Character getSource(){return source;}
    public Character getDestination(){return destination;}
    public ACTIONTYPE getType(){return type;}
}
