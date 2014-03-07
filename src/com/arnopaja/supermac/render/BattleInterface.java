package com.arnopaja.supermac.render;

import com.arnopaja.supermac.battle.*;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Random;

/**
 * This class keeps track of what is going on in a battle.
 * Analogous to the WorldInterface.
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
            int temp;
            for(BattleAction ba : perTurnQueue) {
                switch(ba.getType()) {
                    case ATTACK:
                        System.out.println(ba.getSource().getName() + " attacks " + ba.getDestination().getName());
                        temp = (int) (ba.getSource().getAttack() * (2 + Math.abs(battleRandomGen.nextGaussian())));
                        temp /= ba.getDestination().getDefense();
                        System.out.println(temp + " damage done!");
                        ba.getDestination().modifyHealth((short)-temp);
                        if(ba.getDestination().isFainted()) System.out.println(ba.getDestination().getName() + " fell!");
                        break;
                    case DEFEND:
                        //temporarily increase defense
                        System.out.println(ba.getSource().getName() + " is defending.");
                        break;
                    case MAGIC:
                        Spell s = (Spell) ba.getMyUsable();
                        if(s != null) {
                            System.out.println(ba.getSource().getName() + " casts " + s.getName());
                            temp = (int) s.getDamageModifier() * ba.getSource().getSpecial();
                            temp /= (ba.getDestination().getSpecial() / 4);
                            System.out.println(temp + " damage done!");
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
    }

    public void setTurnActions() {
        Enemy enemy;
        Hero heroes[] = mainParty.getBattleParty();
        //allow character actions to be set
        for(Hero hero : heroes) {
            perTurnQueue.add(new BattleAction(hero,
                    enemyParty.get(battleRandomGen.nextInt(enemyParty.getSize())),
                    BattleAction.ACTIONTYPE.ATTACK, null));
        }
        for(int i=0; i<enemyParty.getSize(); i++){
            enemy = (Enemy) enemyParty.get(i);
            perTurnQueue.add(new BattleAction(enemy,
                    heroes[battleRandomGen.nextInt(3)],
                    BattleAction.ACTIONTYPE.ATTACK, null));
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
