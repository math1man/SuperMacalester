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
            switch (keycode) {
                case Keys.UP:
                    east();
                    break;
                case Keys.RIGHT:
                    south();
                    break;
                case Keys.DOWN:
                    west();
                    break;
                case Keys.LEFT:
                    north();
                    break;
                case Keys.SPACE:
                    interact();
                    break;
                default:
                    return false;
            }
        } else if (screen.isDialogue() || screen.isPrebattle()) {
            // TODO: figure out how to select options with keyboard
            dialogueInput(0, 0);
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
                north();
            } else if (gameX > gameWidth - SIDE_BUTTON_WIDTH) {
                south();
            } else if (gameY < SIDE_BUTTON_WIDTH) {
                east();
            } else if (gameY > gameHeight - SIDE_BUTTON_WIDTH) {
                west();
            } else {
                interact();
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
        if (!character.move(Direction.NORTH)) {
            interact();
        }
    }

    public void east() {
        if (!character.move(Direction.EAST)) {
            interact();
        }
    }

    public void south() {
        if (!character.move(Direction.SOUTH)) {
            interact();
        }
    }

    public void west() {
        if (!character.move(Direction.WEST)) {
            interact();
        }
    }

    public void interact() {
        screen.runInteraction(character.interact());
    }
}
