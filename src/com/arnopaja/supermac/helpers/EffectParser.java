package com.arnopaja.supermac.helpers;

import com.arnopaja.supermac.inventory.Effect;

import java.util.LinkedList;

/**
 * Created by Envy on 4/23/14.
 */
public class EffectParser
{
    public static LinkedList<Effect> parse(String parseString)
    {
        LinkedList<Effect> effectsList = new LinkedList<Effect>();
        Effect.Type myType;
        Effect.Target myTarget;
        String parseStrings[] = parseString.split(",");
        char c;
        int myValue;
        for (String s:parseStrings)
        {
            if(Character.isUpperCase(s.charAt(0))) myTarget = Effect.Target.SELF;
            else myTarget = Effect.Target.OTHER;
            c = Character.toLowerCase(s.charAt(0));
            /*
            M - mana self
            m - mana other
            H - health self
            h - health other
            S - speed self
            s - speed other
            A - attack self
            a - attack other
            D - defense self
            d - defense other
            Z - special self
            z - special other
            */
            switch(c)
            {
                case 'm':
                    myType = Effect.Type.MANA;
                    break;
                case 'h':
                    myType = Effect.Type.HEALTH;
                    break;
                case 's':
                    myType = Effect.Type.SPEED;
                    break;
                case 'a':
                    myType = Effect.Type.ATTACK;
                    break;
                case 'd':
                    myType = Effect.Type.DEFENSE;
                    break;
                case 'z':
                    myType = Effect.Type.SPECIAL;
                    break;
                default:
                    //ERROR
                    myType = null;
                    break;
            }
            myValue = Integer.parseInt(s.substring(1));
            effectsList.push(new Effect(myType,myTarget,myValue));
        }
        return effectsList;
    }
}
