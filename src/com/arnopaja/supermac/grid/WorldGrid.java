package com.arnopaja.supermac.grid;

/**
 * Implementation of Grid for the world.
 * Has a fixed size and is filled based on the map of Macalester.
 *
 * @author Ari Weiland
 */
public class WorldGrid extends Grid {

    // TODO: update these appropriately
    public static final int WORLD_WIDTH = 16;
    public static final int WORLD_HEIGHT = 16;

    public WorldGrid() {
        super(WORLD_WIDTH, WORLD_HEIGHT);
    }

    public void fillMacMap() {
        // TODO: this method should fill the grid with the macalester tile map
    }
}
