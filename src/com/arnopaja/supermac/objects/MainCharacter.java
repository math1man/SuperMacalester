package com.arnopaja.supermac.objects;

import com.arnopaja.supermac.grid.Grid;
import com.arnopaja.supermac.grid.GridElement;

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
        GridElement element = grid.getAdjacentGridElement(x, y, facing);
        if (element.isInteractable()) {
            // TODO: add actual interaction
        }
    }
}
