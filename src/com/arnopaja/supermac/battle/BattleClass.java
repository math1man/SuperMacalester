package com.arnopaja.supermac.battle;

/**
 * Created by Envy on 2/26/14.
 */
public class BattleClass
{
    public static enum CLASSTYPE
    {
        COMPSCI,ECON,NATSCI,HUMANITIES;
    }
    public static enum SPECIAL
    {
        BLACK,WHITE,RED,NONE;
    }
    private CLASSTYPE myClass;
    private SPECIAL mySpecial;
    private short baseHealth;
    private short baseMana;
    private byte baseAttack,baseDefense,baseSpecial,baseSpeed;
    //GET
    public CLASSTYPE getClassType(){return myClass;}
    public SPECIAL getSpecial(){return mySpecial;}
    public short getBaseHealth(){return baseHealth;}
    public short getBaseMana(){return baseMana;}
    public byte getBaseAttack(){return baseAttack;}
    public byte getBaseDefense(){return baseDefense;}
    public byte getBaseSpecial(){return baseSpecial;}
    public byte getVitality(){return (byte)Math.round(baseHealth/8.0);} //used for leveling up health
    public byte getIntelligence(){return (byte)Math.round(baseMana / 8.0);}
    public BattleClass(CLASSTYPE c)
    {
        myClass = c;
        switch(c)
        {
            case COMPSCI:
                baseHealth = 30;
                baseMana = 10;
                baseAttack = 5;
                baseDefense = 10;
                baseSpecial = 25;
                baseSpeed = 15;
                mySpecial = SPECIAL.BLACK;
                break;
            case ECON:
                baseHealth = 40;
                baseMana = 5;
                baseAttack = 20;
                baseDefense = 15;
                baseSpecial = 10;
                baseSpeed = 5;
                mySpecial = SPECIAL.WHITE;
                break;
            case NATSCI:
                baseHealth = 35;
                baseMana = 5;
                baseAttack = 10;
                baseDefense = 10;
                baseSpecial = 15;
                baseSpeed = 10;
                mySpecial = SPECIAL.RED;
                break;
            case HUMANITIES:
                baseHealth = 30;
                baseMana = 0;
                baseAttack = 20;
                baseDefense = 10;
                baseSpecial = 5;
                baseSpeed = 20;
                mySpecial = SPECIAL.NONE;
                break;
            default:
                //big error
                break;
        }
    }
    public static BattleClass CompSci = new BattleClass(CLASSTYPE.COMPSCI);
    public static BattleClass Econ = new BattleClass(CLASSTYPE.ECON);
    public static BattleClass NaturalScience = new BattleClass(CLASSTYPE.NATSCI);
    public static BattleClass Humanities = new BattleClass(CLASSTYPE.HUMANITIES);
}
