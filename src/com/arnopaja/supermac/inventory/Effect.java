package com.arnopaja.supermac.inventory;

/**
 * Created by Envy on 4/23/14.
 */
public class Effect
{
    Type ty;
    Target ta;
    int value;

    public Effect(Type ty, Target ta, int value)
    {
        this.ty = ty;
        this.ta = ta;
        this.value = value;
    }

    public static enum Type
    {
        HEALTH,MANA,ATTACK,DEFENSE,SPEED,SPECIAL;
    }
    public static enum Target
    {
        SELF,OTHER;
    }
}
