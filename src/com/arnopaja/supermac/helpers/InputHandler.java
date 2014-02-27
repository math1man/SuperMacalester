package com.arnopaja.supermac.helpers;

import com.arnopaja.supermac.grid.Direction;
import com.arnopaja.supermac.grid.Grid;
import com.arnopaja.supermac.objects.MainCharacter;
import com.arnopaja.supermac.world.GameWorld;
import com.badlogic.gdx.InputProcessor;

/**
 * @author Ari Weiland
 */
public class InputHandler implements InputProcessor {

    public static final int SIDE_BUTTON_WIDTH = Grid.GRID_PIXEL_DIMENSION * 2;

    private GameWorld world;

    private float gameWidth;
    private float gameHeight;
    private float scaleFactorX;
    private float scaleFactorY;

    public InputHandler(GameWorld world, float gameWidth, float gameHeight,
                        float scaleFactorX, float scaleFactorY) {
        this.world = world;
        this.gameWidth = gameWidth;
        this.gameHeight = gameHeight;
        this.scaleFactorX = scaleFactorX;
        this.scaleFactorY = scaleFactorY;
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int i) {
        return false;
    }

    @Override
    public boolean keyTyped(char c) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        int gameX = scaleX(screenX);
        int gameY = scaleY(screenY);
        MainCharacter character = world.getMainCharacter();
        if (gameX < SIDE_BUTTON_WIDTH) {
            System.out.println("west");
            character.move(Direction.WEST);
        } else if (gameX > gameWidth - SIDE_BUTTON_WIDTH) {
            System.out.println("east");
            character.move(Direction.EAST);
        } else if (gameY < SIDE_BUTTON_WIDTH) {
            System.out.println("north");
            character.move(Direction.NORTH);
        } else if (gameY > gameHeight - SIDE_BUTTON_WIDTH) {
            System.out.println("south");
            character.move(Direction.SOUTH);
        } else {
            System.out.println("INTERACTION (1)");
            character.interact();
        }
        System.out.println(character);
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int i, int i2, int i3) {
        return false;
    }

    @Override
    public boolean mouseMoved(int i, int i2) {
        return false;
    }

    @Override
    public boolean scrolled(int i) {
        return false;
    }

    private int scaleX(int screenX) {
        return (int) (screenX * scaleFactorX);
    }

    private int scaleY(int screenY) {
        return (int) (screenY * scaleFactorY);
    }
}
