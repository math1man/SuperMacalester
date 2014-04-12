package com.arnopaja.supermac.helpers;

import com.arnopaja.supermac.world.grid.Grid;
import com.arnopaja.supermac.world.objects.Tile;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * @author Ari Weiland
 */
public class SpriteUtils {

    /**
     * Creates a TextureRegion from the texture, x, and y with default width and
     * height of 32 x 32, using the no flip orientation
     * @param texture
     * @param x
     * @param y
     * @return
     */
    public static TextureRegion makeSprite(Texture texture, int x, int y) {
        return makeSprite(texture, x, y, false, false);
    }

    /**
     * Creates a TextureRegion from the texture, x, and y with default width and
     * height of 1 x 1, using no flip orientation
     * @param texture
     * @param x
     * @param y
     * @return
     */
    public static TextureRegion makeSprite(Texture texture, int x, int y, boolean flipX, boolean flipY) {
        return makeSprite(texture, x, y, 1, 1, flipX, flipY);
    }

    /**
     * Creates a TextureRegion from the texture, x, y, width, and height parameters
     * using the no flip orientation.
     * @param texture
     * @param x
     * @param y
     * @param width
     * @param height
     * @return
     */
    public static TextureRegion makeSprite(Texture texture, int x, int y, int width, int height) {
        return makeSprite(texture, x, y, width, height, false, false);
    }

    /**
     * Creates a TextureRegion from the texture, x, y, width, and height parameters
     * and flipping it according to the flipX and flipY parameters.
     * Position and dimension parameters are in grid spaces, not pixels.
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
        TextureRegion sprite = new TextureRegion(texture, //x, y, width, height);
                x * Grid.GRID_PIXEL_DIMENSION, y * Grid.GRID_PIXEL_DIMENSION,
                width * Grid.GRID_PIXEL_DIMENSION, height * Grid.GRID_PIXEL_DIMENSION);
        sprite.flip(flipX, !flipY);
        return sprite;
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
    public static TextureRegion[][] split(TextureRegion sprite, int tileWidth, int tileHeight, boolean flipX, boolean flipY) {
        sprite.flip(flipX, flipY); // flip the sprite appropriately
        flipX = sprite.isFlipX();
        flipY = sprite.isFlipY();
        sprite.flip(flipX, flipY); // unflip the sprite for the splitting process
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

    public static TextureRegion[][] split(Texture texture) {
        return split(texture, false, false);
    }

    public static TextureRegion[][] split(Texture texture, boolean flipX, boolean flipY) {
        return split(texture, Grid.GRID_PIXEL_DIMENSION, Grid.GRID_PIXEL_DIMENSION, flipX, flipY);
    }

    public static TextureRegion[][] split(Texture texture, int tileWidth, int tileHeight, boolean flipX, boolean flipY) {
        return split(new TextureRegion(texture), tileWidth, tileHeight, flipX, !flipY);
    }

    public static TextureRegion[][] split(Tile tile) {
        return split(tile, Grid.GRID_PIXEL_DIMENSION, Grid.GRID_PIXEL_DIMENSION);
    }

    public static TextureRegion[][] split(Tile tile, int tileWidth, int tileHeight) {
        return split(tile.getSprite(), tileWidth, tileHeight, false, false);
    }

    public static void split(Tile[][] tiles) {
        for (int i=0; i<tiles.length; i++) {
            for (int j=0; j<tiles[0].length; j++) {
                Tile tile = tiles[i][j];
                if (tile.isLarge()) {
                    TextureRegion[][] sprites = split(tile);
                    for (int p=0; p< sprites.length; p++) {
                        for (int q=0; q< sprites[0].length; q++) {
                            tiles[i+p][j+q] = Tile.createTile("", sprites[p][q], tile.isPathable());
                        }
                    }
                }
            }
        }
    }
}
