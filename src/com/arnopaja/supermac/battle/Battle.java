package com.arnopaja.supermac.battle;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.Comparator;
import java.util.PriorityQueue;

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
            for(BattleAction ba : perTurnQueue)
            {

            }
        }
        return false;
    }
    public void setTurnActions()
    {
        //allow character actions to be set
    }
}
