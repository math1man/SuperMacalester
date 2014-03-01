package com.arnopaja.supermac.helpers;

import com.arnopaja.supermac.render.BattleInterface;
import com.badlogic.gdx.InputProcessor;

/**
 * @author Ari Weiland
 */
public class BattleInputHandler implements InputProcessor {

    private BattleInterface battle;

    private float gameWidth;
    private float gameHeight;
    private float scaleFactorX;
    private float scaleFactorY;

    public BattleInputHandler(BattleInterface battle,
                              float gameWidth, float gameHeight,
                              float scaleFactorX, float scaleFactorY) {
        this.battle = battle;
        this.gameWidth = gameWidth;
        this.gameHeight = gameHeight;
        this.scaleFactorX = scaleFactorX;
        this.scaleFactorY = scaleFactorY;
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

    private int scaleX(int screenX) {
        return (int) (screenX * scaleFactorX);
    }

    private int scaleY(int screenY) {
        return (int) (screenY * scaleFactorY);
    }
}
