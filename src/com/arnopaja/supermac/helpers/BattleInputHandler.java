package com.arnopaja.supermac.helpers;

import com.arnopaja.supermac.screen.GameScreen;

/**
 * @author Ari Weiland
 */
public class BattleInputHandler extends BaseInputHandler {


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
        int gameX = scaleX(screenX);
        int gameY = scaleY(screenY);

        // return true if something happens
        return false;
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
