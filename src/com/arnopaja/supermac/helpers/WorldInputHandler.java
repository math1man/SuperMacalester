package com.arnopaja.supermac.helpers;

import com.arnopaja.supermac.grid.Direction;
import com.arnopaja.supermac.grid.Grid;
import com.arnopaja.supermac.objects.MainMapCharacter;
import com.arnopaja.supermac.screen.WorldScreen;
import com.badlogic.gdx.InputProcessor;

/**
 * @author Ari Weiland
 */
public class WorldInputHandler implements InputProcessor {

    public static final int SIDE_BUTTON_WIDTH = Grid.GRID_PIXEL_DIMENSION * 2;

    private WorldScreen screen;

    private float gameWidth;
    private float gameHeight;
    private float scaleFactorX;
    private float scaleFactorY;

    public WorldInputHandler(WorldScreen screen, float gameWidth, float gameHeight,
                             float scaleFactorX, float scaleFactorY) {
        this.screen = screen;
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
        if (screen.getState() == WorldScreen.GameState.RUNNING) {
            MainMapCharacter character = screen.getWorld().getMainCharacter();
            if (gameX < SIDE_BUTTON_WIDTH) {
                character.move(Direction.WEST);
            } else if (gameX > gameWidth - SIDE_BUTTON_WIDTH) {
                character.move(Direction.EAST);
            } else if (gameY < SIDE_BUTTON_WIDTH) {
                character.move(Direction.NORTH);
            } else if (gameY > gameHeight - SIDE_BUTTON_WIDTH) {
                character.move(Direction.SOUTH);
            } else {
                screen.runInteraction(character.interact());
            }
        } else if (screen.isDialogue() || screen.isPrebattle()) {
            DialogueHandler dialogueHandler = screen.getDialogueHandler();
            if (dialogueHandler.onClick(gameX, gameY)) {
                screen.endDialogue();
            }
        }
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
