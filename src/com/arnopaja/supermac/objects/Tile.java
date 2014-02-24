package com.arnopaja.supermac.objects;

import com.arnopaja.supermac.grid.GridElement;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * @author Ari Weiland
 */
public class Tile extends GridElement {

    private TextureRegion sprite;
    private boolean isPathable;

    public Tile(int x, int y,TextureRegion sprite, boolean isPathable) {
        super(true);
        this.sprite = sprite;
        this.isPathable = isPathable;
    }

    @Override
    public boolean render(SpriteBatch batcher, float x, float y) {
        batcher.draw(sprite, x, y);
        return true;
    }

    public TextureRegion getSprite() {
        return sprite;
    }

    public boolean isPathable() {
        return isPathable;
    }

    public void setSprite(TextureRegion sprite) {
        this.sprite = sprite;
    }

    public void setPathable(boolean isPathable) {
        this.isPathable = isPathable;
    }
}
