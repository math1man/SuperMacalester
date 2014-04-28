package com.arnopaja.supermac.world.objects;

import com.arnopaja.supermac.helpers.Interaction;
import com.arnopaja.supermac.plot.QuestEntity;
import com.arnopaja.supermac.world.grid.Direction;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * @author Ari Weiland
 */
public class QuestNpc extends MapNpc implements QuestEntity {

    public QuestNpc(String name, Direction direction) {
        super(name, null, direction, true, Interaction.NULL);
    }

    @Override
    public void setInteraction(Interaction interaction) {
        this.interaction = interaction;
    }

    public static class Parser extends Entity.Parser<QuestNpc> {
        @Override
        public QuestNpc fromJson(JsonElement element) {
            JsonObject object = element.getAsJsonObject();
            String name = null;
            if (object.has("name")) {
                name = getString(object, "name");
            }
            Direction direction = Direction.WEST;
            if (has(object, Direction.class)) {
                direction = getObject(object, Direction.class);
            }
            return new QuestNpc(name, direction);
        }

        @Override
        public JsonElement toJson(QuestNpc object) {
            JsonObject json = new JsonObject();
            addClass(json, QuestNpc.class);
            addString(json, "name", object.getName());
            addObject(json, object.getDirection(), Direction.class);
            return json;
        }
    }
}
