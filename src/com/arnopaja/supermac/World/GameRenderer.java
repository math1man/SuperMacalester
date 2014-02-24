package com.arnopaja.supermac.world;

import com.arnopaja.supermac.grid.RenderGrid;
import com.arnopaja.supermac.objects.MainCharacter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;

/**
 * @author Ari Weiland
 */
public class GameRenderer {

    private com.arnopaja.supermac.world.GameWorld world;
    private OrthographicCamera cam;
    private ShapeRenderer shapeRenderer;
    private SpriteBatch batcher;

    private float gameWidth, gameHeight;

    public GameRenderer(GameWorld world, float gameWidth, float gameHeight) {
        this.world = world;
        this.gameWidth = gameWidth;
        this.gameHeight = gameHeight;
        cam = new OrthographicCamera();
        cam.setToOrtho(true, gameWidth, gameHeight);

        batcher = new SpriteBatch();
        batcher.setProjectionMatrix(cam.combined);
        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setProjectionMatrix(cam.combined);

        // Call helper methods to initialize instance variables
        initGameObjects();
        initAssets();
    }

    private void initGameObjects() {

    }

    private void initAssets() {

    }

    public void render(float delta, float runTime) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

        MainCharacter mainCharacter = world.getMainCharacter();
        Vector2 centerPosition = mainCharacter.getPosition();
        RenderGrid renderTileGrid = world.getWorldTileGrid().getRenderGrid(centerPosition);
        RenderGrid renderEntityGrid = world.getWorldEntityGrid().getRenderGrid(centerPosition);

        shapeRenderer.begin(ShapeType.Filled);

        // Draw Background color
        shapeRenderer.setColor(55 / 255.0f, 80 / 255.0f, 100 / 255.0f, 1);
        shapeRenderer.rect(0, 0, gameWidth, gameHeight);

        shapeRenderer.end();

        batcher.begin();
        batcher.end();
    }
}
