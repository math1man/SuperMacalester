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

    protected Entity(boolean isRendered, Location location, boolean isInteractable) {
        this.isRendered = isRendered;
        putInGrid(location);
        this.isInteractable = isInteractable;
        // TODO: what about collisions?
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

    public void putInGrid(Location location) {
        this.location = location;
        if (isInGrid()) {
            // TODO: what about collisions?
            getGrid().putEntity(this);
        }
    }

    public void changeGrid(Location location) {
        if (isInGrid()) {
            getGrid().removeEntity(getPosition());
        }
        putInGrid(location);
    }

    public void removeFromGrid() {
        changeGrid(null);
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
    }

    @Override
    public String toString() {
        return "Entity{" +
                "location=" + location +
                ", isInteractable=" + isInteractable +
                ", isRendered=" + isRendered() +
                '}';
    }
}
