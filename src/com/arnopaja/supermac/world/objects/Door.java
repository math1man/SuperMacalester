package com.arnopaja.supermac.world.objects;

import com.arnopaja.supermac.GameScreen;
import com.arnopaja.supermac.helpers.Interaction;
import com.arnopaja.supermac.helpers.SuperParser;
import com.arnopaja.supermac.world.grid.Location;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * @author Ari Weiland
 */
public class Door extends NonRenderedEntity {

    private final Location destination;

    public Door(Location location, Location destination) {
        super(location);
        this.destination = destination;
    }

    public Location getDestination() {
        return destination;
    }

    @Override
    public Interaction toInteraction() {
        return new Interaction(destination) {
            @Override
            public void run(GameScreen screen) {
                MainMapCharacter main = screen.getWorld().getMainCharacter();
                for (Entity e : main.getGrid().getEntities()) {
                    if (e.isDelayed()) {
                        e.forceChangeGrid();
                    }
                }
                main.changeGrid(destination);
            }
        };
    }

    public static class Parser extends SuperParser<Door> {
        @Override
        public Door fromJson(JsonElement element) {
            JsonObject object = element.getAsJsonObject();
            Location location = getObject(object, Location.class);
            Location destination = getObject(object, "destination", Location.class);
            return new Door(location, destination);
        }

        @Override
        public JsonElement toJson(Door object) {
            JsonObject json = new JsonObject();
            addObject(json, "location", object.getLocation(), Location.class);
            addObject(json, "destination", object.destination, Location.class);
            return json;
        }
    }
}
