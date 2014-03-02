package com.arnopaja.supermac.objects;

import com.arnopaja.supermac.grid.Direction;
import com.arnopaja.supermac.grid.Grid;
import com.arnopaja.supermac.helpers.AssetLoader;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

/**
 * @author Ari Weiland
 */
public class MainMapCharacter extends MapCharacter {

    public MainMapCharacter(Grid grid, int x, int y, Direction facing) {
        this(grid, new Vector2(x, y), facing);
        setFacingSprites(AssetLoader.steven);
        setFacingAnimations(AssetLoader.stevenStepping);
    }

    public MainMapCharacter(Grid grid, Vector2 position, Direction facing) {
        super(grid, position, facing);
    }

    public void interact() {
        Entity entity = grid.getEntity(Direction.getAdjacent(position, facing));
        if (entity != null && entity.isInteractable()) {
            entity.interact(this);
        }
    }

    @Override
    public void interact(MainMapCharacter character) {
        System.out.println("You have somehow managed to interact with yourself");
        // insert masturbation joke here
    }

    @Override
    public TextureRegion getSprite(float runTime) {
        return getSprite(facing, runTime);
    }

    @Override
    public TextureRegion getSprite(Direction dir, float runTime) {
        int ordinal = dir.ordinal();
        if (isMoving) {
            return facingAnimations[ordinal].getKeyFrame(runTime);
        } else {
            return facingSprites[ordinal];
        }
    }

    @Override
    public String toString() {
        return "MainMapCharacter{" +
                "grid=" + grid +
                ", position=" + position +
                ", facing=" + facing +
                ", isRendered=" + isRendered +
                '}';
    }
}
