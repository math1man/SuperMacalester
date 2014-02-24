package com.arnopaja.supermac.grid;

import com.badlogic.gdx.math.Vector2;

/**
 * Superclass for all types of grids.
 *
 * @author Ari Weiland
 */
public class Grid {

    public static final int GRID_PIXEL_DIMENSION = 16; // the pixel width and height of a grid space

    public final int gridWidth, gridHeight;

    protected GridElement[][] gridArray;

    protected Grid(int gridWidth, int gridHeight) {
        this.gridWidth = gridWidth;
        this.gridHeight = gridHeight;
        clear();
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
     * missing elements are set as blank elements.
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
                if (i+x < gridArray.length
                        && i+x >= 0
                        && j+y < gridArray[0].length
                        && j+y >= 0) {
                    subGrid[i][j] = gridArray[i+x][j+y];
                } else {
                    subGrid[i][j] = new BlankElement();
                }
            }
        }
        return subGrid;
    }

    /**
     * Duplicate of the other getSubGridArray method using a Vector2 instead of x and y coords.
     *
     * @param position the coordinates of upper left hand corner of the subgrid array.  Can be negative
     * @param width the width of the subgrid array
     * @param height the height of the subgrid array
     * @return the subgrid array
     */
    public GridElement[][] getSubGridArray(Vector2 position, int width, int height) {
        return getSubGridArray((int) position.x, (int) position.y, width, height);
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
     * Duplicate of the other getSubGrid method using a Vector2 instead of x and y coords.
     *
     * @param position the coordinates of upper left hand corner of the subgrid.  Can be negative
     * @param width the width of the subgrid
     * @param height the height of the subgrid
     * @return the subgrid
     */
    public Grid getSubGrid(Vector2 position, int width, int height) {
        return getSubGrid((int) position.x, (int) position.y, width, height);
    }

    /**
     * Returns a RenderGrid of the calling grid centered at the given coordinates.
     * If the RenderGrid covers spaces not in the calling grid, those spaces will be
     * set as blank elements.
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

    /**
     * Duplicate of the other getRenderGrid method using a Vector2 instead of x and y coords.
     *
     * @param position the coordinates of the center of the RenderGrid
     * @return the RenderGrid
     */
    public RenderGrid getRenderGrid(Vector2 position) {
        return getRenderGrid((int) position.x, (int) position.y);
    }

    public void clear() {
        gridArray = new GridElement[gridWidth][gridHeight];
    }
}
