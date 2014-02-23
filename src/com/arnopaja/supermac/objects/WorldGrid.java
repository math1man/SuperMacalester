package com.arnopaja.supermac.objects;

/**
 * @author Ari Weiland
 */
public class WorldGrid extends GridSpace {

    public static final int WORLD_WIDTH = 16;
    public static final int WORLD_HEIGHT = 16;

    public WorldGrid() {
        super(WORLD_WIDTH, WORLD_HEIGHT);
    }
}
