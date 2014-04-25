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
public class MapNpc extends MapCharacter {

    public static final boolean DEFAULT_CAN_MOVE = true;
    public static final float SECONDS_BETWEEN_RANDOM_MOVES = 4;

    private final String name;
    protected Interaction interaction;
    private boolean canMove = true;

    public MapNpc(String name) {
        this(name, null);
    }

    public MapNpc(String name, Location location) {
        this(name, location, Direction.WEST);
    }

    public MapNpc(String name, Location location, Direction direction) {
        this(name, location, direction, false, Interaction.NULL);
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

    @Override
    public Interaction toInteraction() {
        return interaction;
    }

    public static class Parser extends Entity.Parser<MapNpc> {
        @Override
        public MapNpc fromJson(JsonElement element) {
            JsonObject object = element.getAsJsonObject();
            String name = null;
            if (object.has("name")) {
                name = getString(object, "name");
            }
            Location location = null;
            if (has(object, Location.class)) {
                location = getObject(object, Location.class);
            }
            Direction direction = Direction.WEST;
            if (has(object, Direction.class)) {
                direction = getObject(object, Direction.class);
            }
            boolean isInteractable = false;
            if (object.has("interactable")) {
                isInteractable = getBoolean(object, "interactable");
            }
            Interaction interaction = Interaction.NULL;
            if (has(object, Interaction.class)) {
                interaction = getObject(object, Interaction.class);
            }
            boolean canMove = MapNpc.DEFAULT_CAN_MOVE;
            if (object.has("canMove")) {
                canMove = getBoolean(object, "canMove");
            }
            return new MapNpc(name, location, direction, isInteractable, interaction, canMove);
        }

        @Override
        public JsonElement toJson(MapNpc object) {
            JsonObject json = toBaseJson(object);
            addObject(json, object.getDirection(), Direction.class);
            addString(json, "name", object.name);
            addBoolean(json, "canMove", object.canMove);
            if (object.interaction != null) {
                addObject(json, object.interaction, Interaction.class);
            }
            return json;
        }
    }
}
