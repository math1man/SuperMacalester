package com.arnopaja.supermac.world.objects;

import com.arnopaja.supermac.helpers.Interaction;
import com.arnopaja.supermac.helpers.SuperParser;
import com.arnopaja.supermac.plot.QuestEntity;
import com.arnopaja.supermac.world.grid.Location;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * @author Ari Weiland
 */
public class NonRenderedQuestEntity extends NonRenderedEntity implements QuestEntity {

    private final Location location;
    private Interaction interaction = Interaction.NULL;

    public NonRenderedQuestEntity(Location location) {
        super(null);
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

    @Override
    public Interaction toInteraction() {
        return interaction;
    }

    public static class Parser extends SuperParser<NonRenderedQuestEntity> {
        @Override
        public NonRenderedQuestEntity fromJson(JsonElement element) {
            JsonObject object = element.getAsJsonObject();
            Location location = null;
            if (has(object, Location.class)) {
                location = getObject(object, Location.class);
            }
            return new NonRenderedQuestEntity(location);
        }

        @Override
        public JsonElement toJson(NonRenderedQuestEntity object) {
            JsonObject json = new JsonObject();
            addObject(json, object.location, Location.class);
            return json;
        }
    }
}
