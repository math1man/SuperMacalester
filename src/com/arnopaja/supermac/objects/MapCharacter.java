package com.arnopaja.supermac.objects;

import com.arnopaja.supermac.grid.Direction;
import com.arnopaja.supermac.grid.Grid;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Vector2;

/**
 * @author Ari Weiland
 */
public abstract class MapCharacter extends Entity {

    // Ordered N-E-S-W (same as Direction order)
    protected Animation[] facingAnimations;

    protected MapCharacter() {
        this(null, 0, 0, Direction.SOUTH);
    }

    protected MapCharacter(Grid grid, int x, int y, Direction facing) {
        this(grid, new Vector2(x, y), facing);
    }

    protected MapCharacter(Grid grid, Vector2 position, Direction facing) {
        this(grid, position, facing, false);
    }

    protected MapCharacter(Grid grid, int x, int y, Direction facing, boolean isInteractable) {
        this(grid, new Vector2(x, y), facing, isInteractable);
    }

    protected MapCharacter(Grid grid, Vector2 position, Direction facing, boolean isInteractable) {
        super(true, grid, position, facing, isInteractable);
    }

    public Animation getAnimation() {
        return getAnimation(facing);
    }

    public Animation getAnimation(Direction dir) {
        if (facingAnimations != null && facingAnimations.length == 4) {
            return facingAnimations[dir.ordinal()];
        }
        return null;
    }

    public void setFacingAnimations(Animation[] facingAnimations) {
        if (facingSprites.length != 4) {
            throw new IllegalArgumentException("Must have 4 facing animations: North, East, South, West");
        }
        this.facingAnimations = facingAnimations;
    }

    public void setAnimation(Animation animation, Direction dir) {
        facingAnimations[dir.ordinal()] = animation;
    }

    @Override
    public String toString() {
        return "MapCharacter{" +
                "grid=" + grid +
                ", position=" + position +
                ", facing=" + facing +
                ", isInteractable=" + isInteractable +
                '}';
    }
}
