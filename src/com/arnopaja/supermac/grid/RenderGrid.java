package com.arnopaja.supermac.grid;

import com.arnopaja.supermac.objects.Entity;
import com.arnopaja.supermac.objects.Tile;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

/**
 * Implementation of Grid used for rendering purposes.
 * This is the subsection of a grid that is rendered on screen.
 * It has a size that is either fixed or dependent on screen resolution.
 * It should never be instantiated except by the getRenderGrid method in Grid.
 *
 * @author Ari Weiland
 */
public class RenderGrid extends Grid {

    // TODO: decide on these dimensions
    // TODO: alternatively, may need to be dynamic based on screen res
    public static final int RENDER_WIDTH = 27;
    public static final int RENDER_HEIGHT = 17;

    private final int renderWidth = RENDER_WIDTH;
    private final int renderHeight = RENDER_HEIGHT;

    protected RenderGrid(Grid grid) {
        super(grid);
        if ((grid.gridWidth != renderWidth) || (grid.gridHeight != renderHeight)) {
            throw new IllegalArgumentException();
        }
    }

    /**
     * Renders all elements of this RenderGrid using their appropriate render method.
     * They are rendered at the appropriate position corresponding to their grid space,
     * offset by the vector (x, y).
     *
     * @param batcher the SpriteBatch used to render the elements
     * @param x the x component of the offset vector
     * @param y the y component of the offset vector
     */
    public void renderTiles(SpriteBatch batcher, float x, float y) {
        for (int i=0; i<renderWidth; i++) {
            for (int j=0; j<renderHeight; j++) {
                Tile tile = tileArray[i][j];
                if (tile.isRendered()) {
                    tile.render(batcher, i + x, j + y);
                }
            }
        }
    }

    public void renderTiles(SpriteBatch batcher, Vector2 offset) {
        renderTiles(batcher, offset.x, offset.y);
    }

    public void renderEntities(SpriteBatch batcher, float x, float y) {
        for (int i=0; i<renderWidth; i++) {
            for (int j=0; j<renderHeight; j++) {
                Entity entity = getEntity(i, j);
                if (entity != null && entity.isRendered()) {
                    entity.render(batcher, i + x, j + y);
                }
            }
        }
    }

    public void renderEntities(SpriteBatch batcher, Vector2 offset) {
        renderEntities(batcher, offset.x, offset.y);
    }
}
