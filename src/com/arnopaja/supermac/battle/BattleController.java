package com.arnopaja.supermac.battle;

import com.arnopaja.supermac.helpers.BaseController;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.Comparator;
import java.util.Random;
import java.util.concurrent.PriorityBlockingQueue;

/**
 * This class keeps track of what is going on in a battle.
 *
 * @author Ari Weiland
 */
public class BattleController implements BaseController {

    private static Random battleRandomGen = new Random();

    private final boolean isBossFight;
    private final MainParty mainParty;
    private final EnemyParty enemyParty;
    private final PriorityBlockingQueue<BattleAction> perTurnQueue;

    private TextureRegion background;

    public BattleController(MainParty mainParty, EnemyParty enemyParty, byte location) {
        this.mainParty = mainParty;
        this.enemyParty = enemyParty;
        perTurnQueue = new PriorityBlockingQueue<BattleAction>(mainParty.getSize() + enemyParty.getSize(),
                new Comparator<BattleAction>() {
                    public int compare(BattleAction a, BattleAction b) {
                        // compare n1 and n2
                        return a.getPriority() < b.getPriority() ? -1 : a.getPriority() == b.getPriority() ? 0 : 1;
                    }
                }
        );
        isBossFight = enemyParty.containsBoss();
    }

    @Override
    public void update(float delta) {
        if (mainParty.isDefeated()) {
            // run code for if the main party is defeated
        } else if (enemyParty.isDefeated()) {
            // run code for if the enemy party is defeated
        } else {
            BattleAction action = perTurnQueue.poll();
            if (action == null) {
                setTurnActions();
            } else {
                // TODO: handle dialogue?
                action.runAction();
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

    public MainParty getMainParty() {
        return mainParty;
    }

    public EnemyParty getEnemyParty() {
        return enemyParty;
    }

    public PriorityBlockingQueue<BattleAction> getPerTurnQueue() {
        return perTurnQueue;
    }

    public TextureRegion getBackground() {
        return background;
    }

    public void setBackground(TextureRegion background) {
        this.background = background;
    }
}
