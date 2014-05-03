package com.arnopaja.supermac.world.objects;

import com.arnopaja.supermac.helpers.Interaction;
import com.arnopaja.supermac.helpers.InteractionBuilder;
import com.arnopaja.supermac.world.grid.Direction;
import com.arnopaja.supermac.world.grid.Grid;
import com.arnopaja.supermac.world.grid.Location;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import java.util.Random;

/**
 * @author Ari Weiland
 */
public abstract class Entity implements Renderable, InteractionBuilder {

    public static final Random RANDOM = new Random();

    private final boolean isRendered;

    private Location location;
    private Location destination = null;
    private boolean isDelayed = false;

    private boolean isInteractable;

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
     * Delays any further grid changes until a call to forceChangeGrid() is made.
     */
    public void delay() {
        isDelayed = true;
    }

    /**
     * Removes the entity from the grid.
     * If the entity has been delayed, it will not be removed immediately.
     * Identical to calling removeFromGrid(false).
     */
    public void removeFromGrid() {
        removeFromGrid(false);
    }

    /**
     * Removes the entity from the grid.
     * If the entity has been delayed or delay is set to true,
     * it will not be removed until a call to forceChangeGrid.
     * Identical to calling changeGrid(null, delay).
     */
    public void removeFromGrid(boolean delay) {
        changeGrid(null, delay);
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
     * Moves the entity to a new location.
     * If the entity has been delayed or if delay is set to true,
     * it will not be moved until a call to forceChangeGrid().
     *
     * @param destination the new location to move to
     */
    public void changeGrid(Location destination, boolean delay) {
        this.destination = destination;
        if (delay) {
            delay();
        }
        if (!isDelayed) {
            forceChangeGrid();
        }
    }

    /**
     * Moves the entity to a new location, overriding any delay.
     *
     * @param destination the new location to move to
     */
    public void forceChangeGrid(Location destination) {
        changeGrid(destination);
        forceChangeGrid();
    }

    /**
     * Moves the entity to what ever location was most recently set as destination.
     * If the destination has not been updated since the last call, nothing will happen.
     * This method concludes any delay placed on the entity.
     */
    public void forceChangeGrid() {
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
            this.location = location;
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
}
