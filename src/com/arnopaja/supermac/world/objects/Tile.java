package com.arnopaja.supermac.world.objects;

import com.arnopaja.supermac.world.grid.Grid;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * @author Ari Weiland
 */
public class Tile implements Renderable {

    public static final Tile NULL = new Tile("", false, null, false);
    public static final TileMap TILE_MAP = new TileMap();

    private final String tileKey; // primarily for debugging
    private final boolean isRendered;
    private final TextureRegion sprite;
    private final boolean isPathable;

    private Tile(String tileKey, boolean isRendered, TextureRegion sprite, boolean isPathable) {
        this.tileKey = tileKey;
        this.isRendered = isRendered;
        this.sprite = sprite;
        this.isPathable = isPathable;
    }

    public static Tile createTile(String tileCode, TextureRegion sprite, boolean isPathable) {
        return new Tile(tileCode, true, sprite, isPathable);
    }

    public static Tile createTile(String tileCode) {
        if (tileCode.isEmpty()) {
            return NULL;
        }
        String[] temp = tileCode.split("-");
        String tileKey = temp[0];
        TextureRegion sprite = TILE_MAP.get(tileKey);
        if (sprite == null) {
            return NULL;
        }

        boolean isPathable = !tileKey.startsWith("_");
        if (temp.length > 1) {
            for (char tileFlag : temp[1].toCharArray()) {
                // add all optional flags here
                if (tileFlag == 'i') {
                    isPathable = false;
                }
            }
        }
        return createTile(tileKey, sprite, isPathable);
    }

    @Override
    public boolean render(SpriteBatch batch, Vector2 position, float runTime) {
        if (isRendered() && getSprite(runTime) != null) {
            Vector2 renderPos = position.cpy().scl(Grid.GRID_PIXEL_DIMENSION);
            batch.draw(getSprite(runTime), renderPos.x, renderPos.y);
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

    @Override
    public TextureRegion getSprite(float runTime) {
        return getSprite();
    }

    public boolean isPathable() {
        return isPathable;
    }

    public boolean isLarge() {
        return sprite != null && !(sprite.getRegionWidth() == Grid.GRID_PIXEL_DIMENSION
                && sprite.getRegionHeight() == Grid.GRID_PIXEL_DIMENSION);
    }

    public String getTileKey() {
        return tileKey;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Tile)) return false;

        Tile tile = (Tile) o;

        return (!isRendered && !tile.isRendered) || (isPathable == tile.isPathable
                && (sprite != null ? sprite.equals(tile.sprite) : tile.sprite == null));


    }

    public static class TileMap {
        private static final Random random = new Random();

        private final Map<String, TextureRegion> tileMap = new HashMap<String, TextureRegion>();
        private final Map<String, Integer> randomTileMap = new HashMap<String, Integer>();

        /**
         * Notes:
         * _ is a prefix for large tiles
         * only letters should be used otherwise
         * @param code
         * @param sprites
         */
        public void put(String code, TextureRegion... sprites) {
            int length = sprites.length;
            if (length > 1) {
                randomTileMap.put(code, length);
                for (int i=0; i<length; i++) {
                    tileMap.put(code + i, sprites[i]);
                }
            } else {
                tileMap.put(code, sprites[0]);
            }
        }

        public TextureRegion get(String tileKey) {
            tileKey = tileKey.trim();
            if (randomTileMap.containsKey(tileKey)) {
                tileKey += random.nextInt(randomTileMap.get(tileKey));
            }
            return tileMap.get(tileKey);
        }

        public boolean contains(String tileKey) {
            return tileMap.containsKey(tileKey) || randomTileMap.containsKey(tileKey);
        }
    }
}
