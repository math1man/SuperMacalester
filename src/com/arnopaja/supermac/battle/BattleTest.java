package com.arnopaja.supermac.battle;

import com.arnopaja.supermac.battle.characters.Enemy;
import com.arnopaja.supermac.battle.characters.EnemyParty;
import com.arnopaja.supermac.battle.characters.Hero;
import com.arnopaja.supermac.helpers.dialogue.DialogueHandler;

import java.util.Random;

/**
 * @author Ari Weiland
 */
public class BattleTest extends Battle {

    public BattleTest(EnemyParty enemyParty, String backgroundName) {
        super(enemyParty, backgroundName);
    }

    @Override
    public void update(float delta) {
        if (isReady()) {
            if (mainParty.isDefeated()) {
                // run code for if the main party is defeated
            } else if (enemyParty.isDefeated()) {
                // run code for if the enemy party is defeated
            } else {
                BattleAction action = actionQueue.poll();
                if (action == null) {
                    setTurnActions();
                } else {
                    System.out.println(action.run(delta).getText());
                }
            }
        }
    }

    @Override
    protected void setTurnActions() {
        // implement me!
        Random r = new Random();
        //Set enemy actions
        BattleAction a;
        for (Enemy e:enemyParty)
        {
            a = BattleAction.attack(e,mainParty.getRandom());
            addAction(a);
            System.out.println(a);
        }
        //Set friendly actions
        for (Hero h:mainParty.getActiveParty())
        {
            a = BattleAction.attack(h,enemyParty.getRandom());
            addAction(a);
            System.out.println(a);
        }
    }

    public static void main(String[] args) {
        //create enemy party
        //create main party
        Battle battle = new Battle(/*enemy part*/, null);
        DialogueHandler handler = new DialogueHandler();
        battle.ready(/*main party*/, handler);

        while (/*loop condition*/) {
            float delta = 1f / 60;
            battle.update(delta);
            // some timing function based on delta
        }
    }
}
