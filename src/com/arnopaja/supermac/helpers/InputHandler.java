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
        if (gameX < Grid.GRID_PIXEL_DIMENSION) {
            character.move(Direction.WEST);
        } else if (gameX > gameWidth - Grid.GRID_PIXEL_DIMENSION) {
            character.move(Direction.EAST);
        } else if (gameY < Grid.GRID_PIXEL_DIMENSION) {
            character.move(Direction.NORTH);
        } else if (gameY > gameHeight - Grid.GRID_PIXEL_DIMENSION) {
            character.move(Direction.SOUTH);
        } else {
            character.interact();
        }
        return false;
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
