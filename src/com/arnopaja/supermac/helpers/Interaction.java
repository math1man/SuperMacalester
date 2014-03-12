package com.arnopaja.supermac.helpers;

import com.arnopaja.supermac.GameScreen;
import com.arnopaja.supermac.battle.BattleAction;
import com.arnopaja.supermac.battle.BattleController;

/**
 * This class dictates how an interaction proceeds via its run method
 *
 * @author Ari Weiland
 */
public abstract class Interaction {

    public static Interaction getNull() {
        return new Interaction() {
            @Override
            public void run(GameScreen screen) {}
        };
    }

    public static Interaction dialogue(final DialogueDisplayable dialogue) {
        if (dialogue == null) {
            return Interaction.getNull();
        } else {
            return new Interaction() {
                @Override
                public void run(GameScreen screen) {
                    screen.dialogue();
                    screen.getDialogueHandler().displayDialogue(dialogue);
                }
            };
        }
    }

    public static Interaction clearDialogue() {
        return new Interaction() {
            @Override
            public void run(GameScreen screen) {
                screen.endDialogue();
                screen.getDialogueHandler().clear();
            }
        };
    }

    public static Interaction goToBattle(final BattleController battle) {
        if (battle == null) {
            return Interaction.getNull();
        } else {
            return new Interaction() {
                @Override
                public void run(GameScreen screen) {
                    screen.goToBattle(battle);
                }
            };
        }
    }

    public static Interaction preBattle(final Dialogue dialogue, final BattleController battle) {
        if (dialogue == null || battle == null) {
            return Interaction.getNull();
        } else {
            return new Interaction() {
                @Override
                public void run(GameScreen screen) {
                    screen.preBattle(battle);
                    screen.getDialogueHandler().displayDialogue(dialogue);
                }
            };
        }
    }

    public static Interaction battle(final BattleAction action) {
        if (action == null) {
            return Interaction.getNull();
        } else {
            return new Interaction() {
                @Override
                public void run(GameScreen screen) {
                    screen.getBattle().addAction(action);
                }
            };
        }
    }

    public abstract void run(GameScreen screen);
}
