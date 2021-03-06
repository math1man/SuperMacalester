package com.arnopaja.supermac.world.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

/**
 * Interface for things rendered in the grid.
 *
 * @author Ari Weiland
 */
public interface Renderable {

    /**
     * Renders the Renderable if it is renderable, otherwise does nothing.
     * Returns whether or not something was rendered.
     *
     * @param batch the SpriteBatch used to render the element
     * @param position the coordinates to render at
     * @param runTime
     * @return true if something was rendered, else false
     */
    public abstract boolean render(SpriteBatch batch, Vector2 position, float runTime);
    public abstract boolean isRendered();
    public abstract TextureRegion getSprite(float runTime);
}
