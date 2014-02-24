package com.arnopaja.supermac.objects;

import com.arnopaja.supermac.grid.GridSpace;

/**
 * @author Ari Weiland
 */
public class MainCharacter extends GridSpace {
    public MainCharacter(int x, int y, boolean isRendered) {
        super(x, y, true);
    }

    public void move(direction dir) {
        switch (dir) {
            case NORTH: y--; break;
            case EAST: x++; break;
            case SOUTH: y++; break;
            case WEST: x--; break;
        }
    }
}
