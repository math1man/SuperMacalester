package com.arnopaja.supermac.grid;

import com.badlogic.gdx.math.Vector2;

/**
 * Abstract superclass for things that go in a grid.
 *
 * @author Ari Weiland
 */
public abstract class GridElement {

    public static enum direction { NORTH, EAST, SOUTH, WEST }

    protected int x;
    protected int y;
    protected boolean isRendered;

    protected GridElement(int x, int y, boolean isRendered) {
        this.x = x;
        this.y = y;
        this.isRendered = isRendered;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean isRendered() {
        return isRendered;
    }

    public Vector2 getPosition() {
        return new Vector2(x, y);
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setRendered(boolean isRendered) {
        this.isRendered = isRendered;
    }

    public void setPosition (int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void setPosition (Vector2 position) {
        this.x = (int) position.x;
        this.y = (int) position.y;
    }
}
