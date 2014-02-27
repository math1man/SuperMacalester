package com.arnopaja.supermac.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Superclass for things that are rendered.
 *
 * @author Ari Weiland
 */
public abstract class Renderable {

    protected boolean isRendered;

    protected Renderable(boolean isRendered) {
        this.isRendered = isRendered;
    }

    /**
     * Renders the Renderable if it is renderable, otherwise does nothing.
     * Returns whether or not something was rendered.
     *
     *
     * @param batcher the SpriteBatch used to render the element
     * @param x the x coordinate to render at
     * @param y the y coordinate to render at
     * @param runTime
     * @return true if something was rendered, else false
     */
    public abstract boolean render(SpriteBatch batcher, float x, float y, float runTime);

    public boolean isRendered() {
        return isRendered;
    }

    public void setRendered(boolean isRendered) {
        this.isRendered = isRendered;
    }
}
