package com.arnopaja.supermac.objects;

import com.arnopaja.supermac.helpers.Dialogue;
import com.arnopaja.supermac.helpers.DialogueHandler;
import com.arnopaja.supermac.render.BattleController;
import com.arnopaja.supermac.screen.GameScreen;

/**
 * This class dictates how an interaction proceeds via its run method
 *
 * @author Ari Weiland
 */
public abstract class Interaction {

    public static Interaction getNullInteraction() {
        return new Interaction() {
            @Override
            public void run(GameScreen screen, DialogueHandler dialogueHandler) {}
        };
    }

    public static Interaction getDialogueInteraction(final Dialogue dialogue) {
        return new Interaction() {
            @Override
            public void run(GameScreen screen, DialogueHandler dialogueHandler) {
                screen.dialogue();
                dialogueHandler.displayDialogue(dialogue);
            }
        };
    }

    public static Interaction getBattleInteraction(final BattleController battle) {
        return new Interaction() {
            @Override
            public void run(GameScreen screen, DialogueHandler dialogueHandler) {
                screen.goToBattle(battle);
            }
        };
    }

    public static Interaction getPreBattleInteraction(final Dialogue dialogue, final BattleController battle) {
        return new Interaction() {
            @Override
            public void run(GameScreen screen, DialogueHandler dialogueHandler) {
                screen.preBattle(battle);
                dialogueHandler.displayDialogue(dialogue);
            }
        };
    }

    public abstract void run(GameScreen screen, DialogueHandler dialogueHandler);
}
