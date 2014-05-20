package com.arnopaja.supermac.world.objects;

import com.arnopaja.supermac.GameScreen;
import com.arnopaja.supermac.helpers.interaction.Interactions;
import com.arnopaja.supermac.helpers.load.AssetLoader;
import com.arnopaja.supermac.helpers.interaction.Interaction;
import com.arnopaja.supermac.helpers.SuperParser;
import com.arnopaja.supermac.world.grid.Direction;
import com.arnopaja.supermac.world.grid.Location;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * @author Ari Weiland
 */
public class MapNpc extends MapCharacter {

    public static final boolean DEFAULT_CAN_MOVE = true;
    public static final float SECONDS_BETWEEN_RANDOM_MOVES = 4;

    private final String name;
    protected Interaction interaction;
    private boolean canMove;

    public MapNpc(String name) {
        this(name, null);
    }

    public MapNpc(String name, Location location) {
        this(name, location, Direction.WEST);
    }

    public MapNpc(String name, Location location, Direction direction) {
        this(name, location, direction, false, Interactions.NULL);
    }

    public MapNpc(String name, Location location, Direction direction, boolean isInteractable, Interaction interaction) {
        this(name, location, direction, isInteractable, interaction, DEFAULT_CAN_MOVE);
    }

    public MapNpc(String name, Location location, Direction direction, boolean isInteractable, Interaction interaction, boolean canMove) {
        super(location, direction, isInteractable, AssetLoader.getCharacter(name));
        this.name = name;
        this.interaction = interaction;
        this.canMove = canMove;
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        if (!isMoving() && canMove()) {
            float random = RANDOM.nextFloat() * SECONDS_BETWEEN_RANDOM_MOVES / delta;
            if (random < 1) {
                int ordinal = RANDOM.nextInt(4);
                move(Direction.values()[ordinal]);
            }
        }
    }

    public boolean canMove() {
        return canMove;
    }

    public void setCanMove(boolean canMove) {
        this.canMove = canMove;
    }

    public String getName() {
        return name;
    }

    @Override
    public void run(GameScreen screen) {
        interaction.run(screen);
    }

    @Override
    public String toString() {
        return "MapNpc{" +
                "name='" + name + '\'' +
                ", isInteractable=" + isInteractable() +
                ", interaction=" + interaction +
                ", canMove=" + canMove +
                '}';
    }

    public static class Parser extends SuperParser<MapNpc> {
        @Override
        public MapNpc fromJson(JsonElement element) {
            JsonObject object = element.getAsJsonObject();
            String name = getString(object, "name", null);
            Location location = getObject(object, Location.class, null);
            Direction direction = getObject(object, Direction.class, Direction.WEST);
            boolean isInteractable = getBoolean(object, "interactable", false);
            Interaction interaction = getObject(object, Interaction.class, Interactions.NULL);
            boolean canMove = getBoolean(object, "canMove", MapNpc.DEFAULT_CAN_MOVE);
            return new MapNpc(name, location, direction, isInteractable, interaction, canMove);
        }

        @Override
        public JsonElement toJson(MapNpc object) {
            JsonObject json = new JsonObject();
            addString(json, "name", object.name);
            addObject(json, object.getLocation(), Location.class);
            addObject(json, object.getDirection(), Direction.class);
            addBoolean(json, "canMove", object.canMove);
            addBoolean(json, "interactable", object.isInteractable());
            if (object.interaction != null) {
                addObject(json, object.interaction, Interaction.class);
            }
            return json;
        }
    }
}
