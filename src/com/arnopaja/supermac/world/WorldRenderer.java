package com.arnopaja.supermac.world;

import com.arnopaja.supermac.helpers.BaseRenderer;
import com.arnopaja.supermac.world.grid.Grid;
import com.arnopaja.supermac.world.grid.RenderGrid;
import com.arnopaja.supermac.helpers.DialogueHandler;
import com.arnopaja.supermac.world.objects.MainMapCharacter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;

/**
 * @author Ari Weiland
 */
public class WorldRenderer extends BaseRenderer<WorldController> {

    private final int renderGridWidth, renderGridHeight;
    private final Vector2 renderOffset;

    public WorldRenderer(DialogueHandler dialogueHandler, float gameWidth, float gameHeight) {
        super(dialogueHandler, gameWidth, gameHeight);

        this.renderGridWidth = getRenderDimension(gameWidth);
        this.renderGridHeight = getRenderDimension(gameHeight);
        this.renderOffset = new Vector2(gameWidth, gameHeight)
                .scl(-1.0f / Grid.GRID_PIXEL_DIMENSION)
                .add(new Vector2(renderGridWidth, renderGridHeight))
                .scl(0.5f);
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

    @Override
    public void render(float runTime) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

        MainMapCharacter mainCharacter = getController().getMainCharacter();
        Vector2 centerPosition = mainCharacter.getPosition();
        RenderGrid renderGrid = getController().getWorldGrid()
                .getRenderGrid(centerPosition, renderGridWidth, renderGridHeight);

        shapeRenderer.begin(ShapeType.Filled);

        // Draw Background color
        shapeRenderer.setColor(0, 0, 0, 1);
        shapeRenderer.rect(0, 0, gameWidth, gameHeight);

        shapeRenderer.end();

        batcher.begin();
        batcher.disableBlending();

        // Render the base tiles
        Vector2 offset = renderOffset.cpy().add(mainCharacter.getMovingOffset()).scl(-1);
        renderGrid.renderTiles(batcher, offset, runTime);

        batcher.enableBlending();

        // Render entities on top of the tiles
        renderGrid.renderEntities(batcher, offset, runTime);

        // Render any dialogue present
        dialogueHandler.render(shapeRenderer, batcher);

        batcher.end();
    }
}
