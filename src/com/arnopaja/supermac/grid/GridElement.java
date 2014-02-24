package com.arnopaja.supermac.grid;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Superclass for things that go in a grid.
 *
 * @author Ari Weiland
 */
public abstract class GridElement {

    public static enum direction { NORTH, EAST, SOUTH, WEST }

    protected boolean isRendered;

    protected GridElement(boolean isRendered) {
        this.isRendered = isRendered;
    }

    /**
     * Renders the grid element if it is renderable, otherwise does nothing.
     *
     *
     * @param batcher the SpriteBatch used to render the element
     * @param x
     *@param y @return true if something was rendered, else false
     */
    public abstract boolean render(SpriteBatch batcher, float x, float y);

    public boolean isRendered() {
        return isRendered;
    }

    public void setRendered(boolean isRendered) {
        this.isRendered = isRendered;
    }
}
