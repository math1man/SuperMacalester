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

    protected Entity(boolean isRendered, boolean isInteractable) {
        super(isRendered, isInteractable);
    }

    public boolean move(Direction dir) {
        Tile element = grid.getTile(Direction.getAdjacent(position, dir));
        // TODO: add entity collision in addition to tile pathing
        if ((element != null) && (element.isPathable())) {
            position = Direction.getAdjacent(position, dir);
            grid.moveEntity(this, dir);
            return true;
        }
        return false;
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
        position.set(x, position.y);
    }

    public void setY(int y) {
        position.set(position.x, y);
    }

    public void setPosition (int x, int y) {
        setX(x);
        setY(y);
    }

    public void setPosition(Vector2 position) {
        this.position = position;
    }

    public void setFacing(Direction facing) {
        this.facing = facing;
    }
}
