package com.arnopaja.supermac.battle;

import com.arnopaja.supermac.battle.characters.EnemyParty;
import com.arnopaja.supermac.helpers.dialogue.DialogueHandler;

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
    }

    public static void main(String[] args) {
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
