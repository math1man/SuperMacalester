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
    private boolean canMove;

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

    public String getName() {
        return name;
    }

    @Override
    public Interaction toInteraction() {
        return interaction;
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

    public static class Parser extends Entity.Parser<MapNpc> {
        @Override
        public MapNpc fromJson(JsonElement element) {
            JsonObject object = element.getAsJsonObject();
            String name = null;
            if (object.has("name")) {
                name = getString(object, "name");
                System.out.println("My name is " + name);
            }
            Location location = null;
            if (has(object, Location.class)) {
                location = getObject(object, Location.class);
                System.out.println("I am at " + location);
            }
            Direction direction = Direction.WEST;
            if (has(object, Direction.class)) {
                direction = getObject(object, Direction.class);
            }
            boolean isInteractable = false;
            if (object.has("interactable")) {
                System.out.println("I am interactable!");
                isInteractable = getBoolean(object, "interactable");
            }
            Interaction interaction = Interaction.NULL;
            if (has(object, Interaction.class)) {
                interaction = getObject(object, Interaction.class);
                System.out.println("I have an interaction");
            }
            boolean canMove = MapNpc.DEFAULT_CAN_MOVE;
            if (object.has("canMove")) {
                canMove = getBoolean(object, "canMove");
                System.out.println("I can move");
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
