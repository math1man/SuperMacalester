package com.arnopaja.supermac.objects;

import com.arnopaja.supermac.grid.Direction;
import com.arnopaja.supermac.grid.Grid;
import com.arnopaja.supermac.render.Renderable;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

/**
 * @author Ari Weiland
 */
public abstract class Entity extends Renderable {

    public static final float MOVE_SPEED = 3f; // grid spaces per second
    public static final float ALIGN_THRESHOLD = 0.001f;

    // Ordered N-E-S-W (same as Direction order)
    protected TextureRegion[] facingSprites;

    protected Grid grid;
    protected Vector2 position;
    protected Direction facing;
    protected boolean isInteractable;

    protected boolean isMoving = false;
    protected Vector2 movingOffset = new Vector2(0, 0);
    protected Vector2 deltaMove;

    protected Entity(boolean isRendered, Grid grid, Vector2 position, Direction facing, boolean isInteractable) {
        super(isRendered);
        this.grid = grid;
        this.position = position;
        this.facing = facing;
        this.isInteractable = isInteractable;
        // TODO: what about collisions?
        this.grid.putEntity(this);
    }

    public void update(float delta) {
        if (isMoving) {
            movingOffset.add(deltaMove.cpy().scl(delta));
        }
        if (movingOffset.isZero(ALIGN_THRESHOLD)) {
            isMoving = false;
            movingOffset = new Vector2();
        }
    }

    @Override
    public boolean render(SpriteBatch batcher, Vector2 position, float runTime) {
        if (isRendered && getSprite(runTime) != null) {
            Vector2 renderPos = position.cpy().add(movingOffset).scl(Grid.GRID_PIXEL_DIMENSION);
            batcher.draw(getSprite(runTime), renderPos.x, renderPos.y);
            return true;
        }
        return false;
    }

    public boolean move(Direction dir) {
        if (!isMoving) {
            facing = dir;
            if (grid.moveEntity(this, dir)) {
                isMoving = true;
                movingOffset = Direction.getAdjacent(dir).scl(-1);
                deltaMove = movingOffset.cpy().scl(-MOVE_SPEED);
                return true;
            }
        }
        return false;
    }

    public void changeGrid(Grid newGrid, int x, int y) {
        changeGrid(newGrid, new Vector2(x, y));
    }

    public void changeGrid(Grid newGrid, Vector2 position) {
        grid.removeEntity(position);
        grid = newGrid;
        // TODO: what about collisions?
        newGrid.putEntity(this, position);
    }

    public Grid getGrid() {
        return grid;
    }

    public Vector2 getPosition() {
        return position;
    }

    public Direction getFacing() {
        return facing;
    }

    public void setPosition (int x, int y) {
        setPosition(new Vector2(x, y));
    }

    public void setPosition(Vector2 position) {
        this.position = position;
    }

    public boolean isMoving() {
        return isMoving;
    }

    public Vector2 getMovingOffset() {
        return movingOffset;
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
        return getSprite(facing);
    }

    public TextureRegion getSprite(Direction dir) {
        return getSprite(dir, 0);
    }

    public TextureRegion getSprite(float runTime) {
        return getSprite(facing, runTime);
    }

    public TextureRegion getSprite(Direction dir, float runTime) {
        if ((facingSprites != null) && (facingSprites.length == 4)) {
            return facingSprites[dir.ordinal()];
        }
        return null;
    }

    public void setFacingSprites(TextureRegion[] facingSprites) {
        if (facingSprites.length != 4) {
            throw new IllegalArgumentException("Must have 4 facing sprites: North, East, South, West");
        }
        this.facingSprites = facingSprites;
    }

    public void setSprite(TextureRegion sprite, Direction dir) {
        facingSprites[dir.ordinal()] = sprite;
    }

    @Override
    public String toString() {
        return "Entity{" +
                "grid=" + grid +
                ", position=" + position +
                ", facing=" + facing +
                ", isInteractable=" + isInteractable +
                ", isRendered=" + isRendered +
                '}';
    }
}
