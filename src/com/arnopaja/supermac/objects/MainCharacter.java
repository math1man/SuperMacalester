package com.arnopaja.supermac.objects;

import com.arnopaja.supermac.grid.Direction;
import com.arnopaja.supermac.grid.Grid;
import com.arnopaja.supermac.helpers.AssetLoader;
import com.badlogic.gdx.math.Vector2;

/**
 * @author Ari Weiland
 */
public class MainCharacter extends Character {

    public MainCharacter(Grid grid, int x, int y, Direction facing) {
        this(grid, new Vector2(x, y), facing);
        setSprite(AssetLoader.steven);
    }

    public MainCharacter(Grid grid, Vector2 position, Direction facing) {
        super(grid, position, facing);
    }

    @Override
    public void update(float delta) {

    }

    public void interact() {
        Entity entity = grid.getEntity(Direction.getAdjacent(position, facing));
        if (entity != null && entity.isInteractable()) {
            // TODO: interact!
        }
    }
}
