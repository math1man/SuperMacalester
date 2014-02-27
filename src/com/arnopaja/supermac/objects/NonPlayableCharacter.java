package com.arnopaja.supermac.objects;

import com.arnopaja.supermac.grid.Direction;
import com.arnopaja.supermac.grid.Grid;
import com.badlogic.gdx.math.Vector2;

/**
 * @author Ari Weiland
 */
public class NonPlayableCharacter extends Character {

    public NonPlayableCharacter(Grid grid, int x, int y, Direction facing) {
        super(grid, x, y, facing);
    }

    public NonPlayableCharacter(Grid grid, Vector2 position, Direction facing) {
        super(grid, position, facing);
    }

    public NonPlayableCharacter(Grid grid, int x, int y, Direction facing, boolean isInteractable) {
        super(grid, x, y, facing, isInteractable);
    }

    public NonPlayableCharacter(Grid grid, Vector2 position, Direction facing, boolean isInteractable) {
        super(grid, position, facing, isInteractable);
    }
}
