package com.arnopaja.supermac.grid;

import com.badlogic.gdx.math.Vector2;

/**
 * @author Ari Weiland
 */
public enum Direction {

    NORTH, EAST, SOUTH, WEST;

    public static Direction getOpposite(Direction dir) {
        return Direction.values()[(dir.ordinal() + 2) % 4];
    }

    public static Vector2 getAdjacent(Direction dir) {
        return getAdjacent(new Vector2(), dir);
    }

    public static Vector2 getAdjacent(Vector2 position, Direction dir) {
        Vector2 destination = new Vector2(position);
        switch (dir) {
            case NORTH:
                destination = new Vector2(position.x, position.y-1);
                break;
            case EAST:
                destination = new Vector2(position.x+1, position.y);
                break;
            case SOUTH:
                destination = new Vector2(position.x, position.y+1);
                break;
            case WEST:
                destination = new Vector2(position.x-1, position.y);
                break;
        }
        return destination;
    }

    /**
     * Returns the cardinal direction closest to the angle of the line between the two points.
     * Angles round counterclockwise (i.e. due Northeast rounds to North)
     *
     * @param from the starting point
     * @param toward the ending point
     * @return the closest cardinal direction from "from" to "toward"
     */
    public static Direction getDirectionToward(Vector2 from, Vector2 toward) {
        float angle = toward.cpy().sub(from).angle();
        if (angle < 45 || angle >= 315) {
            return Direction.EAST;
        } else if (angle < 135) {
            return Direction.SOUTH;
        } else if (angle < 225) {
            return Direction.WEST;
        } else {
            return Direction.NORTH;
        }
    }
}
