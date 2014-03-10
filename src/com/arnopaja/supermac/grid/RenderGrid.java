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

    protected RenderGrid(Grid grid) {
        super(grid);
    }

    /**
     * Renders all elements of this RenderGrid using their appropriate render method.
     * They are rendered at the appropriate position corresponding to their grid space,
     * offset by the vector (x, y).
     *
     * @param batcher the SpriteBatch used to render the elements
     * @param offset the offset vector
     * @param runTime the current runtime.  Used to properly render animations
     */
    public void renderTiles(SpriteBatch batcher, Vector2 offset, float runTime) {
        for (int i=0; i<gridWidth; i++) {
            for (int j=0; j<gridHeight; j++) {
                Tile tile = tileArray[i][j];
                if (tile.isRendered()) {
                    tile.render(batcher, new Vector2(i, j).add(offset), runTime);
                }
            }
        }
    }

    /**
     * Renders all elements of this RenderGrid using their appropriate render method.
     * They are rendered at the appropriate position corresponding to their grid space,
     * offset by the vector (x, y).
     *
     * @param batcher the SpriteBatch used to render the elements
     * @param offset the offset vector
     * @param runTime the current runtime.  Used to properly render animations
     */
    public void renderEntities(SpriteBatch batcher, Vector2 offset, float runTime) {
        for (int i=0; i<gridWidth; i++) {
            for (int j=0; j<gridHeight; j++) {
                Vector2 position = new Vector2(i, j);
                Entity entity = getEntity(position);
                if (entity != null && entity.isRendered()) {
                    entity.render(batcher, position.add(offset), runTime);
                }
            }
        }
    }
}