package com.arnopaja.supermac.world.objects;

import com.arnopaja.supermac.GameScreen;
import com.arnopaja.supermac.helpers.Interaction;
import com.arnopaja.supermac.world.grid.Location;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * @author Ari Weiland
 */
public class Door extends NonRenderedEntity {

    private final Location destination;
    private final Entity entity;

    public Door(Location location, Location destination, MainMapCharacter character) {
        super(location);
        this.destination = destination;
        this.entity = character;
    }

    public Location getDestination() {
        return destination;
    }

    @Override
    public Interaction toInteraction() {
        final Entity entity = this.entity;
        return new Interaction(entity, destination) {
            @Override
            public void run(GameScreen screen) {
                for (Entity e : entity.getGrid().getEntities()) {
                    if (e.isDelayed()) {
                        e.forceChangeGrid();
                    }
                }
                entity.changeGrid(destination);
            }
        };
    }

    public static class Parser extends Entity.Parser<Door> {
        @Override
        public Door fromJson(JsonElement element) {
            JsonObject object = element.getAsJsonObject();
            Location location = getObject(object, Location.class);
            Location destination = getObject(object, "destination", Location.class);
            return new Door(location, destination, world.getMainCharacter());
        }

        @Override
        public JsonElement toJson(Door object) {
            JsonObject json = toBaseJson(object);
            addObject(json, "destination", object.destination, Location.class);
            return json;
        }
    }
}
