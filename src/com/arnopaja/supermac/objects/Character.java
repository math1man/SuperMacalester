package com.arnopaja.supermac.objects;

import com.arnopaja.supermac.grid.BlankElement;
import com.arnopaja.supermac.grid.Grid;
import com.arnopaja.supermac.grid.GridElement;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

/**
 * @author Ari Weiland
 */
public abstract class Character extends GridElement {

    protected Animation animation;
    protected TextureRegion sprite;

    // position in the WorldGrid or FloorGrid
    protected Grid grid;
    protected int x;
    protected int y;
    protected Direction facing;

    protected Character() {
        this(null, 0, 0, Direction.SOUTH);
    }

    protected Character(Grid grid, int x, int y, Direction facing) {
        this(grid, x, y, facing, false);
    }

    protected Character(Grid grid, int x, int y, Direction facing, boolean isInteractable) {
        super(true, isInteractable);
        changeGrid(grid, x, y, facing);
    }

    public void changeGrid(Grid grid, int x, int y, Direction facing) {
        this.grid = grid;
        setPosition(x, y);
        setFacing(facing);
    }

    @Override
    public boolean render(SpriteBatch batcher, float xPos, float yPos) {
        // TODO: finish this method to deal with animations, etc.
        batcher.draw(sprite, xPos, yPos);
        return true;
    }

    public abstract void update(float delta);

    public boolean move(Direction dir) {
        GridElement element = grid.getAdjacentGridElement(x, y, dir);
        if ((element != null) && (element instanceof BlankElement)) {
            grid.setGridElement(x, y, element);
            switch (dir) {
                case NORTH: y--; break;
                case EAST: x++; break;
                case SOUTH: y++; break;
                case WEST: x--; break;
            }
            grid.setGridElement(x, y, this);
            return true;
        }
        return false;
    }

    public Grid getGrid() {
        return grid;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Vector2 getPosition() {
        return new Vector2(x, y);
    }

    public Direction getFacing() {
        return facing;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setPosition (int x, int y) {
        setX(x);
        setY(y);
    }

    public void setPosition (Vector2 position) {
        setPosition((int) position.x, (int) position.y);
    }

    public void setFacing(Direction facing) {
        this.facing = facing;
    }

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
