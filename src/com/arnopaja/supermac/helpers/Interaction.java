package com.arnopaja.supermac.helpers;

import com.arnopaja.supermac.GameScreen;
import com.arnopaja.supermac.battle.Battle;
import com.arnopaja.supermac.battle.BattleAction;
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
public abstract class Interaction<U, V> {

    // Interaction parameters
    protected final U primary;
    protected final V secondary;

    protected Interaction() {
        this(null);
    }

    protected Interaction(U primary) {
        this(primary, null);
    }

    protected Interaction(U primary, V secondary) {
        this.primary = primary;
        this.secondary = secondary;
    }

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

    public static Interaction dialogue(DialogueDisplayable dialogue) {
        if (dialogue == null) {
            return NULL;
        } else {
            return new Interaction<DialogueDisplayable, Object>(dialogue) {
                @Override
                public void run(GameScreen screen) {
                    screen.dialogue();
                    screen.getDialogueHandler().displayDialogue(primary);
                }
            };
        }
    }

    public static Interaction battle(Battle battle) {
        if (battle == null) {
            return NULL;
        } else {
            return new Interaction<Battle, Object>(battle) {
                @Override
                public void run(GameScreen screen) {
                    screen.goToBattle(primary);
                }
            };
        }
    }

    public static Interaction openChest(Chest chest) {
        if (chest == null) {
            return NULL;
        } else {
            return new Interaction<Chest, Object>(chest) {
                public void run(GameScreen screen) {
                    primary.open();
                    DialogueDisplayable dialogue;
                    if (primary.isEmpty()) {
                        dialogue = new Dialogue("This chest is empty", Interaction.closeChest(primary));
                    } else {
                        // Items go into inventory from chest
                        List<AbstractItem> items = primary.getContents();
                        int length = items.size() + 2;

                        Object[] objects = Arrays.copyOf(items.toArray(), length);
                        objects[length - 2] = "All";
                        objects[length - 1] = "Close";

                        Interaction[] interactions = new Interaction[length];
                        for (int i=0; i<length-2; i++) {
                            interactions[i] = takeItem(items.get(i), primary);
                        }
                        interactions[length - 2] = Interaction.combine(Arrays.copyOf(interactions, length-2));
                        interactions[length - 1] = Interaction.closeChest(primary);

                        dialogue = new DialogueOptions("Take items?", objects, interactions);
                    }
                    screen.getDialogueHandler().displayDialogue(dialogue);
                }
            };
        }
    }

    public static Interaction closeChest(Chest chest) {
        if (chest == null) {
            return NULL;
        } else {
            return new Interaction<Chest, Object>(chest) {
                public void run(GameScreen screen) {
                    Interaction.CLEAR_DIALOGUE.run(screen);
                    primary.close();
                }
            };
        }
    }

    public static Interaction takeItem(AbstractItem item, Chest chest) {
        if (item == null || chest == null) {
            return NULL;
        } else {
            return new Interaction<AbstractItem, Chest>(item, chest) {

                @Override
                public void run(GameScreen screen) {
                    secondary.removeItem(primary);          // remove item from chest
                    Inventory.add(primary);                 // place in inventory
                    if (secondary.isEmpty()) {              // if chest is empty,
                        Interaction.closeChest(secondary);  // close it, otherwise
                    } else {                                // return to chest UI
                        Interaction.openChest(secondary).run(screen);
                    }
                }
            };
        }
    }

    public static Interaction changeGrid(Entity entity, Location location) {
        if (entity == null) {
            return NULL;
        } else {
            return new Interaction<Entity, Location>(entity, location) {
                @Override
                public void run(GameScreen screen) {
                    for (Entity e : primary.getGrid().getEntities()) {
                        if (e.isDelayed()) {
                            e.changeGrid();
                        }
                    }
                    primary.changeGrid(secondary);
                }
            };
        }
    }

    public static Interaction battleAction(BattleAction action) {
        if (action == null) {
            return NULL;
        } else {
            return new Interaction<BattleAction, Object>(action) {
                @Override
                public void run(GameScreen screen) {
                    screen.getBattle().addAction(primary);
                }
            };
        }
    }

    public static Interaction nextGoal(Quest quest) {
        if (quest == null) {
            return NULL;
        } else {
            return new Interaction<Quest, Object>(quest) {
                @Override
                public void run(GameScreen screen) {
                    primary.nextGoal();
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
    public static Interaction combine(Interaction... interactions) {
        if (interactions == null) {
            return NULL;
        } else {
            return new Interaction<Interaction[], Object>(interactions) {
                @Override
                public void run(GameScreen screen) {
                    for (Interaction interaction : primary) {
                        interaction.run(screen);
                    }
                }

                @Override
                public boolean equals(Object o) {
                    if (this == o) return true;
                    if (!(o instanceof Interaction)) return false;

                    Interaction that = (Interaction) o;

                    return (primary == null ? that.primary == null :
                            (that.primary instanceof Interaction[]
                                    && Arrays.equals(primary, (Interaction[]) that.primary)))
                            && (secondary == null ? that.secondary == null : secondary.equals(that.secondary));

                }
            };
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Interaction)) return false;

        Interaction that = (Interaction) o;

        return (primary == null ? that.primary == null : primary.equals(that.primary))
                && (secondary == null ? that.secondary == null : secondary.equals(that.secondary));
    }
}
