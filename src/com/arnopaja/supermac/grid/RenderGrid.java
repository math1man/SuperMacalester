package com.arnopaja.supermac.grid;

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

    public RenderGrid() {
        super(RENDER_WIDTH, RENDER_HEIGHT);
    }

    protected RenderGrid(GridElement[][] grid) {
        super(grid);
        if (grid.length != RENDER_WIDTH || grid[0].length != RENDER_HEIGHT) {
            throw new IllegalArgumentException();
        }
    }

    protected RenderGrid(Grid grid) {
        super(grid);
        if (grid.getGridWidth() != RENDER_WIDTH || grid.gridHeight != RENDER_HEIGHT) {
            throw new IllegalArgumentException();
        }
    }
}
