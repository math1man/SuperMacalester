package com.arnopaja.supermac.objects;

import com.arnopaja.supermac.grid.Grid;
import com.arnopaja.supermac.helpers.AssetLoader;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Ari Weiland
 */
public class Tile extends Renderable {

    public static Map<String, TextureRegion> spriteMap;

    private TextureRegion sprite;
    private boolean isPathable;

    private Tile(boolean isRendered, TextureRegion sprite, boolean isPathable) {
        super(isRendered);
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
        char[] tileFlags = tileCode.toCharArray();
        if (tileFlags[0] == 'n') {
            return createNullTile();
        }
        TextureRegion sprite = spriteMap.get("" + tileFlags[0]);
        boolean isPathable = false;
        for (int i=1; i<tileFlags.length; i++) {
            // add all optional flags here
            if (tileFlags[i] == 'p') {
                isPathable = true;
            }
        }
        return createTile(sprite, isPathable);
    }

    @Override
    public boolean render(SpriteBatch batcher, float x, float y, float runTime) {
        if (isRendered && sprite != null) {
            batcher.draw(sprite, x * Grid.GRID_PIXEL_DIMENSION, y * Grid.GRID_PIXEL_DIMENSION);
            return true;
        }
        return false;
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

    public static void initSpriteMap() {
        spriteMap = new HashMap<String, TextureRegion>();
        spriteMap.put("g", AssetLoader.darkgrass1);
        spriteMap.put("c", AssetLoader.cobblestone1);
        spriteMap.put("b", AssetLoader.buildingTile);
    }
}
