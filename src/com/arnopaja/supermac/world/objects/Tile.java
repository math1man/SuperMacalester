package com.arnopaja.supermac.world.objects;

import com.arnopaja.supermac.helpers.load.AssetLoader;
import com.arnopaja.supermac.world.grid.Grid;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

/**
 * @author Ari Weiland
 */
public class Tile implements Renderable {

    public static final Tile NULL = new Tile("", false, null, false);

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
        Animation animation = AssetLoader.getTile(tileKey);
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

}
