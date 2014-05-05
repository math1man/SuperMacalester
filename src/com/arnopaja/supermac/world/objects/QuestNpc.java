package com.arnopaja.supermac.world.objects;

import com.arnopaja.supermac.GameScreen;
import com.arnopaja.supermac.helpers.interaction.Interaction;
import com.arnopaja.supermac.helpers.interaction.Interactions;
import com.arnopaja.supermac.helpers.interaction.MultiInteraction;
import com.arnopaja.supermac.helpers.load.SuperParser;
import com.arnopaja.supermac.plot.Quest;
import com.arnopaja.supermac.world.grid.Direction;
import com.arnopaja.supermac.world.grid.Location;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * @author Ari Weiland
 */
public class QuestNpc extends MapNpc {

    private final Location location;
    private final boolean isPrimary;
    private final boolean delay;
    private final MultiInteraction netInteraction = new MultiInteraction();

    /**
     * Constructs a non-rendered QuestNpc
     * @param location
     * @param interaction
     * @param isPrimary
     * @param delay
     */
    public QuestNpc(Location location, Interaction interaction, boolean isPrimary, boolean delay) {
        this(null, location, Direction.WEST, interaction, isPrimary, delay);
    }

    /**
     * Constructs a rendered QuestNpc
     * @param name
     * @param location
     * @param direction
     * @param interaction
     * @param isPrimary
     * @param delay
     */
    public QuestNpc(String name, Location location, Direction direction, Interaction interaction, boolean isPrimary, boolean delay) {
        super(name, null, direction, true, interaction, false);
        this.location = location;
        this.isPrimary = isPrimary;
        this.delay = delay;
        netInteraction.attach(interaction);
    }

    public void activate(Quest quest) {
        if (isPrimary) {
            netInteraction.attach(quest);
        }
        forceChangeGrid(location);
    }

    public void deactivate() {
        setInteractable(false);
        removeFromGrid(delay);
    }

    @Override
    public void run(GameScreen screen) {
        netInteraction.run(screen);
    }

    public static class Parser extends SuperParser<QuestNpc> {
        @Override
        public QuestNpc fromJson(JsonElement element) {
            JsonObject object = element.getAsJsonObject();
            String name = getString(object, "name", null);
            Location location = getObject(object, Location.class);
            Direction direction = getObject(object, Direction.class, Direction.WEST);
            Interaction interaction = getObject(object, Interaction.class, Interactions.NULL);
            boolean isPrimary = getBoolean(object, "primary", true);
            boolean delay = getBoolean(object, "delay", true);
            return new QuestNpc(name, location, direction, interaction, isPrimary, delay);
        }

        @Override
        public JsonElement toJson(QuestNpc object) {
            JsonObject json = new JsonObject();
            addString(json, "name", object.getName());
            addObject(json, object.location, Location.class);
            addObject(json, object.getDirection(), Direction.class);
            addObject(json, object.interaction, Interaction.class);
            addBoolean(json, "primary", object.isPrimary);
            addBoolean(json, "delay", object.delay);
            return json;
        }
    }
}
