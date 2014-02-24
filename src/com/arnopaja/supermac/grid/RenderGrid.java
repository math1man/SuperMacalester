package com.arnopaja.supermac.grid;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Implementation of Grid used for rendering purposes.
 * This is the subsection of a grid that is rendered on screen.
 * It has a size that is either fixed or dependent on screen resolution.
 *
 * @author Ari Weiland
 */
public class RenderGrid extends Grid {

    // TODO: decide on these dimensions
    // TODO: alternatively, may need to be dynamic based on screen res
    public static final int RENDER_WIDTH = 15;
    public static final int RENDER_HEIGHT = 15;

    private final int renderWidth = RENDER_WIDTH;
    private final int renderHeight = RENDER_HEIGHT;

    protected RenderGrid(GridElement[][] grid) {
        super(grid);
        if (grid.length != renderWidth || grid[0].length != renderHeight) {
            throw new IllegalArgumentException();
        }
    }

    protected RenderGrid(Grid grid) {
        super(grid);
        if (grid.getGridWidth() != renderWidth || grid.gridHeight != renderHeight) {
            throw new IllegalArgumentException();
        }
    }

    /**
     * Renders all elments of this RenderGrid using their appropriate render method.
     * They are rendered at the appropriate position corresponding to their grid space,
     * offset by the vector (x, y).
     *
     * @param batcher the SpriteBatch used to render the elements
     * @param x the x component of the offset vector
     * @param y the y component of the offset vector
     */
    public void render(SpriteBatch batcher, float x, float y) {
        for (int i=0; i<renderWidth; i++) {
            for (int j=0; j<renderHeight; j++) {
                GridElement element = gridArray[i][j];
                element.render(batcher, i*GRID_PIXEL_DIMENSION + x, j*GRID_PIXEL_DIMENSION + y);
            }
        }
    }
}
