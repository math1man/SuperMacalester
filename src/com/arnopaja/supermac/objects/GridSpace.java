package com.arnopaja.supermac.objects;

/**
 * @author Ari Weiland
 */
public abstract class GridSpace {

    public final int gridWidth, gridHeight;

    // TODO: change to a proper tile grid, not int
    protected int[][] grid;

    public GridSpace(int gridWidth, int gridHeight) {
        this.gridWidth = gridWidth;
        this.gridHeight = gridHeight;
        grid = new int[gridWidth][gridHeight];
    }
}
