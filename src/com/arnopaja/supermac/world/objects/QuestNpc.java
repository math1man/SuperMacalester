package com.arnopaja.supermac.world.objects;

import com.arnopaja.supermac.helpers.Interaction;
import com.arnopaja.supermac.helpers.SuperParser;
import com.arnopaja.supermac.plot.QuestEntity;
import com.arnopaja.supermac.world.grid.Direction;
import com.arnopaja.supermac.world.grid.Location;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * @author Ari Weiland
 */
public class QuestNpc extends MapNpc implements QuestEntity {

    private final Location location;

    public QuestNpc(String name, Location location, Direction direction) {
        super(name, null, direction, true, Interaction.NULL);
        this.location = location;
    }

    @Override
    public void activate(Interaction interaction) {
        this.interaction = interaction;
        forceChangeGrid(location);
    }

    @Override
    public void deactivate(boolean delay) {
        setInteractable(false);
        removeFromGrid(delay);
    }

    public static class Parser extends SuperParser<QuestNpc> {
        @Override
        public QuestNpc fromJson(JsonElement element) {
            JsonObject object = element.getAsJsonObject();
            String name = null;
            if (object.has("name")) {
                name = getString(object, "name");
            }
            Location location = null;
            if (has(object, Location.class)) {
                location = getObject(object, Location.class);
            }
            Direction direction = Direction.WEST;
            if (has(object, Direction.class)) {
                direction = getObject(object, Direction.class);
            }
            return new QuestNpc(name, location, direction);
        }

        @Override
        public JsonElement toJson(QuestNpc object) {
            JsonObject json = new JsonObject();
            addString(json, "name", object.getName());
            addObject(json, object.getLocation(), Location.class);
            addObject(json, object.getDirection(), Direction.class);
            return json;
        }
    }
}
