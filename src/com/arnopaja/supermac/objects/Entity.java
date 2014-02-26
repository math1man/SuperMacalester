package com.arnopaja.supermac.objects;

import com.arnopaja.supermac.grid.Direction;
import com.arnopaja.supermac.grid.Grid;
import com.arnopaja.supermac.grid.GridElement;
import com.badlogic.gdx.math.Vector2;

/**
 * @author Ari Weiland
 */
public abstract class Entity extends GridElement {

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

    // TODO: should this and the changeGrid methods maybe be in character instead?
    public boolean move(Direction dir) {
        facing = dir;
        if (grid.moveEntity(this, dir)) {
            setPosition(Direction.getAdjacent(position, dir));
            return true;
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

    public int getX() {
        return (int) position.x;
    }

    public int getY() {
        return (int) position.y;
    }

    public Vector2 getPosition() {
        return position;
    }

    public Direction getFacing() {
        return facing;
    }

    public void setX(int x) {
        setPosition(x, position.y);
    }

    public void setY(int y) {
        setPosition(position.x, y);
    }

    public void setPosition (float x, float y) {
        position.set(x, y);
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
}
