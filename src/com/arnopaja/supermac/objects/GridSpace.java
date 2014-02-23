package com.arnopaja.supermac.objects;

/**
 * @author Ari Weiland
 */
public abstract class GridSpace {

    public final int gridWidth, gridHeight;

    // TODO: change to a proper tile grid
    protected Tile[][] grid;

    public GridSpace(int gridWidth, int gridHeight) {
        this.gridWidth = gridWidth;
        this.gridHeight = gridHeight;
        grid = new Tile[gridWidth][gridHeight];
    }

    public int getGridWidth() {
        return gridWidth;
    }

    public int getGridHeight() {
        return gridHeight;
    }

    public Tile[][] getGrid() {
        return grid;
    }

    public Tile getTile(int x, int y) {
        return grid[x][y];
    }
}
