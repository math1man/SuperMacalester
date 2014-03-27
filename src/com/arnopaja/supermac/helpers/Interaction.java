package com.arnopaja.supermac.helpers;

import com.arnopaja.supermac.GameScreen;
import com.arnopaja.supermac.battle.BattleAction;
import com.arnopaja.supermac.battle.BattleController;
import com.arnopaja.supermac.helpers.dialogue.DialogueDisplayable;
import com.arnopaja.supermac.plot.Quest;
import com.arnopaja.supermac.world.grid.Location;
import com.arnopaja.supermac.world.objects.Entity;

/**
 * This class dictates how an interaction proceeds via its run method
 *
 * @author Ari Weiland
 */
public abstract class Interaction {

    /**
     * Default null interaction.  Running this does nothing
     */
    public static final Interaction NULL = new Interaction() {
        @Override
        public void run(GameScreen screen) {}
    };

    public abstract void run(GameScreen screen);

    public static Interaction dialogue(final DialogueDisplayable dialogue) {
        if (dialogue == null) {
            return NULL;
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
                screen.getDialogueHandler().clear();
                screen.endDialogue();
            }
        };
    }

    public static Interaction goToBattle(final BattleController battle) {
        if (battle == null) {
            return NULL;
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
            return NULL;
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

    public static Interaction changeGrid(final Entity entity, final Location location) {
        if (entity == null) {
            return NULL;
        } else {
            return new Interaction() {
                @Override
                public void run(GameScreen screen) {
                    for (Entity e : entity.getGrid().getEntities()) {
                        if (e.isDelayed()) {
                            e.changeGrid();
                        }
                    }
                    entity.changeGrid(location);
                }
            };
        }
    }

    public static Interaction battle(final BattleAction action) {
        if (action == null) {
            return NULL;
        } else {
            return new Interaction() {
                @Override
                public void run(GameScreen screen) {
                    screen.getBattle().addAction(action);
                }
            };
        }
    }

    public static Interaction nextGoal(final Quest quest) {
        if (quest == null) {
            return NULL;
        } else {
            return new Interaction() {
                @Override
                public void run(GameScreen screen) {
                    quest.nextGoal();
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
            return NULL;
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
}
