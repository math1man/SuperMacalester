package com.arnopaja.supermac.helpers;

import com.arnopaja.supermac.helpers.dialogue.DialogueHandler;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

/**
 * @author Ari Weiland
 */
public abstract class Renderer<T extends Controller> {

    protected final DialogueHandler dialogueHandler;
    protected final ShapeRenderer shapeRenderer;
    protected final SpriteBatch batch;
    protected final float gameWidth, gameHeight;

    private T controller;

    public Renderer(DialogueHandler dialogueHandler, float gameWidth, float gameHeight) {
        this.dialogueHandler = dialogueHandler;
        this.gameWidth = gameWidth;
        this.gameHeight = gameHeight;

        OrthographicCamera cam = new OrthographicCamera();
        cam.setToOrtho(true, gameWidth, gameHeight);

        batch = new SpriteBatch();
        batch.setProjectionMatrix(cam.combined);
        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setProjectionMatrix(cam.combined);
    }

    public abstract void render(float runTime);

    public void renderBackgroundColor() {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(0, 0, 0, 1);
        shapeRenderer.rect(0, 0, gameWidth, gameHeight);
        shapeRenderer.end();
    }

    public T getController() {
        return controller;
    }

    public void setController(T controller) {
        this.controller = controller;
    }

    public void dispose() {
        shapeRenderer.dispose();
        batch.dispose();
    }
}
