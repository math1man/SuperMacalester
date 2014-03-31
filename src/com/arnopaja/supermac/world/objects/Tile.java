package com.arnopaja.supermac.world.objects;

import com.arnopaja.supermac.helpers.MapLoader;
import com.arnopaja.supermac.world.grid.Grid;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

/**
 * @author Ari Weiland
 */
public class Tile implements Renderable {

    public static final Tile NULL = new Tile(false, null, false);

    private final boolean isRendered;
    private final TextureRegion sprite;
    private final boolean isPathable;

    private Tile(boolean isRendered, TextureRegion sprite, boolean isPathable) {
        this.isRendered = isRendered;
        this.sprite = sprite;
        this.isPathable = isPathable;
    }

    public static Tile createTile(TextureRegion sprite, boolean isPathable) {
        return new Tile(true, sprite, isPathable);
    }

    public static Tile createTile(String tileCode) {
        if (tileCode.isEmpty()) {
            return NULL;
        }
        String[] temp = tileCode.split("-");
        String tileKey = temp[0];
        TextureRegion sprite = MapLoader.TILE_MAP.get(tileKey);
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
        return createTile(sprite, isPathable);
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
        return sprite != null && !(sprite.getRegionWidth() == 32 && sprite.getRegionHeight() == 32);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Tile)) return false;

        Tile tile = (Tile) o;

        return (!isRendered && !tile.isRendered) || (isPathable == tile.isPathable
                && (sprite != null ? sprite.equals(tile.sprite) : tile.sprite == null));


    }
}
