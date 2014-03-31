package com.arnopaja.supermac.world.objects;

import com.arnopaja.supermac.world.grid.Direction;
import com.arnopaja.supermac.world.grid.Location;
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

    private EnumMap<Direction, TextureRegion> facingSprites;
    private EnumMap<Direction, Animation> facingAnimations;

    protected MapCharacter(Location location, boolean isInteractable,
                           EnumMap<Direction, TextureRegion> facingSprites,
                           EnumMap<Direction, Animation> facingAnimations) {
        super(true, location, isInteractable);
        this.facingSprites = facingSprites;
        this.facingAnimations = facingAnimations;
    }

    @Override
    public boolean render(SpriteBatch batch, Vector2 position, float runTime) {
        return super.render(batch, position.cpy().add(renderOffset), runTime);
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

    public boolean move(Direction direction) {
        if (!isMoving && direction != null) {
            setFacing(direction);
            if (getGrid().moveEntity(this, direction)) {
                isMoving = true;
                renderOffset = Direction.getAdjacent(direction).scl(-1);
                renderOffsetDelta = renderOffset.cpy().scl(-MOVE_SPEED);
                return true;
            }
        }
        return false;
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
            return facingAnimations.get(getFacing()).getKeyFrame(runTime);
        } else {
            return facingSprites.get(getFacing());
        }
    }

    public void setFacingSprites(EnumMap<Direction, TextureRegion> facingSprites) {
        this.facingSprites = facingSprites;
    }

    public void setFacingAnimations(EnumMap<Direction, Animation> facingAnimations) {
        this.facingAnimations = facingAnimations;
    }
}
