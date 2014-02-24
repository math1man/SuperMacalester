package com.arnopaja.supermac.objects;

import com.arnopaja.supermac.grid.GridSpace;

/**
 * @author Ari Weiland
 */
public class Tile extends GridSpace {

    public static enum TileType {
        PATH(true),
        GROUND(true),
        BUILDING(false);

        public final boolean isPathable;

        TileType(boolean isPathable) {
            this.isPathable = isPathable;
        }
    }

    private TileType type;
    private boolean isPathable;

    public Tile(int x, int y, TileType type) {
        super(x, y, true);
        this.type = type;
        isPathable = type.isPathable;
    }

    public TileType getType() {
        return type;
    }

    public boolean isPathable() {
        return isPathable;
    }

    public void setType(TileType type) {
        this.type = type;
    }

    public void setPathable(boolean isPathable) {
        this.isPathable = isPathable;
    }
}
