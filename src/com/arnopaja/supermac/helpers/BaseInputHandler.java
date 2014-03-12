package com.arnopaja.supermac.helpers;

import com.arnopaja.supermac.GameScreen;
import com.badlogic.gdx.InputProcessor;

/**
 * @author Ari Weiland
 */
public abstract class BaseInputHandler<T extends GameScreen> implements InputProcessor {

    protected final T screen;
    protected final float gameWidth, gameHeight;
    protected final float scaleFactorX, scaleFactorY;


    public BaseInputHandler(T screen, float gameWidth, float gameHeight,
                             float scaleFactorX, float scaleFactorY) {
        this.screen = screen;
        this.gameWidth = gameWidth;
        this.gameHeight = gameHeight;
        this.scaleFactorX = scaleFactorX;
        this.scaleFactorY = scaleFactorY;
    }

    public void dialogueInput(int gameX, int gameY) {
        DialogueHandler dialogueHandler = screen.getDialogueHandler();
        Interaction interaction = dialogueHandler.onClick(gameX, gameY);
        if (interaction != null) {
            screen.endDialogue();
            screen.runInteraction(interaction);
        }
    }

    protected int scaleX(int screenX) {
        return (int) (screenX * scaleFactorX);
    }

    protected int scaleY(int screenY) {
        return (int) (screenY * scaleFactorY);
    }
}
