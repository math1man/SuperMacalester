package com.arnopaja.supermac.world.objects;

import com.arnopaja.supermac.world.grid.Grid;
import com.badlogic.gdx.graphics.g2d.Animation;
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
    private final Animation animation;
    private final boolean isPathable;

    private Tile(String tileKey, boolean isRendered, Animation animation, boolean isPathable) {
        this.tileKey = tileKey;
        this.isRendered = isRendered;
        this.animation = animation;
        this.isPathable = isPathable;
    }

    public static Tile createTile(String tileKey, Animation animation, boolean isPathable) {
        return new Tile(tileKey, true, animation, isPathable);
    }

    public static Tile createTile(String tileCode) {
        if (tileCode.isEmpty()) {
            return NULL;
        }
        String[] temp = tileCode.split("-");
        String tileKey = temp[0];
        Animation animation = TILE_MAP.get(tileKey);
        if (animation == null) {
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
        return createTile(tileKey, animation, isPathable);
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

    @Override
    public TextureRegion getSprite(float runTime) {
        // TODO: this shouldn't need the second param, but looping doesn't work otherwise
        return animation.getKeyFrame(runTime, true);
    }

    public Animation getAnimation() {
        return animation;
    }

    public boolean isPathable() {
        return isPathable;
    }

    public boolean isLarge() {
        if (animation != null) {
            TextureRegion sprite = animation.getKeyFrames()[0];
            return sprite != null && !(sprite.getRegionWidth() == Grid.GRID_PIXEL_DIMENSION
                    && sprite.getRegionHeight() == Grid.GRID_PIXEL_DIMENSION);
        }
        return false;
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
                && (animation != null ? animation.equals(tile.animation) : tile.animation == null));


    }

    public static class TileMap {
        private static final Random random = new Random();

        private final Map<String, Animation> tileMap = new HashMap<String, Animation>();
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
            Animation[] animations = new Animation[length];
            for (int i=0; i<length; i++) {
                animations[i] = new Animation(1, sprites[i]);
            }
            put(code, animations);
        }

        /**
         * Notes:
         * _ is a prefix for large tiles
         * only letters should be used otherwise
         * @param code
         * @param animations
         */
        public void put(String code, Animation... animations) {
            int length = animations.length;
            if (length > 1) {
                randomTileMap.put(code, length);
                for (int i=0; i<length; i++) {
                    tileMap.put(code + i, animations[i]);
                }
            } else {
                tileMap.put(code, animations[0]);
            }
        }

        public Animation get(String tileKey) {
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
