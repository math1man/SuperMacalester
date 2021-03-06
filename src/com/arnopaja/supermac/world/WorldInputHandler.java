package com.arnopaja.supermac.world;

import com.arnopaja.supermac.GameScreen;
import com.arnopaja.supermac.helpers.InputHandler;
import com.arnopaja.supermac.helpers.dialogue.menu.PauseMenu;
import com.arnopaja.supermac.world.grid.Direction;
import com.arnopaja.supermac.world.grid.Grid;
import com.arnopaja.supermac.world.objects.MainMapCharacter;
import com.badlogic.gdx.Input.Keys;

/**
 * @author Ari Weiland
 */
public class WorldInputHandler extends InputHandler {

    public static final float SIDE_BUTTON_WIDTH = Grid.GRID_PIXEL_DIMENSION * 2f;

    public WorldInputHandler(GameScreen screen, float gameWidth, float gameHeight,
                             float scaleFactorX, float scaleFactorY) {
        super(screen, gameWidth, gameHeight, scaleFactorX, scaleFactorY);
    }

    @Override
    public boolean keyDown(int keycode) {
        if (screen.isRunning()) {
            if (getDirection(keycode) != null) {
                move(getDirection(keycode));
            } else {
                if (keycode == Keys.SPACE) {
                    screen.getMainCharacter().run(screen);
                }
            }
        } else if (screen.isDialogue() && keycode == Keys.SPACE) {
            dialogueInput(0, 0);
        } else {
            return false;
        }
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        return stop(getDirection(keycode));
    }

    @Override
    public boolean keyTyped(char c) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        float gameX = scaleX(screenX);
        float gameY = scaleY(screenY);
        if (screen.isRunning()) {
            if (gameX < SIDE_BUTTON_WIDTH && gameY < SIDE_BUTTON_WIDTH) {
                new PauseMenu().run(screen);
            } else {
                move(getDirection(gameX, gameY));
            }
        } else if (screen.isDialogue()) {
            dialogueInput(gameX, gameY);
        } else {
            return false;
        }
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        float gameX = scaleX(screenX);
        float gameY = scaleY(screenY);
        return stop(getDirection(gameX, gameY));
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
        if (!screen.getMainCharacter().move(direction)) {
            screen.getMainCharacter().run(screen);
        }
    }

    private boolean stop(Direction direction) {
        MainMapCharacter character = screen.getMainCharacter();
        if (character.continueMoving() && character.getMovingDirection() == direction) {
            character.stop();
            return true;
        }
        return false;
    }

    private Direction getDirection(float gameX, float gameY) {
        if (gameY < SIDE_BUTTON_WIDTH) {
            return Direction.EAST;
        } else if (gameY > gameHeight - SIDE_BUTTON_WIDTH) {
            return Direction.WEST;
        } else if (gameX < SIDE_BUTTON_WIDTH) {
            return Direction.NORTH;
        } else if (gameX > gameWidth - SIDE_BUTTON_WIDTH) {
            return Direction.SOUTH;
        } else {
            return null;
        }
    }

    private Direction getDirection(int keycode) {
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
