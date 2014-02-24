package com.arnopaja.supermac.grid;

/**
 * Superclass for things that go in a grid.
 *
 * @author Ari Weiland
 */
public class GridElement {

    public static enum direction { NORTH, EAST, SOUTH, WEST }

    protected boolean isRendered;

    protected GridElement(boolean isRendered) {
        this.isRendered = isRendered;
    }

    public boolean isRendered() {
        return isRendered;
    }

    public void setRendered(boolean isRendered) {
        this.isRendered = isRendered;
    }
}
