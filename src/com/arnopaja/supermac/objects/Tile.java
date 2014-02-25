package com.arnopaja.supermac.objects;

import com.arnopaja.supermac.grid.GridElement;
import com.arnopaja.supermac.helpers.AssetLoader;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * @author Ari Weiland
 */
public class Tile extends GridElement {

    private TextureRegion sprite;
    private boolean isPathable;

    private Tile(boolean isRendered, TextureRegion sprite, boolean isPathable) {
        super(isRendered, false);
        this.sprite = sprite;
        this.isPathable = isPathable;
    }

    public static Tile createNullTile() {
        return new Tile(false, null, false);
    }

    public static Tile createTile(TextureRegion sprite, boolean isPathable) {
        return new Tile(true, sprite, isPathable);
    }

    public static Tile createTile(String tileCode) {
        if (tileCode.equals("b"))
            return createTile(AssetLoader.buildingTile, false);
        else if (tileCode.equals("g"))
            return createTile(AssetLoader.groundTile, true);
        else if (tileCode.equals("p"))
            return createTile(AssetLoader.pathTile, true);
        else
            return createNullTile();
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
