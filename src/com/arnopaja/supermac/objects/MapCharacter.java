package com.arnopaja.supermac.objects;

import com.arnopaja.supermac.grid.Direction;
import com.arnopaja.supermac.grid.Grid;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

/**
 * @author Ari Weiland
 */
public abstract class MapCharacter extends Entity {

    // Ordered N-E-S-W (same as Direction order)
    protected Animation[] facingAnimations;
    protected boolean isMoving = false;
    protected Vector2 movingOffset = new Vector2(0, 0);
    protected Vector2 deltaMove;

    protected MapCharacter() {
        this(null, 0, 0, Direction.SOUTH);
    }

    protected MapCharacter(Grid grid, int x, int y, Direction facing) {
        this(grid, new Vector2(x, y), facing);
    }

    protected MapCharacter(Grid grid, Vector2 position, Direction facing) {
        this(grid, position, facing, false);
    }

    protected MapCharacter(Grid grid, int x, int y, Direction facing, boolean isInteractable) {
        this(grid, new Vector2(x, y), facing, isInteractable);
    }

    protected MapCharacter(Grid grid, Vector2 position, Direction facing, boolean isInteractable) {
        super(true, grid, position, facing, isInteractable);
    }

    @Override
    public boolean render(SpriteBatch batcher, Vector2 position, float runTime) {
        return super.render(batcher, position.cpy().add(movingOffset), runTime);
    }

    @Override
    public void update(float delta) {
        if (isMoving) {
            movingOffset.add(deltaMove.cpy().scl(delta));
        }
        if (movingOffset.isZero(ALIGN_THRESHOLD)) {
            isMoving = false;
            movingOffset = new Vector2();
        }
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

    public boolean isMoving() {
        return isMoving;
    }

    public Vector2 getMovingOffset() {
        return movingOffset;
    }

    public Animation getAnimation() {
        return getAnimation(facing);
    }

    public Animation getAnimation(Direction dir) {
        if (facingAnimations != null && facingAnimations.length == 4) {
            return facingAnimations[dir.ordinal()];
        }
        return null;
    }

    public void setFacingAnimations(Animation[] facingAnimations) {
        if (facingSprites.length != 4) {
            throw new IllegalArgumentException("Must have 4 facing animations: North, East, South, West");
        }
        this.facingAnimations = facingAnimations;
    }

    public void setAnimation(Animation animation, Direction dir) {
        facingAnimations[dir.ordinal()] = animation;
    }

    @Override
    public String toString() {
        return "MapCharacter{" +
                "grid=" + grid +
                ", position=" + position +
                ", facing=" + facing +
                ", isInteractable=" + isInteractable +
                '}';
    }
}
