package com.arnopaja.supermac.world.objects;

import com.arnopaja.supermac.helpers.AssetLoader;
import com.arnopaja.supermac.helpers.Interaction;
import com.arnopaja.supermac.world.grid.Direction;
import com.arnopaja.supermac.world.grid.Location;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * @author Ari Weiland
 */
public class MainMapCharacter extends MapCharacter {

    private Direction movingDirection = null;

    public MainMapCharacter(Location location) {
        this(location, Direction.WEST);
    }

    public MainMapCharacter(Location location, Direction direction) {
        super(location, direction, false, AssetLoader.getAsset("Steven"));
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        if (continueMoving()) {
            super.move(movingDirection);
        }
    }

    @Override
    public boolean move(Direction direction) {
        movingDirection = direction;
        return super.move(direction);
    }

    public void stop() {
        movingDirection = null;
    }

    public Direction getMovingDirection() {
        return movingDirection;
    }

    public boolean continueMoving() {
        return movingDirection != null;
    }

    public Interaction interact() {
        Entity entity = getGrid().getEntity(Direction.getAdjacent(getPosition(), getDirection()));
        if (entity != null) {
            return entity.interact(this);
        }
        return Interaction.NULL;
    }

    public static class Parser extends Entity.Parser<MainMapCharacter> {
        @Override
        public MainMapCharacter fromJson(JsonElement element) {
            JsonObject object = element.getAsJsonObject();
            Location location = getObject(object, Location.class);
            Direction direction = Direction.WEST;
            if (has(object, Direction.class)) {
                direction = getObject(object, Direction.class);
            }
            return new MainMapCharacter(location, direction);
        }

        @Override
        public JsonElement toJson(MainMapCharacter object) {
            JsonObject json = toBaseJson(object);
            addObject(json, object.getDirection(), Direction.class);
            return json;
        }
    }
}
