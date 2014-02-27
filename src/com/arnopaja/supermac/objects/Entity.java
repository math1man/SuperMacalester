package com.arnopaja.supermac.objects;

import com.arnopaja.supermac.grid.Direction;
import com.arnopaja.supermac.grid.Grid;
import com.arnopaja.supermac.grid.GridElement;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

/**
 * @author Ari Weiland
 */
public abstract class Entity extends GridElement {

    // Ordered N-E-S-W (same as Direction order)
    protected TextureRegion[] facingSprites;

    protected Grid grid;
    protected Vector2 position;
    protected Direction facing;
    protected boolean isInteractable;

    protected Entity(boolean isRendered, Grid grid, Vector2 position, Direction facing, boolean isInteractable) {
        super(isRendered);
        this.grid = grid;
        this.position = position;
        this.facing = facing;
        this.isInteractable = isInteractable;
        // TODO: what about collisions?
        this.grid.putEntity(this);
    }

    public abstract void update(float delta);

    public boolean move(Direction dir) {
        facing = dir;
        return grid.moveEntity(this, dir);
//        if (grid.moveEntity(this, dir)) {
//            setPosition(Direction.getAdjacent(position, dir));
//            return true;
//        }
//        return false;
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
        return facingSprites[dir.ordinal()];
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
