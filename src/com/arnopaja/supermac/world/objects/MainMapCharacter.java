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
        super(location, false, AssetLoader.getAsset("Steven"));
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
        return interact(this);
    }

    @Override
    public Interaction interact(MainMapCharacter character) {
        Entity entity = getGrid().getEntity(Direction.getAdjacent(getPosition(), getFacing()));
        if (entity != null) {
            return entity.interact(this);
        }
        return Interaction.NULL;
    }

    public static class Parser extends Entity.Parser<MainMapCharacter> {
        @Override
        public MainMapCharacter convert(JsonElement element) {
            JsonObject object = element.getAsJsonObject();
            JsonObject location = object.getAsJsonObject("location");
            return new MainMapCharacter(convert(location, Location.class));
        }
    }
}
