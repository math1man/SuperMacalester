package com.arnopaja.supermac.helpers;

import com.arnopaja.supermac.grid.Direction;
import com.arnopaja.supermac.grid.Grid;
import com.arnopaja.supermac.objects.Interaction;
import com.arnopaja.supermac.objects.MainMapCharacter;
import com.arnopaja.supermac.screen.WorldScreen;
import com.badlogic.gdx.Input.Keys;

/**
 * @author Ari Weiland
 */
public class WorldInputHandler extends BaseInputHandler<WorldScreen> {

    public static final int SIDE_BUTTON_WIDTH = Grid.GRID_PIXEL_DIMENSION * 2;

    private MainMapCharacter character;

    public WorldInputHandler(WorldScreen screen, float gameWidth, float gameHeight,
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
        } else if (screen.isDialogue() || screen.isPrebattle()) {
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

    public void dialogueInput(int gameX, int gameY) {
        DialogueHandler dialogueHandler = screen.getDialogueHandler();
        Interaction interaction = dialogueHandler.onClick(gameX, gameY);
        if (interaction != null) {
            screen.endDialogue();
            screen.runInteraction(interaction);
        }
    }
}
