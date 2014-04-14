package com.arnopaja.supermac.world.objects;

import com.arnopaja.supermac.helpers.Interaction;
import com.arnopaja.supermac.world.grid.Direction;
import com.arnopaja.supermac.world.grid.Location;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.EnumMap;

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
        this(location, DEFAULT_INTERACTABLE);
    }

    public MapNpc(Location location, boolean isInteractable) {
        this(location, isInteractable, DEFAULT_CAN_MOVE);
    }

    public MapNpc(Location location, boolean isInteractable, boolean canMove) {
        this(location, isInteractable, canMove, null, null);
    }

    public MapNpc(Location location, boolean isInteractable, boolean canMove,
                  EnumMap<Direction, TextureRegion> facingSprites,
                  EnumMap<Direction, Animation> facingAnimations) {
        super(location, isInteractable, facingSprites, facingAnimations);
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

    public static class Parser extends Entity.Parser<MapNpc> {
        @Override
        public MapNpc convert(JsonElement element) {
            JsonObject object = element.getAsJsonObject();
            Location location = null;
            if (object.has("location")) {
                location = convert(object.getAsJsonObject("location"), Location.class);
            }
            boolean isInteractable = MapNpc.DEFAULT_INTERACTABLE;
            if (object.has("interactable")) {
                isInteractable = object.getAsJsonPrimitive("interactable").getAsBoolean();
            }
            boolean canMove = MapNpc.DEFAULT_CAN_MOVE;
            if (object.has("canMove")) {
                canMove = object.getAsJsonPrimitive("canMove").getAsBoolean();
            }
            MapNpc npc = new MapNpc(location, isInteractable, canMove);
            if (object.has("interaction")) {
                npc.setInteraction(convert(object.getAsJsonObject("interaction"), Interaction.class));
            }
            return npc;
        }
    }
}
