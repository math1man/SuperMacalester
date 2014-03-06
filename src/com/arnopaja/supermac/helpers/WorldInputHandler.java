package com.arnopaja.supermac.helpers;

import com.arnopaja.supermac.grid.Direction;
import com.arnopaja.supermac.grid.Grid;
import com.arnopaja.supermac.objects.MainMapCharacter;
import com.arnopaja.supermac.screen.WorldScreen;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;

/**
 * @author Ari Weiland
 */
public class WorldInputHandler implements InputProcessor {

    public static final int SIDE_BUTTON_WIDTH = Grid.GRID_PIXEL_DIMENSION * 2;

    private WorldScreen screen;
    private MainMapCharacter character;

    private float gameWidth;
    private float gameHeight;
    private float scaleFactorX;
    private float scaleFactorY;

    public WorldInputHandler(WorldScreen screen, float gameWidth, float gameHeight,
                             float scaleFactorX, float scaleFactorY) {
        this.screen = screen;
        this.character = screen.getWorld().getMainCharacter();
        this.gameWidth = gameWidth;
        this.gameHeight = gameHeight;
        this.scaleFactorX = scaleFactorX;
        this.scaleFactorY = scaleFactorY;
    }

    @Override
    public boolean keyDown(int keycode) {
        if (screen.isRunning()) {
            switch (keycode) {
                case Keys.UP:
                    north();
                    break;
                case Keys.RIGHT:
                    east();
                    break;
                case Keys.DOWN:
                    south();
                    break;
                case Keys.LEFT:
                    west();
                    break;
                case Keys.SPACE:
                    interact();
                    break;
                default:
                    return false;
            }
        } else if (screen.isDialogue() || screen.isPrebattle()) {
            // TODO: figure out how to select options with keyboard
            dialogue(0, 0);
        } else {
            return false;
        }
        return true;
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
        if (screen.isRunning()) {
            if (gameX < SIDE_BUTTON_WIDTH) {
                west();
            } else if (gameX > gameWidth - SIDE_BUTTON_WIDTH) {
                east();
            } else if (gameY < SIDE_BUTTON_WIDTH) {
                north();
            } else if (gameY > gameHeight - SIDE_BUTTON_WIDTH) {
                south();
            } else {
                interact();
            }
        } else if (screen.isDialogue() || screen.isPrebattle()) {
            dialogue(gameX, gameY);
        } else {
            return false;
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

    public void north() {
        character.move(Direction.NORTH);
    }

    public void east() {
        character.move(Direction.EAST);
    }

    public void south() {
        character.move(Direction.SOUTH);
    }

    public void west() {
        character.move(Direction.WEST);
    }

    public void interact() {
        screen.runInteraction(character.interact());
    }

    public void dialogue(int gameX, int gameY) {
        DialogueHandler dialogueHandler = screen.getDialogueHandler();
        if (dialogueHandler.onClick(gameX, gameY)) {
            screen.endDialogue();
        }
    }

    private int scaleX(int screenX) {
        return (int) (screenX * scaleFactorX);
    }

    private int scaleY(int screenY) {
        return (int) (screenY * scaleFactorY);
    }
}
