package com.arnopaja.supermac.objects;

import com.arnopaja.supermac.grid.GridElement;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

/**
 * @author Ari Weiland
 */
public abstract class Character extends GridElement {

    private Animation animation;
    private TextureRegion sprite;

    // position in the WorldGrid or BuildingGrid
    private int x;
    private int y;
    private Direction facing;

    public Character() {
        super(true);
        int x = 0;
        int y = 0;
        facing = Direction.SOUTH;
    }

    @Override
    public boolean render(SpriteBatch batcher, float xPos, float yPos) {
        // TODO: finish this method to deal with animations, etc.
        batcher.draw(sprite, xPos, yPos);
        return true;
    }

    public abstract void update(float delta);

    public void move(Direction dir) {
        switch (dir) {
            case NORTH: y--; break;
            case EAST: x++; break;
            case SOUTH: y++; break;
            case WEST: x--; break;
        }
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
        this.x = x;
        this.y = y;
    }

    public void setPosition (Vector2 position) {
        this.x = (int) position.x;
        this.y = (int) position.y;
    }

    public void setFacing(Direction facing) {
        this.facing = facing;
    }
}
