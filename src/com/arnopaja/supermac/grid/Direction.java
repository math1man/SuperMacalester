package com.arnopaja.supermac.grid;

import com.badlogic.gdx.math.Vector2;

/**
 * @author Ari Weiland
 */
public enum Direction {

    NORTH, EAST, SOUTH, WEST;

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
}
