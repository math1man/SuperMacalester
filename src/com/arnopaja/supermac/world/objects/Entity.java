package com.arnopaja.supermac.world.objects;

import com.arnopaja.supermac.helpers.Dialogue;
import com.arnopaja.supermac.helpers.Interaction;
import com.arnopaja.supermac.world.grid.Direction;
import com.arnopaja.supermac.world.grid.Grid;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import java.util.EnumMap;

/**
 * @author Ari Weiland
 */
public abstract class Entity implements Renderable {

    private final boolean isRendered;

    private Grid grid;
    private Vector2 position;
    private Direction facing;
    private boolean isInteractable;

    private EnumMap<Direction, TextureRegion> facingSprites = new EnumMap<Direction, TextureRegion>(Direction.class);

    private Dialogue dialogue;

    protected Entity(boolean isRendered, Grid grid, Vector2 position, Direction facing, boolean isInteractable) {
        this.isRendered = isRendered;
        this.grid = grid;
        this.position = position;
        this.facing = facing;
        this.isInteractable = isInteractable;
        // TODO: what about collisions?
        this.grid.putEntity(this);
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

    public abstract Interaction getInteraction(MainMapCharacter character);

    public Grid getGrid() {
        return grid;
    }

    public Vector2 getPosition() {
        return position;
    }

    public Direction getFacing() {
        return facing;
    }

    public void setGrid(Grid grid) {
        this.grid = grid;
    }

    public void setPosition(Vector2 position) {
        this.position = position;
    }

    public void setFacing(Direction facing) {
        this.facing = facing;
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
            return facingSprites.get(facing);
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

    public Dialogue getDialogue() {
        return dialogue;
    }

    public void setDialogue(Dialogue dialogue) {
        this.dialogue = dialogue;
    }

    @Override
    public String toString() {
        return "Entity{" +
                "grid=" + grid +
                ", position=" + position +
                ", facing=" + facing +
                ", isInteractable=" + isInteractable +
                ", isRendered=" + isRendered() +
                '}';
    }
}
