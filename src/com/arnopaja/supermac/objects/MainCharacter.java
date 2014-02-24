package com.arnopaja.supermac.objects;

import com.arnopaja.supermac.grid.GridElement;
import com.badlogic.gdx.math.Vector2;

/**
 * @author Ari Weiland
 */
public class MainCharacter extends GridElement {

    private int x;
    private int y;

    public MainCharacter(boolean isRendered) {
        super(true);
    }

    public void move(direction dir) {
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
}
