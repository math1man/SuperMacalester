package com.arnopaja.supermac.world.grid;

import com.arnopaja.supermac.world.objects.Entity;
import com.badlogic.gdx.math.Vector2;

/**
 * @author Ari Weiland
 */
public class Location {

    private final Grid grid;
    private Vector2 position;
    private Direction facing;

    public Location(Grid grid, Vector2 position, Direction facing) {
        this.grid = grid;
        this.position = position;
        this.facing = facing;
    }

    public RenderGrid getRenderGrid(int renderGridWidth, int renderGridHeight) {
        return grid.getRenderGrid(position, renderGridWidth, renderGridHeight);
    }

    /**
     * Gets the entity at this location.
     * In other words, gets the entity at this position in this grid.
     *
     * @return the entity
     */
    public Entity getEntity() {
        return grid.getEntity(position);
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

        // if grid is not null but position is, it deserves to throw NullPointerExceptions
        return grid == null ? location.grid == null :
                (grid.equals(location.grid) && position.equals(location.position));

    }
}
