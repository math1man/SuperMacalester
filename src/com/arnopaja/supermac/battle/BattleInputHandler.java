package com.arnopaja.supermac.battle;

import com.arnopaja.supermac.helpers.InputHandler;
import com.arnopaja.supermac.GameScreen;

/**
 * @author Ari Weiland
 */
public class BattleInputHandler extends InputHandler {

    public BattleInputHandler(GameScreen screen, float gameWidth, float gameHeight,
                              float scaleFactorX, float scaleFactorY) {
        super(screen, gameWidth, gameHeight, scaleFactorX, scaleFactorY);
    }

    @Override
    public boolean keyDown(int i) {
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
        // convert screen coordinates to game coordinates
        float gameX = scaleX(screenX);
        float gameY = scaleY(screenY);
        if (screen.isDialogue()) {
            dialogueInput(gameX, gameY);
        } else {
            return false;
        }

        // return true if something happens
        return true;
    }

    @Override
    public boolean touchUp(int i, int i2, int i3, int i4) {
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
}
