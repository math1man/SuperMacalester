package com.arnopaja.supermac.world.objects;

import com.arnopaja.supermac.helpers.Interaction;
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
    private Interaction netInteraction;

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
    }

    public void activate(Quest quest) {
        if (isPrimary) {
            netInteraction = interaction.attach(quest);
        }
        forceChangeGrid(location);
    }

    public void deactivate() {
        setInteractable(false);
        removeFromGrid(delay);
    }

    @Override
    public Interaction toInteraction() {
        return netInteraction;
    }

    public static class Parser extends SuperParser<QuestNpc> {
        @Override
        public QuestNpc fromJson(JsonElement element) {
            JsonObject object = element.getAsJsonObject();
            String name = null;
            if (object.has("name")) {
                name = getString(object, "name");
            }
            Location location = getObject(object, Location.class);
            Direction direction = Direction.WEST;
            if (has(object, Direction.class)) {
                direction = getObject(object, Direction.class);
            }
            Interaction interaction = Interaction.NULL;
            if (has(object, Interaction.class)) {
                interaction = getObject(object, Interaction.class);
            }
            boolean isPrimary = true;
            if (object.has("primary")) {
                isPrimary = getBoolean(object, "primary");
            }
            boolean delay = true;
            if (object.has("delay")) {
                delay = getBoolean(object, "delay");
            }
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
