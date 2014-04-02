package com.arnopaja.supermac.helpers;

import com.arnopaja.supermac.world.objects.Tile;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * @author Ari Weiland
 */
public class SpriteUtils {

    /**
     * Creates a TextureRegion from the texture, x, y, width, and height parameters
     * using the default flip orientation of (false, true).
     * @param texture
     * @param x
     * @param y
     * @param width
     * @param height
     * @return
     */
    public static TextureRegion makeSprite(Texture texture, int x, int y, int width, int height) {
        return makeSprite(texture, x, y, width, height, false, true);
    }

    /**
     * Creates a TextureRegion from the texture, x, y, width, and height parameters
     * and flipping it according to the flipX and flipY parameters.
     * @param texture
     * @param x
     * @param y
     * @param width
     * @param height
     * @param flipX
     * @param flipY
     * @return
     */
    public static TextureRegion makeSprite(Texture texture, int x, int y,
                                           int width, int height, boolean flipX, boolean flipY) {
        TextureRegion sprite = new TextureRegion(texture, x, y, width, height);
        sprite.flip(flipX, flipY);
        return sprite;
    }

    /**
     * Creates a copy of the specified TextureRegion that is flipped
     * according to the flipX and flipY parameters.
     * @param sprite
     * @param flipX
     * @param flipY
     * @return
     */
    public static TextureRegion makeFlipped(TextureRegion sprite, boolean flipX, boolean flipY) {
        TextureRegion flipped = new TextureRegion(sprite);
        sprite.flip(flipX, flipY);
        return flipped;
    }

    /**
     * These methods should be used instead of the TextureRegion.split()
     * methods. Those methods have a bug that causes them to misbehave
     * if the TextureRegion has been flipped.
     *
     * These methods also maintain my preferred array orientation of
     * array[x][y] rather than LibGDX's array[row][column].
     *
     * @param sprite
     * @param tileWidth
     * @param tileHeight
     * @return
     */
    public static TextureRegion[][] split(TextureRegion sprite, int tileWidth, int tileHeight) {
        boolean flipX = sprite.isFlipX();
        boolean flipY = sprite.isFlipY();
        sprite.flip(flipX, flipY);
        int x = sprite.getRegionX();
        int y = sprite.getRegionY();
        int width = sprite.getRegionWidth();
        int height = sprite.getRegionHeight();

        int xTiles = width / tileWidth;
        int yTiles = height / tileHeight;

        int startY = y;
        TextureRegion[][] tiles = new TextureRegion[xTiles][yTiles];
        for (int tileX = 0; tileX < xTiles; tileX++, x += tileWidth) {
            y = startY;
            for (int tileY = 0; tileY < yTiles; tileY++, y += tileHeight) {
                tiles[tileX][tileY] = new TextureRegion(sprite.getTexture(), x, y, tileWidth, tileHeight);
                tiles[tileX][tileY].flip(flipX, flipY);
            }
        }
        return tiles;
    }

    public static TextureRegion[][] split(Texture texture, int tileWidth, int tileHeight) {
        return split(new TextureRegion(texture), tileWidth, tileHeight);
    }

    public static TextureRegion[][] split(Tile tile, int tileWidth, int tileHeight) {
        return split(tile.getSprite(), tileWidth, tileHeight);
    }

    public static void split(Tile[][] tiles) {
        for (int i=0; i<tiles.length; i++) {
            for (int j=0; j<tiles[0].length; j++) {
                Tile tile = tiles[i][j];
                if (tile.isLarge()) {
                    TextureRegion[][] sprites = split(tile, 32, 32);
                    for (int p=0; p<sprites.length; p++) {
                        for (int q=0; q<sprites[0].length; q++) {
                            tiles[i+p][j+q] = Tile.createTile(sprites[p][q], tile.isPathable());
                        }
                    }
                }
            }
        }
    }
}
