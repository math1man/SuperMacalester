package com.arnopaja.supermac.render;

import com.arnopaja.supermac.grid.Grid;
import com.arnopaja.supermac.grid.RenderGrid;
import com.arnopaja.supermac.helpers.Dialogue;
import com.arnopaja.supermac.objects.MainMapCharacter;
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
public class WorldRenderer {

    private WorldInterface world;
    private OrthographicCamera cam;
    private ShapeRenderer shapeRenderer;
    private SpriteBatch batcher;

    private final float gameWidth, gameHeight;
    private final int renderGridWidth, renderGridHeight;
    private final Vector2 renderOffset;

    public WorldRenderer(WorldInterface world, float gameWidth, float gameHeight) {
        this.world = world;
        this.gameWidth = gameWidth;
        this.gameHeight = gameHeight;
        this.renderGridWidth = getRenderDimension(gameWidth);
        this.renderGridHeight = getRenderDimension(gameHeight);
        this.renderOffset = new Vector2(renderGridWidth, renderGridHeight)
                .add(new Vector2(gameWidth, gameHeight).scl(-1.0f/Grid.GRID_PIXEL_DIMENSION))
                .scl(0.5f);

        cam = new OrthographicCamera();
        cam.setToOrtho(true, gameWidth, gameHeight);

        batcher = new SpriteBatch();
        batcher.setProjectionMatrix(cam.combined);
        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setProjectionMatrix(cam.combined);
    }

    private static int getRenderDimension(float gameDimension) {
        int renderDimension = (int) Math.ceil(gameDimension / Grid.GRID_PIXEL_DIMENSION);
        if (renderDimension % 2 == 0) {
            renderDimension += 3;
        } else {
            renderDimension += 2;
        }
        return renderDimension;
    }

    public void render(float delta, float runTime) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

        MainMapCharacter mainCharacter = world.getMainCharacter();
        Vector2 centerPosition = mainCharacter.getPosition();
        RenderGrid renderGrid = world.getWorldGrid().getRenderGrid(centerPosition, renderGridWidth, renderGridHeight);

        shapeRenderer.begin(ShapeType.Filled);

        // Draw Background color
        shapeRenderer.setColor(0, 0, 0, 1);
        shapeRenderer.rect(0, 0, gameWidth, gameHeight);

        shapeRenderer.end();

        batcher.begin();
        batcher.disableBlending();
        Vector2 offset = renderOffset.cpy().add(mainCharacter.getMovingOffset()).scl(-1);
        renderGrid.renderTiles(batcher, offset, runTime);    // Render the base tiles
        batcher.enableBlending();
        renderGrid.renderEntities(batcher, offset, runTime); // Render entities on top of the tiles
        Dialogue.render(batcher);
        batcher.end();
    }
}
