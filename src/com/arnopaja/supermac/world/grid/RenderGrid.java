package com.arnopaja.supermac.world.grid;

import com.arnopaja.supermac.world.objects.Entity;
import com.arnopaja.supermac.world.objects.Tile;
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

    public void render(SpriteBatch batch, Vector2 offset, float runTime) {
        batch.begin();
        batch.disableBlending();
        renderTiles(batch, offset, runTime);
        batch.enableBlending();
        renderEntities(batch, offset, runTime);
        batch.end();
    }

    /**
     * Renders all elements of this RenderGrid using their appropriate render method.
     * They are rendered at the appropriate position corresponding to their grid space,
     * offset by the vector (x, y).
     *
     * @param batch the SpriteBatch used to render the elements
     * @param offset the offset vector
     * @param runTime the current runtime.  Used to properly render animations
     */
    private void renderTiles(SpriteBatch batch, Vector2 offset, float runTime) {
        for (int i=0; i<gridWidth; i++) {
            for (int j=0; j<gridHeight; j++) {
                Tile tile = tileArray[i][j];
                if (tile.isRendered()) {
                    tile.render(batch, new Vector2(i, j).add(offset), runTime);
                }
            }
        }
    }

    /**
     * Renders all elements of this RenderGrid using their appropriate render method.
     * They are rendered at the appropriate position corresponding to their grid space,
     * offset by the vector (x, y).
     *
     * @param batch the SpriteBatch used to render the elements
     * @param offset the offset vector
     * @param runTime the current runtime.  Used to properly render animations
     */
    private void renderEntities(SpriteBatch batch, Vector2 offset, float runTime) {
        for (int i=0; i<gridWidth; i++) {
            for (int j=0; j<gridHeight; j++) {
                Vector2 position = new Vector2(i, j);
                Entity entity = getEntity(position);
                if (entity != null && entity.isRendered()) {
                    entity.render(batch, position.add(offset), runTime);
                }
            }
        }
    }
}
