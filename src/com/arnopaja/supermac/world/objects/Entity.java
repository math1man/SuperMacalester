package com.arnopaja.supermac.world.objects;

import com.arnopaja.supermac.helpers.Interaction;
import com.arnopaja.supermac.helpers.ToInteraction;
import com.arnopaja.supermac.helpers.SuperParser;
import com.arnopaja.supermac.world.grid.Direction;
import com.arnopaja.supermac.world.grid.Grid;
import com.arnopaja.supermac.world.grid.Location;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * @author Ari Weiland
 */
public abstract class Entity implements Renderable, ToInteraction {

    public static final Random RANDOM = new Random();

    private final boolean isRendered;

    private Location location;
    private Location destination = null;
    private boolean isDelayed = false;

    private boolean isInteractable;
    protected boolean isQuestEntity = false;

    protected Entity(boolean isRendered, Location location, boolean isInteractable) {
        this.isRendered = isRendered;
        putInGrid(location);
        this.isInteractable = isInteractable;
    }

    @Override
    public boolean render(SpriteBatch batch, Vector2 position, float runTime) {
        if (isRendered() && getSprite(runTime) != null) {
            Vector2 renderPos = position.cpy().scl(Grid.GRID_PIXEL_DIMENSION);
            batch.draw(getSprite(runTime), renderPos.x, renderPos.y);
            return true;
        }
        return false;
    }

    @Override
    public boolean isRendered() {
        return isRendered;
    }

    public abstract void update(float delta);

    public Interaction interact() {
        if (isInteractable) {
            return toInteraction();
        } else {
            return Interaction.NULL;
        }
    }

    /**
     * Removes the entity from the grid.
     * If the entity has been delayed, it will not be removed immediately.
     */
    public void removeFromGrid() {
        changeGrid(null);
    }

    /**
     * Moves the entity to a new location.
     * If the entity has been delayed, it will not be moved immediately.
     * Identical to calling changeGrid(destination, false).
     *
     * @param destination the new location to move to
     */
    public void changeGrid(Location destination) {
        changeGrid(destination, false);
    }

    /**
     * Delays any further grid changes until a call to changeGrid() is made.
     */
    public void delay() {
        isDelayed = true;
    }

    /**
     * Removes the entity from the grid eventually.
     * A call to changeGrid() must be made to move the entity.
     */
    public void delayedRemoveFromGrid() {
        delayedChangeGrid(null);
    }

    /**
     * Moves the entity to a new location eventually.
     * A call to changeGrid() must be made to move the entity.
     *
     * @param destination the new location to move to
     */
    public void delayedChangeGrid(Location destination) {
        delay();
        changeGrid(destination, false);
    }

    /**
     * Moves the entity to a new location.
     * If force is set to true, the entity will move whether or not
     * it was previously delayed.
     *
     * @param destination the new location to move to
     * @param force forces the entity to move despite a delay
     */
    public void changeGrid(Location destination, boolean force) {
        this.destination = destination;
        if (force || !isDelayed) {
            changeGrid();
        }
    }

    /**
     * Moves the entity to what ever location was most recently set as destination.
     * If the destination has not been updated since the last call, nothing will happen.
     * This method concludes any delay placed on the entity.
     */
    public void changeGrid() {
        isQuestEntity = false;
        isDelayed = false;
        if (location == null || !location.equals(destination)) {
            if (isInGrid()) {
                getGrid().removeEntity(getPosition());
            }
            putInGrid(destination);
        }
    }

    private void putInGrid(Location location) {
        if (location != null) {
            this.location = location; // TODO: getNearestValidLocation()?
            if (isInGrid()) {
                getGrid().putEntity(this);
            }
        }
    }

    public boolean isDelayed() {
        return isDelayed;
    }

    public boolean isInGrid() {
        return location != null;
    }

    public Location getLocation() {
        return location;
    }

    public Grid getGrid() {
        return location.getGrid();
    }

    public Vector2 getPosition() {
        return location.getPosition();
    }

    public void setPosition(Vector2 position) {
        location.setPosition(position);
    }


    public boolean isInteractable() {
        return isInteractable;
    }

    public void setInteractable(boolean isInteractable) {
        this.isInteractable = isInteractable;
    }

    public Direction getDirectionToward(Vector2 position) {
        return Direction.getDirectionToward(getPosition(), position);
    }

    public boolean isQuestEntity() {
        return isQuestEntity;
    }

    public static class Parser<T extends Entity> extends SuperParser<T> {

        private static final Map<String, Parser> parsers = new HashMap<String, Parser>();

        static {
            parsers.put(MainMapCharacter.class.getSimpleName(), new MainMapCharacter.Parser());
            parsers.put(MapNpc.class.getSimpleName(), new MapNpc.Parser());
            parsers.put(Door.class.getSimpleName(), new Door.Parser());
            parsers.put(Chest.class.getSimpleName(), new Chest.Parser());
        }

        @Override
        public T fromJson(JsonElement element) {
            JsonObject entity = element.getAsJsonObject();
            String className = getClass(entity);
            Parser<T> parser = parsers.get(className);
            return parser.fromJson(element);
        }

        @Override
        public JsonElement toJson(T object) {
            Parser parser = parsers.get(object.getClass().getSimpleName());
            return parser.toJson(object);
        }

        protected JsonObject toBaseJson(T object) {
            JsonObject json = new JsonObject();
            if (object.getLocation() != null) {
                addObject(json, object.getLocation(), Location.class);
            }
            addBoolean(json, "interactable", object.isInteractable());
            addClass(json, object.getClass());
            return json;
        }
    }
}
