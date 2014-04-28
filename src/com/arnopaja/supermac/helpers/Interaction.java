package com.arnopaja.supermac.helpers;

import com.arnopaja.supermac.GameScreen;
import com.arnopaja.supermac.battle.Battle;
import com.arnopaja.supermac.battle.characters.Hero;
import com.arnopaja.supermac.helpers.dialogue.Dialogue;
import com.arnopaja.supermac.inventory.GenericItem;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.Arrays;

/**
 * This class dictates how an interaction proceeds via its run method.
 * It also provides various static utility methods.
 *
 * @author Ari Weiland
 */
public abstract class Interaction implements InteractionBuilder {

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
     * Returns a new interaction that adds the builder onto this interaction.
     * This interaction is NOT modified by this method
     * @param builder
     * @return
     */
    public Interaction attach(InteractionBuilder builder) {
        if (builder == null) {
            return this;
        } else {
            return combine(this, builder);
        }
    }

    /**
     * Default null interaction.  Running this does nothing
     */
    public static final Interaction NULL = new Interaction() {
        @Override
        public void run(GameScreen screen) {}
    };

    static {
        System.out.println(NULL);
    }

    /**
     * Combines a group of InteractionBuilders into one new interaction that runs
     * the run methods of each builder.toInteraction() in the order specified.
     * This method runs no checks on the interactions, so it is the responsibility
     * of the user to not do anything stupid.
     *
     * @param builders the interactions to be combined
     * @return
     */
    public static Interaction combine(final InteractionBuilder... builders) {
        if (builders == null || builders.length == 0) {
            return NULL;
        } else {
            return new Interaction(builders) {
                @Override
                public void run(GameScreen screen) {
                    for (InteractionBuilder builder : builders) {
                        builder.toInteraction().run(screen);
                    }
                }
            };
        }
    }

    //-----------------------------
    //  Interaction Array Methods
    //-----------------------------

    /**
     * Returns an array of null interactions of the given size
     * @return
     * @param size
     */
    public static Interaction[] getNulls(int size) {
        return convert(NULL, size);
    }

    /**
     * Converts a set of ToInteractions into an array of interactions,
     * as per the ToInteraction.toInteraction method.
     * @param builders
     * @return
     */
    public static Interaction[] convert(InteractionBuilder... builders) {
        Interaction[] interactions = new Interaction[builders.length];
        for (int i=0; i<builders.length; i++) {
            interactions[i] = builders[i].toInteraction();
        }
        return interactions;
    }

    /**
     * Fills an array of interactions of the given size with the
     * specified builder.toInteraction()
     * @param builder
     * @return
     */
    public static Interaction[] convert(InteractionBuilder builder, int size) {
        InteractionBuilder[] builders = new InteractionBuilder[size];
        Arrays.fill(builders, builder);
        return convert(builders);
    }

    @Override
    public Interaction toInteraction() {
        return this;
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

            return paramEquals(primary, that.primary) && paramEquals(secondary, that.secondary);
        }

        private static boolean paramEquals(Object a, Object b) {
            if (a == b) return true;
            if (a == null || b == null) return false;

            if (a instanceof Object[] && b instanceof Object[]) {
                return Arrays.equals((Object[]) a, (Object[]) b);
            } else {
               return a.equals(b);
            }
        }
    }

    public static class Parser extends SuperParser<Interaction> {
        @Override
        public Interaction fromJson(JsonElement element) {
            JsonObject object = element.getAsJsonObject();
            Interaction interaction = NULL;
            if (object.has("dialogue")) {
                Dialogue dialogue = getObject(object, "dialogue", Dialogue.class);
                return dialogue.toInteraction();
            } else if (object.has("options")) {
                Dialogue dialogue = getObject(object, "options", Dialogue.class);
                return dialogue.toInteraction();
            }
            if (has(object, GenericItem.class)) {
                GenericItem item = getObject(object, GenericItem.class);
                interaction.attach(item);
            }
            if (has(object, Hero.class)) {
                Hero hero = getObject(object, Hero.class);
                interaction.attach(hero);
            }
            if (has(object, Battle.class)) {
                Battle battle = getObject(object, Battle.class);
                interaction.attach(battle);
            }
            return interaction;
        }

        @Override
        public JsonElement toJson(Interaction object) {
            JsonObject json = new JsonObject();
            addToJson(json, object.parameters.primary);
            return json;
        }

        private static void addToJson(JsonObject json, Object param) {
            if (param instanceof Dialogue) {
                addObject(json, (Dialogue) param, Dialogue.class);
            } else if (param instanceof Battle) {
                addObject(json, (Battle) param, Battle.class);
            } else if (param instanceof GenericItem) {
                addObject(json, (GenericItem) param, GenericItem.class);
            } else if (param instanceof Hero) {
                addObject(json, (Hero) param, Hero.class);
            } else if (param instanceof InteractionBuilder[]) {
                for (InteractionBuilder builder : ((InteractionBuilder[]) param)) {
                    addToJson(json, builder);
                }
            }
        }
    }
}
