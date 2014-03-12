package com.arnopaja.supermac.world.objects;

import com.arnopaja.supermac.world.grid.Direction;
import com.arnopaja.supermac.world.grid.Grid;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import java.util.EnumMap;

/**
 * @author Ari Weiland
 */
public abstract class MapCharacter extends Entity {

    public static final float MOVE_SPEED = 3f; // grid spaces per second

    private boolean isMoving = false;
    private Vector2 renderOffset = new Vector2();
    private Vector2 renderOffsetDelta;

    private EnumMap<Direction, Animation> facingAnimations = new EnumMap<Direction, Animation>(Direction.class);

    protected MapCharacter() {
        this(null, new Vector2(0, 0), Direction.WEST);
    }

    protected MapCharacter(Grid grid, Vector2 position, Direction facing) {
        this(grid, position, facing, false);
    }

    protected MapCharacter(Grid grid, Vector2 position, Direction facing, boolean isInteractable) {
        super(true, grid, position, facing, isInteractable);
    }

    @Override
    public boolean render(SpriteBatch batcher, Vector2 position, float runTime) {
        return super.render(batcher, position.cpy().add(renderOffset), runTime);
    }

    @Override
    public void update(float delta) {
        if (isMoving) {
            renderOffset.add(renderOffsetDelta.cpy().scl(delta));
            if (renderOffset.hasSameDirection(renderOffsetDelta)) {
                isMoving = false;
                renderOffset = new Vector2();
            }
        }
    }

    public boolean move(Direction dir) {
        if (!isMoving) {
            setFacing(dir);
            if (getGrid().moveEntity(this, dir)) {
                isMoving = true;
                renderOffset = Direction.getAdjacent(dir).scl(-1);
                renderOffsetDelta = renderOffset.cpy().scl(-MOVE_SPEED);
                return true;
            }
        }
        return false;
    }

    public void changeGrid(Grid newGrid, Vector2 position) {
        getGrid().removeEntity(position);
        setGrid(newGrid);
        // TODO: what about collisions?
        newGrid.putEntity(this, position);
    }

    public boolean isMoving() {
        return isMoving;
    }

    public Vector2 getRenderOffset() {
        return renderOffset;
    }

    @Override
    public TextureRegion getSprite(float runTime) {
        if (isMoving()) {
            return getAnimation().getKeyFrame(runTime);
        } else {
            return getSprite();
        }
    }

    public Animation getAnimation() {
        return facingAnimations.get(getFacing());
    }

    public void setFacingAnimations(EnumMap<Direction, Animation> facingAnimations) {
        this.facingAnimations = facingAnimations;
    }

    @Override
    public String toString() {
        return "MapCharacter{" +
                "grid=" + getGrid() +
                ", position=" + getPosition() +
                ", facing=" + getFacing() +
                ", isInteractable=" + isInteractable() +
                '}';
    }
}
