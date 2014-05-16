package com.arnopaja.supermac.helpers;

import com.arnopaja.supermac.GameScreen;
import com.badlogic.gdx.InputProcessor;

/**
 * TODO: make this non-abstract and universal maybe?
 * @author Ari Weiland
 */
public abstract class InputHandler implements InputProcessor {

    protected final GameScreen screen;
    protected final float gameWidth, gameHeight;
    protected final float scaleFactorX, scaleFactorY;

    public InputHandler(GameScreen screen, float gameWidth, float gameHeight,
                        float scaleFactorX, float scaleFactorY) {
        this.screen = screen;
        this.gameWidth = gameWidth;
        this.gameHeight = gameHeight;
        this.scaleFactorX = scaleFactorX;
        this.scaleFactorY = scaleFactorY;
    }

    public void dialogueInput(float gameX, float gameY) {
        screen.getDialogueHandler().onClick(gameX, gameY).run(screen);
    }

    protected float scaleX(int screenX) {
        return screenX * scaleFactorX;
    }

    protected float scaleY(int screenY) {
        return screenY * scaleFactorY;
    }
}
