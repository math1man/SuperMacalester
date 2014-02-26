package com.arnopaja.supermac.objects;

import com.arnopaja.supermac.grid.Direction;
import com.arnopaja.supermac.grid.Grid;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

/**
 * @author Ari Weiland
 */
public abstract class Character extends Entity {

    protected Animation animation;
    protected TextureRegion sprite;

    protected Character() {
        this(null, 0, 0, Direction.SOUTH);
    }

    protected Character(Grid grid, int x, int y, Direction facing) {
        this(grid, new Vector2(x, y), facing);
    }

    protected Character(Grid grid, Vector2 position, Direction facing) {
        this(grid, position, facing, false);
    }

    protected Character(Grid grid, int x, int y, Direction facing, boolean isInteractable) {
        this(grid, new Vector2(x, y), facing, isInteractable);
    }

    protected Character(Grid grid, Vector2 position, Direction facing, boolean isInteractable) {
        super(true, grid, position, facing, isInteractable);
    }

    @Override
    public boolean render(SpriteBatch batcher, float xPos, float yPos) {
        // TODO: finish this method to deal with animations, etc.
//        batcher.draw(sprite, xPos, yPos);
        return true;
    }

    public abstract void update(float delta);

    public Animation getAnimation() {
        return animation;
    }

    public void setAnimation(Animation animation) {
        this.animation = animation;
    }

    public TextureRegion getSprite() {
        return sprite;
    }

    public void setSprite(TextureRegion sprite) {
        this.sprite = sprite;
    }
}
