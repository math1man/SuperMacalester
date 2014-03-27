package com.arnopaja.supermac.world.objects;

import com.arnopaja.supermac.helpers.Interaction;
import com.arnopaja.supermac.world.grid.Direction;
import com.arnopaja.supermac.world.grid.Grid;
import com.arnopaja.supermac.world.grid.Location;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import java.util.EnumMap;
import java.util.Random;

/**
 * @author Ari Weiland
 */
public abstract class Entity implements Renderable {

    public static final Random RANDOM = new Random();

    private final boolean isRendered;

    private Location location;
    private boolean isInteractable;

    private EnumMap<Direction, TextureRegion> facingSprites = new EnumMap<Direction, TextureRegion>(Direction.class);

    private Interaction interaction = Interaction.NULL;

    /*
     This boolean determines whether to delay moving the entity to a new grid.
     This parameter only applies to changeGrid and removeGrid methods.
     Calling delay, delayedRemoveFromGrid, or delayedChangeGrid will set this
     parameter to true.  Any further calls to these methods, removeFromGrid,
     or changeGrid will update the destination, but the entity will not be
     moved until either changeGrid() is called, or a grid change is forced by
     calling changeGrid(destination, true).

     Whenever the main character walks through a door, changeGrid will be
     called on all delayed characters. This way, the user does not see the
     entity disappear when it changes grids.
      */
    private boolean isDelayed = false;
    private Location destination = null;

    protected Entity(boolean isRendered, Location location, boolean isInteractable) {
        this.isRendered = isRendered;
        changeGrid(location);
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

    public Interaction interact(MainMapCharacter character) {
        if (isInteractable()) {
            setFacing(Direction.getDirectionToward(getPosition(), character.getPosition()));
            return getInteraction();
        }
        return Interaction.NULL;
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
        isDelayed = false;
        if (!location.equals(destination)) {
            if (isInGrid()) {
                getGrid().removeEntity(getPosition());
            }
            this.location = destination;
            if (isInGrid()) {
                // TODO: what about collisions?
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

    public Direction getFacing() {
        return location.getFacing();
    }

    public void setPosition(Vector2 position) {
        location.setPosition(position);
    }

    public void setFacing(Direction direction) {
        location.setFacing(direction);
    }

    public boolean isInteractable() {
        return isInteractable;
    }

    public void setInteractable(boolean isInteractable) {
        this.isInteractable = isInteractable;
    }

    @Override
    public TextureRegion getSprite() {
        if ((facingSprites != null)) {
            return facingSprites.get(getFacing());
        }
        return null;
    }

    @Override
    public TextureRegion getSprite(float runTime) {
        return getSprite();
    }

    public void setFacingSprites(EnumMap<Direction, TextureRegion> facingSprites) {
        this.facingSprites = facingSprites;
    }

    public Interaction getInteraction() {
        return interaction;
    }

    public void setInteraction(Interaction interaction) {
        this.interaction = interaction;
        setInteractable(true);
    }
}
