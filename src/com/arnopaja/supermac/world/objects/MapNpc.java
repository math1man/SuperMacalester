package com.arnopaja.supermac.world.objects;

import com.arnopaja.supermac.helpers.CharacterAsset;
import com.arnopaja.supermac.helpers.Interaction;
import com.arnopaja.supermac.world.grid.Direction;
import com.arnopaja.supermac.world.grid.Location;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * @author Ari Weiland
 */
public class MapNpc extends MapCharacter {

    public static final boolean DEFAULT_INTERACTABLE = false;
    public static final boolean DEFAULT_CAN_MOVE = true;
    public static final float SECONDS_BETWEEN_RANDOM_MOVES = 4;

    private boolean canMove = true;

    public MapNpc() {
        this(null);
    }

    public MapNpc(Location location) {
        this(location, Direction.WEST);
    }

    public MapNpc(Location location, Direction direction) {
        this(location, direction, DEFAULT_INTERACTABLE);
    }

    public MapNpc(Location location, Direction direction, boolean isInteractable) {
        this(location, direction, isInteractable, DEFAULT_CAN_MOVE);
    }

    public MapNpc(Location location, Direction direction, boolean isInteractable, boolean canMove) {
        this(location, direction, isInteractable, canMove, null);
    }

    public MapNpc(Location location, Direction direction, boolean isInteractable, boolean canMove,
                  CharacterAsset asset) {
        super(location, direction, isInteractable, asset);
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

    public void setInteraction(Interaction interaction) {
        this.interaction = interaction;
    }

    public void makeQuestNpc() {
        isQuestEntity = true;
    }

    public boolean canMove() {
        return canMove;
    }

    public void setCanMove(boolean canMove) {
        this.canMove = canMove;
    }

    public static class Parser extends Entity.Parser<MapNpc> {
        @Override
        public MapNpc fromJson(JsonElement element) {
            JsonObject object = element.getAsJsonObject();
            Location location = null;
            if (has(object, Location.class)) {
                location = getObject(object, Location.class);
            }
            Direction direction = Direction.WEST;
            if (has(object, Direction.class)) {
                direction = getObject(object, Direction.class);
            }
            boolean isInteractable = MapNpc.DEFAULT_INTERACTABLE;
            if (object.has("interactable")) {
                isInteractable = getBoolean(object, "interactable");
            }
            boolean canMove = MapNpc.DEFAULT_CAN_MOVE;
            if (object.has("canMove")) {
                canMove = getBoolean(object, "canMove");
            }
            MapNpc npc = new MapNpc(location, direction, isInteractable, canMove);
            if (has(object, Interaction.class)) {
                npc.setInteraction(getObject(object, Interaction.class));
            }
            return npc;
        }

        @Override
        public JsonElement toJson(MapNpc object) {
            JsonObject json = toBaseJson(object);
            addObject(json, object.getDirection(), Direction.class);
            addBoolean(json, "canMove", object.canMove);
            if (object.interaction != null) {
                addObject(json, object.interaction, Interaction.class);
            }
            return json;
        }
    }
}
