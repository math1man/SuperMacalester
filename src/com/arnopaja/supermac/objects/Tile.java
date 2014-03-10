package com.arnopaja.supermac.objects;

import com.arnopaja.supermac.grid.Grid;
import com.arnopaja.supermac.helpers.AssetLoader;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

/**
 * @author Ari Weiland
 */
public class Tile implements Renderable {

    private final boolean isRendered;
    private final TextureRegion sprite;
    private final boolean isPathable;

    private Tile(boolean isRendered, TextureRegion sprite, boolean isPathable) {
        this.isRendered = isRendered;
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
        if (tileCode.startsWith("n")) {
            return createNullTile();
        }
        String[] temp = tileCode.split("-");
        String tileKey = temp[0];
        TextureRegion sprite = AssetLoader.getTileSprite(tileKey);

        boolean isPathable = false;
        if (temp.length > 1) {
            for (char tileFlag : temp[1].toCharArray()) {
                // add all optional flags here
                if (tileFlag == 'p') {
                    isPathable = true;
                }
            }
        }

        return createTile(sprite, isPathable);
    }

    @Override
    public boolean render(SpriteBatch batcher, Vector2 position, float runTime) {
        if (isRendered() && sprite != null) {
            Vector2 renderPos = position.cpy().scl(Grid.GRID_PIXEL_DIMENSION);
            batcher.draw(sprite, renderPos.x, renderPos.y);
            return true;
        }
        return false;
    }

    @Override
    public boolean isRendered() {
        return isRendered;
    }

    public TextureRegion getSprite() {
        return sprite;
    }

    public boolean isPathable() {
        return isPathable;
    }
}
