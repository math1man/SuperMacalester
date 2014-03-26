package com.arnopaja.supermac.world.grid;

import com.badlogic.gdx.math.Vector2;

/**
 * @author Ari Weiland
 */
public class Location {

    public static final Location NULL = new Location(null, null, null);

    private final Grid grid;
    private Vector2 position;
    private Direction facing;

    public Location(Grid grid) {
        this.grid = grid;
        this.position = new Vector2(0, 0);
        this.facing = Direction.WEST;
    }

    public Location(Grid grid, Vector2 position, Direction facing) {
        this.grid = grid;
        this.position = position;
        this.facing = facing;
    }

    public RenderGrid getRenderGrid(int renderGridWidth, int renderGridHeight) {
        return grid.getRenderGrid(position, renderGridWidth, renderGridHeight);
    }

    public Grid getGrid() {
        return grid;
    }

    public Vector2 getPosition() {
        return position;
    }

    public void setPosition(Vector2 position) {
        this.position = position;
    }

    public Direction getFacing() {
        return facing;
    }

    public void setFacing(Direction facing) {
        this.facing = facing;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Location)) return false;

        Location location = (Location) o;

        return facing == location.facing
                && !(grid != null ? !grid.equals(location.grid) : location.grid != null)
                && !(position != null ? !position.equals(location.position) : location.position != null);

    }
}
