package com.arnopaja.supermac.render;

import com.arnopaja.supermac.battle.BattleAction;
import com.arnopaja.supermac.battle.EnemyParty;
import com.arnopaja.supermac.battle.Hero;
import com.arnopaja.supermac.battle.MainParty;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Random;

/**
 * This class keeps track of what is going on in a battle.
 *
 * @author Ari Weiland
 */
public class BattleInterface {

    private static Random battleRandomGen = new Random();

    private boolean isBossFight;
    private MainParty mainParty;
    private EnemyParty enemyParty;
    private PriorityQueue<BattleAction> perTurnQueue;

    private TextureRegion background;

    public BattleInterface(MainParty heroes, EnemyParty enemies, byte location) {
        perTurnQueue = new PriorityQueue<BattleAction>(heroes.getSize() + enemies.getSize(),
                new Comparator<BattleAction>() {
                    public int compare(BattleAction a, BattleAction b) {
                        // compare n1 and n2
                        return a.getPriority() < b.getPriority() ? -1 : a.getPriority() == b.getPriority() ? 0 : 1;
                    }
                }
        );
        isBossFight = enemies.containsBoss();
    }

    public void update(float delta) {
        if (mainParty.isDefeated()) {
            // run code for if the main party is defeated
        } else if (enemyParty.isDefeated()) {
            // run code for if the enemy party is defeated
        } else {
            setTurnActions();
            // TODO: this looping should be handled by the update loop
            for(BattleAction ba : perTurnQueue) {
                // TODO: handle dialogue
                ba.runAction();
            }
        }
    }

    public void setTurnActions() {
        Hero heroes[] = mainParty.getBattleParty();
        // TODO: allow character actions to be set
        for(Hero hero : heroes) {
            perTurnQueue.add(BattleAction.createAttack(hero,
                    enemyParty.get(battleRandomGen.nextInt(enemyParty.getSize()))));
        }
        for(int i=0; i<enemyParty.getSize(); i++){
            perTurnQueue.add(BattleAction.createAttack(enemyParty.get(i),
                    heroes[battleRandomGen.nextInt(3)]));
        }
    }

    public boolean isBossFight() {
        return isBossFight;
    }

    public void setBossFight(boolean isBossFight) {
        this.isBossFight = isBossFight;
    }

    public MainParty getMainParty() {
        return mainParty;
    }

    public void setMainParty(MainParty mainParty) {
        this.mainParty = mainParty;
    }

    public EnemyParty getEnemyParty() {
        return enemyParty;
    }

    public void setEnemyParty(EnemyParty enemyParty) {
        this.enemyParty = enemyParty;
    }

    public PriorityQueue<BattleAction> getPerTurnQueue() {
        return perTurnQueue;
    }

    public TextureRegion getBackground() {
        return background;
    }

    public void setBackground(TextureRegion background) {
        this.background = background;
    }
}
