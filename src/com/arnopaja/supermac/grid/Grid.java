package com.arnopaja.supermac.grid;

/**
 * Superclass for all types of grids.
 *
 * @author Ari Weiland
 */
public class Grid {

    public final int gridWidth, gridHeight;

    protected GridElement[][] gridArray;

    protected Grid(int gridWidth, int gridHeight) {
        this.gridWidth = gridWidth;
        this.gridHeight = gridHeight;
        gridArray = new GridElement[gridWidth][gridHeight];
    }

    protected Grid(GridElement[][] gridArray) {
        this.gridArray = gridArray;
        gridWidth = gridArray.length;
        gridHeight = gridArray[0].length;
    }

    protected Grid(Grid grid) {
        gridArray = grid.getGrid();
        gridWidth = gridArray.length;
        gridHeight = gridArray[0].length;
    }

    public int getGridWidth() {
        return gridWidth;
    }

    public int getGridHeight() {
        return gridHeight;
    }

    public GridElement[][] getGrid() {
        return gridArray;
    }

    public GridElement getTile(int x, int y) {
        return gridArray[x][y];
    }

    /**
     * Returns an array of GridSpaces that corresponds to the described subgrid.
     * If the described subgrid covers area outside the caller's grid array, the
     * missing elements are set to null.
     *
     * @param x the x coordinate of upper left hand corner of the subgrid array.  Can be negative
     * @param y the y coordinate of upper left hand corner of the subgrid array.  Can be negative
     * @param width the width of the subgrid array
     * @param height the height of the subgrid array
     * @return the subgrid array
     */
    public GridElement[][] getSubGridArray(int x, int y, int width, int height) {
        GridElement[][] subGrid = new GridElement[width][height];
        for (int i=0; i<width; i++) {
            for (int j=0; j<height; j++) {
                if (i+x < gridArray.length && i+x >= 0
                        && i+y < gridArray[0].length
                        && i+y >= 0) {
                    subGrid[i][j] = gridArray[i+x][j+x];
                } else {
                    subGrid[i][j] = null;
                }
            }
        }
        return subGrid;
    }

    /**
     * Returns a Grid object whose grid array is determined by the getSubGridArray method.
     *
     * @param x the x coordinate of upper left hand corner of the subgrid.  Can be negative
     * @param y the y coordinate of upper left hand corner of the subgrid.  Can be negative
     * @param width the width of the subgrid
     * @param height the height of the subgrid
     * @return the subgrid
     */
    public Grid getSubGrid(int x, int y, int width, int height) {
        return new Grid(getSubGridArray(x, y, width, height));
    }

    /**
     * Returns a RenderGrid of the calling grid centered at the given coordinates.
     * If the RenderGrid covers spaces not in the calling grid, those spaces will be
     * set to null.
     *
     * @param x the x coordinate of the center of the RenderGrid
     * @param y the x coordinate of the center of the RenderGrid
     * @return the RenderGrid
     */
    public RenderGrid getRenderGrid(int x, int y) {
        int cornerX = x - (RenderGrid.RENDER_WIDTH - 1)/2;
        int cornerY = y - (RenderGrid.RENDER_HEIGHT - 1)/2;
        return new RenderGrid(getSubGrid(cornerX, cornerY,
                RenderGrid.RENDER_WIDTH, RenderGrid.RENDER_HEIGHT));
    }
}
