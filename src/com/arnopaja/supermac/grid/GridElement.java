package com.arnopaja.supermac.grid;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Superclass for things that go in a grid.
 *
 * @author Ari Weiland
 */
public abstract class GridElement {

    public static enum Direction { NORTH, EAST, SOUTH, WEST }

    protected boolean isRendered;
    protected boolean isInteractable;

    protected GridElement(boolean isRendered, boolean isInteractable) {
        this.isRendered = isRendered;
        this.isInteractable = isInteractable;
    }

    /**
     * Renders the GridElement if it is renderable, otherwise does nothing.
     * Returns whether or not something was rendered.
     *
     * @param batcher the SpriteBatch used to render the element
     * @param x the x coordinate to render at
     * @param y the y coordinate to render at
     * @return true if something was rendered, else false
     */
    public abstract boolean render(SpriteBatch batcher, float x, float y);

    public boolean isRendered() {
        return isRendered;
    }

    public void setRendered(boolean isRendered) {
        this.isRendered = isRendered;
    }

    public boolean isInteractable() {
        return isInteractable;
    }

    public void setInteractable(boolean isInteractable) {
        this.isInteractable = isInteractable;
    }
}
