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

            // for dialogue output:
            System.out.println(handler.getWindow().toString());
        }
    }
}
