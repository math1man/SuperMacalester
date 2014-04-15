package com.arnopaja.supermac.world.objects;

import com.arnopaja.supermac.helpers.Interaction;
import com.arnopaja.supermac.world.grid.Location;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * @author Ari Weiland
 */
public class Door extends Entity {

    private final Location destination;

    public Door(Location location, Location destination) {
        super(false, location, true);
        this.destination = destination;
    }

    @Override
    public void update(float delta) {
        // nothing happens
    }

    @Override
    public Interaction interact(final MainMapCharacter character) {
        setInteraction(Interaction.changeGrid(character, destination));
        return super.interact(character);
    }

    public Location getDestination() {
        return destination;
    }

    @Override
    public TextureRegion getSprite(float runTime) {
        return null;
    }

    public static class Parser extends Entity.Parser<Door> {
        @Override
        public Door convert(JsonElement element) {
            JsonObject object = element.getAsJsonObject();
            Location location = convert(object.getAsJsonObject("location"), Location.class);
            Location destination = convert(object.getAsJsonObject("destination"), Location.class);
            return new Door(location, destination);
        }
    }
}
