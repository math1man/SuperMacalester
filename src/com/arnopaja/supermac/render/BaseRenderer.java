package com.arnopaja.supermac.render;

import com.arnopaja.supermac.helpers.DialogueHandler;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

/**
 * @author Ari Weiland
 */
public abstract class BaseRenderer<T extends BaseController> {

    protected final DialogueHandler dialogueHandler;
    protected final ShapeRenderer shapeRenderer;
    protected final SpriteBatch batcher;
    protected final float gameWidth, gameHeight;

    private T controller;

    public BaseRenderer(DialogueHandler dialogueHandler, float gameWidth, float gameHeight) {
        this.dialogueHandler = dialogueHandler;
        this.gameWidth = gameWidth;
        this.gameHeight = gameHeight;

        OrthographicCamera cam = new OrthographicCamera();
        cam.setToOrtho(true, gameWidth, gameHeight);

        batcher = new SpriteBatch();
        batcher.setProjectionMatrix(cam.combined);
        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setProjectionMatrix(cam.combined);
    }

    public abstract void render(float runTime);

    public T getController() {
        return controller;
    }

    public void setController(T controller) {
        this.controller = controller;
    }

    public DialogueHandler getDialogueHandler() {
        return dialogueHandler;
    }

    public ShapeRenderer getShapeRenderer() {
        return shapeRenderer;
    }

    public SpriteBatch getBatcher() {
        return batcher;
    }

    public float getGameWidth() {
        return gameWidth;
    }

    public float getGameHeight() {
        return gameHeight;
    }
}
