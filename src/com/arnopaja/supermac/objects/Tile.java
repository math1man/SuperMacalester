package com.arnopaja.supermac.objects;

import com.arnopaja.supermac.grid.Grid;
import com.arnopaja.supermac.helpers.AssetLoader;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Ari Weiland
 */
public class Tile implements Renderable {

    public static Map<String, TextureRegion> spriteMap;

    private final boolean isRendered;
    private TextureRegion sprite;
    private boolean isPathable;

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

    public void setSprite(TextureRegion sprite) {
        this.sprite = sprite;
    }

    public boolean isPathable() {
        return isPathable;
    }

    public static void initSpriteMap() {
        spriteMap = new HashMap<String, TextureRegion>();
        spriteMap.put("g", AssetLoader.darkgrass1);
        spriteMap.put("c", AssetLoader.cobblestone1);
        spriteMap.put("b", AssetLoader.buildingTile);
    }
}
