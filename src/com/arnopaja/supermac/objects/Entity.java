package com.arnopaja.supermac.objects;

import com.arnopaja.supermac.grid.Direction;
import com.arnopaja.supermac.grid.Grid;
import com.arnopaja.supermac.helpers.Dialogue;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

/**
 * @author Ari Weiland
 */
public abstract class Entity implements Renderable {

    private final boolean isRendered;
    private Grid grid;
    private Vector2 position;
    private Direction facing;
    private boolean isInteractable;

    // Ordered N-E-S-W (same as Direction order)
    private TextureRegion[] facingSprites;
    private TextureRegion currentSprite;

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
    public boolean render(SpriteBatch batcher, Vector2 position, float runTime) {
        if (isRendered() && getSprite(runTime) != null) {
            Vector2 renderPos = position.cpy().scl(Grid.GRID_PIXEL_DIMENSION);
            batcher.draw(getSprite(runTime), renderPos.x, renderPos.y);
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

    public TextureRegion getSprite() {
        if ((facingSprites != null) && (facingSprites.length == 4)) {
            return facingSprites[facing.ordinal()];
        }
        return null;
    }

    public TextureRegion getSprite(float runTime) {
        return getSprite();
    }

    public void setFacingSprites(TextureRegion[] facingSprites) {
        if (facingSprites.length != 4) {
            throw new IllegalArgumentException("Must have 4 facing sprites: North, East, South, West");
        }
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
