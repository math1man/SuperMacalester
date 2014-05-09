package com.arnopaja.supermac.world.objects;

import com.arnopaja.supermac.GameScreen;
import com.arnopaja.supermac.helpers.interaction.Interactions;
import com.arnopaja.supermac.helpers.load.AssetLoader;
import com.arnopaja.supermac.helpers.interaction.Interaction;
import com.arnopaja.supermac.helpers.load.SuperParser;
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
        super(location, direction, true, AssetLoader.getCharacter("Tom"));
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

    @Override
    public void run(GameScreen screen) {
        Entity entity = getLocation().getAdjacent(getDirection()).getEntity();
        if (entity != null) {
            Interaction interaction = entity.interact();
            if (interaction != Interactions.NULL && entity instanceof MapCharacter) {
                ((MapCharacter) entity).setDirection(entity.getDirectionToward(getPosition()));
            }
            interaction.run(screen);
        }
    }

    public static class Parser extends SuperParser<MainMapCharacter> {
        @Override
        public MainMapCharacter fromJson(JsonElement element) {
            JsonObject object = element.getAsJsonObject();
            Location location = getObject(object, Location.class);
            Direction direction = getObject(object, Direction.class, Direction.WEST);
            return new MainMapCharacter(location, direction);
        }

        @Override
        public JsonElement toJson(MainMapCharacter object) {
            JsonObject json = new JsonObject();
            addObject(json, object.getLocation(), Location.class);
            addObject(json, object.getDirection(), Direction.class);
            return json;
        }
    }
}
