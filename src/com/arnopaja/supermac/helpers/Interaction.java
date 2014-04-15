package com.arnopaja.supermac.helpers;

import com.arnopaja.supermac.GameScreen;
import com.arnopaja.supermac.battle.Battle;
import com.arnopaja.supermac.battle.BattleAction;
import com.arnopaja.supermac.helpers.dialogue.Dialogue;
import com.arnopaja.supermac.helpers.dialogue.DialogueOptions;
import com.arnopaja.supermac.helpers.dialogue.DialogueText;
import com.arnopaja.supermac.inventory.GenericItem;
import com.arnopaja.supermac.inventory.Inventory;
import com.arnopaja.supermac.plot.Quest;
import com.arnopaja.supermac.world.grid.Location;
import com.arnopaja.supermac.world.objects.Chest;
import com.arnopaja.supermac.world.objects.Entity;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

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

    private final EqualityParameters parameters;

    public Interaction() {
        this(null);
    }

    public <U> Interaction(U primary) {
        this(primary, null);
    }

    public <U, V> Interaction(U primary, V secondary) {
        parameters = new EqualityParameters<U, V>(primary, secondary);
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

    public static Interaction dialogue(final Dialogue dialogue) {
        if (dialogue == null) {
            return NULL;
        } else {
            return new Interaction(dialogue) {
                @Override
                public void run(GameScreen screen) {
                    screen.dialogue();
                    screen.getDialogueHandler().displayDialogue(dialogue);
                }
            };
        }
    }

    public static Interaction battle(final Battle battle) {
        if (battle == null) {
            return NULL;
        } else {
            return new Interaction(battle) {
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
            return new Interaction(chest) {
                public void run(GameScreen screen) {
                    chest.open();
                    Dialogue dialogue;
                    if (chest.isEmpty()) {
                        dialogue = new DialogueText(Interaction.closeChest(chest), "This chest is empty");
                    } else {
                        // Items go into inventory from chest
                        List<GenericItem> items = chest.getContents().getAll();
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
            return new Interaction(chest) {
                public void run(GameScreen screen) {
                    Interaction.CLEAR_DIALOGUE.run(screen);
                    chest.close();
                }
            };
        }
    }

    public static Interaction takeItem(final GenericItem item, final Chest chest) {
        if (item == null || chest == null) {
            return NULL;
        } else {
            return new Interaction(item, chest) {

                @Override
                public void run(GameScreen screen) {
                    chest.removeItem(item);             // take item from chest
                    Inventory.getMain().store(item);    // place in inventory
                    if (chest.isEmpty()) {              // if chest is empty,
                        Interaction.closeChest(chest);  // close it, otherwise
                    } else {                            // return to chest UI
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
            return new Interaction(entity, location) {
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
            return new Interaction(action) {
                @Override
                public void run(GameScreen screen) {
                    screen.getBattle().addAction(action);
                    if (action.getType() == BattleAction.ActionType.ITEM) {
                        // Removes the item from Inventory when the battle
                        // action is put into the action queue
                        Inventory.getMain().take(action.getItem());
                    }
                }
            };
        }
    }

    public static Interaction nextGoal(final Quest quest) {
        if (quest == null) {
            return NULL;
        } else {
            return new Interaction(quest) {
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
            return new Interaction(interactions) {
                @Override
                public void run(GameScreen screen) {
                    for (Interaction interaction : interactions) {
                        interaction.run(screen);
                    }
                }
            };
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Interaction)) return false;

        Interaction that = (Interaction) o;

        return (parameters.equals(that.parameters));
    }

    private static class EqualityParameters<U, V> {
        protected final U primary;
        protected final V secondary;

        private EqualityParameters(U primary, V secondary) {
            this.primary = primary;
            this.secondary = secondary;
        }



        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof EqualityParameters)) return false;

            EqualityParameters that = (EqualityParameters) o;

            if (primary == null) {
                if (that.primary != null) return false;
            } else {
                if (primary instanceof Interaction[]) {
                    if (!Arrays.equals((Interaction[]) primary, (Interaction[]) that.primary)) return false;
                } else {
                    if (!primary.equals(that.primary)) return false;
                }
            }
            if (secondary == null) {
                return that.secondary == null;
            } else {
                return secondary.equals(that.secondary);
            }
        }
    }

    public static class Parser extends SuperParser<Interaction> {
        @Override
        public Interaction fromJson(JsonElement element) {
            JsonObject object = element.getAsJsonObject();
            if (object.has("dialogue")) {
                Dialogue dialogue = getObject(object, Dialogue.class);
                return Interaction.dialogue(dialogue);
            } else if (object.has("battle")) {
                Battle battle = fromJson(object.get("battle"), Battle.class);
                return Interaction.battle(battle);
            }
            return Interaction.NULL;
        }

        @Override
        public JsonElement toJson(Interaction object) {
            JsonObject json = new JsonObject();
            Object param = object.parameters.primary;
            if (param instanceof Dialogue) {
                addObject(json, (Dialogue) param, Dialogue.class);
            } else if (param instanceof Battle) {
                addObject(json, (Battle) param, Battle.class);
            }
            return json;
        }
    }
}
