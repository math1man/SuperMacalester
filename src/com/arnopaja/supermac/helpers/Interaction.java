package com.arnopaja.supermac.helpers;

import com.arnopaja.supermac.GameScreen;
import com.arnopaja.supermac.battle.BattleAction;
import com.arnopaja.supermac.battle.BattleController;
import com.arnopaja.supermac.helpers.dialogue.Dialogue;
import com.arnopaja.supermac.helpers.dialogue.DialogueDisplayable;
import com.arnopaja.supermac.helpers.dialogue.DialogueOptions;
import com.arnopaja.supermac.inventory.AbstractItem;
import com.arnopaja.supermac.inventory.Inventory;
import com.arnopaja.supermac.plot.Quest;
import com.arnopaja.supermac.world.grid.Location;
import com.arnopaja.supermac.world.objects.Chest;
import com.arnopaja.supermac.world.objects.Entity;

import java.util.Arrays;
import java.util.List;

/**
 * This class dictates how an interaction proceeds via its run method.
 * All static createInteraction methods here create an interaction by
 * calling its constructor. In contrast, all createInteraction methods
 * in the InteractionUtils static class create interactions by calling
 * one of these methods.
 *
 * @author Ari Weiland
 */
public abstract class Interaction {

    public abstract void run(GameScreen screen);

    /**
     * Default null interaction.  Running this does nothing
     */
    public static final Interaction NULL = new Interaction() {
        @Override
        public void run(GameScreen screen) {}
    };

    public static final Interaction CLEAR_DIALOGUE = new Interaction() {
        @Override
        public void run(GameScreen screen) {
            screen.getDialogueHandler().clear();
            screen.endDialogue();
        }
    };

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

    public static Interaction battle(final BattleController battle) {
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

    public static Interaction openChest(final Chest chest) {
        if (chest == null) {
            return NULL;
        } else {
            return new Interaction() {
                public void run(GameScreen screen) {
                    chest.open();
                    DialogueDisplayable dialogue;
                    if (chest.isEmpty()) {
                        dialogue = new Dialogue("This chest is empty", Interaction.closeChest(chest));
                    } else {
                        // TODO: DialogueOptions that allows you to remove items from the chest
                        // Items go into inventory from chest
                        List<AbstractItem> items = chest.getContents();
                        int length = items.size() + 2;

                        Object[] objects = Arrays.copyOf(items.toArray(), length);
                        objects[length - 2] = "All";
                        objects[length - 1] = "Close";

                        Interaction[] interactions = new Interaction[length];
                        for (int i=0; i<length-2; i++) {
                            interactions[i] = takeItem(items.get(i), chest);
                        }
                        interactions[length - 2] = Interaction.combine(Arrays.copyOf(interactions, length-2));
                        interactions[length - 1] = Interaction.closeChest(chest);

                        dialogue = new DialogueOptions("Take items?", objects, interactions);
                    }
                    screen.getDialogueHandler().displayDialogue(dialogue);
                }
            };
        }
    }

    public static Interaction closeChest(final Chest chest) {
        if (chest == null) {
            return NULL;
        } else {
            return new Interaction() {
                public void run(GameScreen screen) {
                    Interaction.CLEAR_DIALOGUE.run(screen);
                    chest.close();
                }
            };
        }
    }

    public static Interaction takeItem(final AbstractItem item, final Chest chest) {
        if (item == null || chest == null) {
            return NULL;
        } else {
            return new Interaction() {
                @Override
                public void run(GameScreen screen) {
                    chest.removeItem(item);               // remove item from chest
                    Inventory.add(item);                  // place in inventory
                    if (chest.isEmpty()) {                // if chest is empty,
                        Interaction.closeChest(chest);    // close it, otherwise
                    } else {                              // return to chest UI
                        Interaction.openChest(chest).run(screen);
                    }
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

    public static Interaction battleAction(final BattleAction action) {
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
