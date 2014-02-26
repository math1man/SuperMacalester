package com.arnopaja.supermac.objects;

import com.arnopaja.supermac.grid.GridElement;
import com.arnopaja.supermac.helpers.AssetLoader;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * @author Ari Weiland
 */
public class Tile extends GridElement {

    public enum TileType {
        BUILDING("b", true, AssetLoader.buildingTile, false),
        GROUND("g", true, AssetLoader.groundTile, true),
        PATH("p", true, AssetLoader.pathTile, true),
        NULL("n", false, null, false);

        public final String tileCode;
        public final boolean isRendered;
        public final TextureRegion sprite; // TODO: resolve this error
        public final boolean isPathable;

        private TileType(String tileCode, boolean isRendered, TextureRegion sprite, boolean isPathable) {
            this.tileCode = tileCode;
            this.isRendered = isRendered;
            this.sprite = sprite;
            this.isPathable = isPathable;
        }

        public static TileType getTileType(String tileCode) {
            for (TileType tt : TileType.values()) {
                if (tt.tileCode.equals(tileCode)) {
                    return tt;
                }
            }
            return NULL;
        }
    }

    private TileType type;
    private TextureRegion sprite;
    private boolean isPathable;

    private Tile(TileType type, boolean isRendered, TextureRegion sprite, boolean isPathable) {
        super(isRendered, false);
        this.type = type;
        this.sprite = sprite;
        this.isPathable = isPathable;
    }

    public static Tile createNullTile() {
        return createTile(TileType.NULL);
    }

    public static Tile createTile(TextureRegion sprite, boolean isPathable) {
        return new Tile(null, true, sprite, isPathable);
    }

    public static Tile createTile(String tileCode) {
        return createTile(TileType.getTileType(tileCode));
    }

    private static Tile createTile(TileType tileType) {
        return new Tile(tileType, tileType.isRendered, tileType.sprite, tileType.isPathable);
    }

    @Override
    public boolean render(SpriteBatch batcher, float x, float y) {
        batcher.draw(sprite, x, y);
        return true;
    }

    public TileType getType() {
        return type;
    }

    public TextureRegion getSprite() {
        return sprite;
    }

    public boolean isPathable() {
        return isPathable;
    }

    public void setType(TileType type) {
        this.type = type;
        isRendered = type.isRendered;
        sprite = type.sprite;
        isPathable = type.isPathable;
    }

    public void setSprite(TextureRegion sprite) {
        this.sprite = sprite;
    }

    public void setPathable(boolean isPathable) {
        this.isPathable = isPathable;
    }
}
