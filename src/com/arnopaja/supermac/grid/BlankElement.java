package com.arnopaja.supermac.grid;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * @author Ari Weiland
 */
public class BlankElement extends GridElement {

    protected BlankElement() {
        super(false);
    }

    @Override
    public boolean render(SpriteBatch batcher, float x, float y) {
        return false;
    }
}
