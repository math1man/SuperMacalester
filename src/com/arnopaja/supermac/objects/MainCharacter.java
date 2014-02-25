package com.arnopaja.supermac.objects;

import com.arnopaja.supermac.grid.Direction;
import com.arnopaja.supermac.grid.Grid;

/**
 * @author Ari Weiland
 */
public class MainCharacter extends Character {

    public MainCharacter(Grid grid, int x, int y, Direction facing) {
        super(grid, x, y, facing);
    }

    @Override
    public void update(float delta) {

    }

    public void interact() {
        Entity entity = grid.getEntity(Direction.getAdjacent(position, facing));
        if (entity.isInteractable()) {
            // TODO: interact!
        }
    }
}
