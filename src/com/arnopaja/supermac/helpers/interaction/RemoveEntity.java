package com.arnopaja.supermac.helpers.interaction;

import com.arnopaja.supermac.GameScreen;
import com.arnopaja.supermac.helpers.load.SuperParser;
import com.arnopaja.supermac.world.grid.Location;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * @author Ari Weiland
 */
public class RemoveEntity implements Interaction {

    private final Location location;

    public RemoveEntity(Location location) {
        this.location = location;
    }

    public Location getLocation() {
        return location;
    }

    @Override
    public void run(GameScreen screen) {
        if (location.getEntity() != null) {
            location.getEntity().forceChangeGrid(null);
        }
    }

    public static class Parser extends SuperParser<RemoveEntity> {
        @Override
        public RemoveEntity fromJson(JsonElement element) {
            JsonObject object = element.getAsJsonObject();
            Location location = getObject(object, Location.class);
            return new RemoveEntity(location);
        }

        @Override
        public JsonElement toJson(RemoveEntity object) {
            JsonObject json = new JsonObject();
            addObject(json, object.location, Location.class);
            return json;
        }
    }
}
