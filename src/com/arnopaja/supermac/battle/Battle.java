package com.arnopaja.supermac.battle;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Random;

/**
 * Created by Envy on 2/28/14.
 */
public class Battle
{
    private Texture backgroundTex;
    private Sprite backgroundSprite;
    private SpriteBatch spriteBatch;
    private boolean isBossFight;
    private MainParty mainParty;
    private EnemyParty enemyParty;
    private PriorityQueue<BattleAction> perTurnQueue;
    private Random battleRandomGen = new Random();
    public Battle(MainParty heroes,EnemyParty enemies,byte location)
    {
        perTurnQueue = new PriorityQueue<BattleAction>(heroes.getSize() + enemies.getSize(), new Comparator<BattleAction>() {
            public int compare(BattleAction a, BattleAction b) {
                // compare n1 and n2
                return a.getPriority() < b.getPriority() ? -1 : a.getPriority() == b.getPriority() ? 0 : 1;
            }
        });
        isBossFight = enemies.containsBoss();
    }
    private void getBackground(byte location)
    {
        //load from hashmap in final
    }
    public boolean doBattle()
    {
        //set scene
        while(!mainParty.isDefeated())
        {
            setTurnActions();
            Integer temp;
            for(BattleAction ba : perTurnQueue)
            {
                switch(ba.getType())
                {
                    case ATTACK:
                        System.out.println(ba.getSource().getName() + " attacks " + ba.getDestination().getName());
                        temp = (int) (ba.getSource().getAttack() * (2 + Math.abs(battleRandomGen.nextGaussian())));
                        temp /= ba.getDestination().getDefense();
                        System.out.println(temp.toString() + " damage done!");
                        ba.getDestination().modifyHealth((short)-temp);
                        if(ba.getDestination().isFainted()) System.out.println(ba.getDestination().getName() + " fell!");
                        break;
                    case DEFEND:
                        //temporarily increase defense
                        System.out.println(ba.getSource().getName() + " is defending.");
                        break;
                    case MAGIC:
                        Spell s = (Spell) ba.getMyUsable();
                        if(s != null)
                        {
                            System.out.println(ba.getSource().getName() + " casts " + s.getName());
                            temp = (int) s.getDamageModifier() * ba.getSource().getSpecial();
                            temp /= (ba.getDestination().getSpecial() / 4);
                            System.out.println(temp.toString() + " damage done!");
                            ba.getDestination().modifyHealth((short)-temp);
                            if(ba.getDestination().isFainted()) System.out.println(ba.getDestination().getName() + " fell!");
                        }
                        break;
                    case ITEM:
                        Item o = (Item) ba.getMyUsable();
                        //use item
                        break;
                    default:
                        //big error
                        break;
                }
            }
        }
        return false;
    }
    public void setTurnActions()
    {
        Enemy e;
        Hero heroes[] = mainParty.getBattleParty();
        //allow character actions to be set
        for(Hero h : heroes)
        {
            perTurnQueue.add(new BattleAction((Character) h,enemyParty.get(battleRandomGen.nextInt(enemyParty.getSize())),BattleAction.ACTIONTYPE.ATTACK,null));
        }
        for(int i=0;i<enemyParty.getSize();i++)
        {
            e = (Enemy) enemyParty.get(i);
            perTurnQueue.add(new BattleAction((Character) e,(Character) heroes[battleRandomGen.nextInt(3)], BattleAction.ACTIONTYPE.ATTACK,null));
        }
    }
}
