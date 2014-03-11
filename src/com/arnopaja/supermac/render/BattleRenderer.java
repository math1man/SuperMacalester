package com.arnopaja.supermac.render;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

/**
 * @author Ari Weiland
 */
public class BattleRenderer {

    private BattleInterface battleInterface;
    private OrthographicCamera cam;
    private ShapeRenderer shapeRenderer;
    private SpriteBatch batcher;

    private final float gameWidth, gameHeight;

    public BattleRenderer(BattleInterface battleInterface, float gameWidth, float gameHeight) {
        this.battleInterface = battleInterface;
        this.gameWidth = gameWidth;
        this.gameHeight = gameHeight;

        cam = new OrthographicCamera();
        cam.setToOrtho(true, gameWidth, gameHeight);

        batcher = new SpriteBatch();
        batcher.setProjectionMatrix(cam.combined);
        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setProjectionMatrix(cam.combined);
    }

    public void render(float delta, float runTime) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        // Draw Background color
        shapeRenderer.setColor(0, 0, 0, 1);
        shapeRenderer.rect(0, 0, gameWidth, gameHeight);

        // Draw other shapes?

        shapeRenderer.end();

        batcher.begin();
        batcher.disableBlending();
        // Render background and solid sprites
        // TODO: eventually, set this up so it doesn't stretch depending on the resolution
        batcher.draw(battleInterface.getBackground(), 0, 0, gameWidth, gameHeight);
        batcher.enableBlending();
        // Render foreground and transparent sprites
        batcher.end();
    }
}
