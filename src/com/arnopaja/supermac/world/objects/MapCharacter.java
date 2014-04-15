package com.arnopaja.supermac.world.objects;

import com.arnopaja.supermac.helpers.CharacterAsset;
import com.arnopaja.supermac.world.grid.Direction;
import com.arnopaja.supermac.world.grid.Location;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

/**
 * @author Ari Weiland
 */
public abstract class MapCharacter extends Entity {

    public static final float MOVE_SPEED = 6f; // grid spaces per second TODO: revert to 3

    private boolean isMoving = false;
    private Vector2 renderOffset = new Vector2();
    private Vector2 renderOffsetDelta;

    private CharacterAsset asset;

    protected MapCharacter(Location location, boolean isInteractable, CharacterAsset asset) {
        super(true, location, isInteractable);
        this.asset = asset;
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
            return asset.getAnimation(getFacing()).getKeyFrame(runTime);
        } else {
            return asset.getSprite(getFacing());
        }
    }

    public void setAsset(CharacterAsset asset) {
        this.asset = asset;
    }
}
