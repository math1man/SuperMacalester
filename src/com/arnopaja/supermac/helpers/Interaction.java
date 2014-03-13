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

    public static Interaction preBattle(final DialogueDisplayable dialogue, final BattleController battle) {
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

    /**
     * Combines a group of interactions into one new interaction that runs
     * the run methods of each interaction in the order specified.
     * This method runs no checks on the interactions, so it is the
     * responsibility of the user to not do anything stupid.
     *
     * @param interactions the interactions to be combined
     * @return
     */
    public static Interaction combine(final Interaction... interactions) {
        if (interactions == null) {
            return Interaction.getNull();
        } else {
            return new Interaction() {
                @Override
                public void run(GameScreen screen) {
                    for (Interaction interaction : interactions) {
                        interaction.run(screen);
                    }
                }
            };
        }
    }

    public abstract void run(GameScreen screen);
}
