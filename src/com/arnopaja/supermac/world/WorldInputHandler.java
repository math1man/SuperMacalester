package com.arnopaja.supermac.world;

import com.arnopaja.supermac.helpers.BaseInputHandler;
import com.arnopaja.supermac.world.grid.Direction;
import com.arnopaja.supermac.world.grid.Grid;
import com.arnopaja.supermac.world.objects.MainMapCharacter;
import com.arnopaja.supermac.GameScreen;
import com.badlogic.gdx.Input.Keys;

/**
 * @author Ari Weiland
 */
public class WorldInputHandler extends BaseInputHandler {

    public static final int SIDE_BUTTON_WIDTH = Grid.GRID_PIXEL_DIMENSION * 2;

    private final MainMapCharacter character;

    public WorldInputHandler(GameScreen screen, float gameWidth, float gameHeight,
                             float scaleFactorX, float scaleFactorY) {
        super(screen, gameWidth, gameHeight, scaleFactorX, scaleFactorY);
        this.character = this.screen.getWorld().getMainCharacter();
    }

    @Override
    public boolean keyDown(int keycode) {
        if (screen.isRunning()) {
            move(getTouchDirection(keycode));
        } else if (screen.isDialogue()) {
            // TODO: figure out how to select options with keyboard?
            dialogueInput(0, 0);
        } else {
            return false;
        }
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        return stop(getTouchDirection(keycode));
    }

    @Override
    public boolean keyTyped(char c) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        int gameX = scaleX(screenX);
        int gameY = scaleY(screenY);
        if (screen.isRunning()) {
            move(getTouchDirection(gameX, gameY));
        } else if (screen.isDialogue()) {
            dialogueInput(gameX, gameY);
        } else {
            return false;
        }
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        int gameX = scaleX(screenX);
        int gameY = scaleY(screenY);
        return stop(getTouchDirection(gameX, gameY));
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

    private void move(Direction direction) {
        if (direction == null || !character.move(direction)) {
            character.interact().run(screen);
        }
    }

    private boolean stop(Direction direction) {
        if (character.continueMoving() && character.getMovingDirection() == direction) {
            character.stop();
            return true;
        }
        return false;
    }

    private Direction getTouchDirection(int gameX, int gameY) {
        if (gameX < SIDE_BUTTON_WIDTH) {
            return Direction.NORTH;
        } else if (gameX > gameWidth - SIDE_BUTTON_WIDTH) {
            return Direction.SOUTH;
        } else if (gameY < SIDE_BUTTON_WIDTH) {
            return Direction.EAST;
        } else if (gameY > gameHeight - SIDE_BUTTON_WIDTH) {
            return Direction.WEST;
        } else {
            return null;
        }
    }

    private Direction getTouchDirection(int keycode) {
        switch (keycode) {
            case Keys.UP:
                return Direction.EAST;
            case Keys.RIGHT:
                return Direction.SOUTH;
            case Keys.DOWN:
                return Direction.WEST;
            case Keys.LEFT:
                return Direction.NORTH;
            default:
                return null;
        }
    }
}
