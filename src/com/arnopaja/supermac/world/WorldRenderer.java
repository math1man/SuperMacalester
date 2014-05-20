package com.arnopaja.supermac.world;

import com.arnopaja.supermac.helpers.Renderer;
import com.arnopaja.supermac.helpers.dialogue.DialogueDisplay;
import com.arnopaja.supermac.helpers.load.AssetLoader;
import com.arnopaja.supermac.world.grid.Grid;
import com.arnopaja.supermac.world.grid.RenderGrid;
import com.arnopaja.supermac.world.objects.MainMapCharacter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector2;

/**
 * @author Ari Weiland
 */
public class WorldRenderer extends Renderer<World> {

    public WorldRenderer(DialogueDisplay dialogueDisplay, float gameWidth, float gameHeight) {
        super(dialogueDisplay, gameWidth, gameHeight);
    }

    @Override
    public void render(float runTime) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        MainMapCharacter mainCharacter = getController().getMainCharacter();
        RenderGrid renderGrid = mainCharacter.getLocation().getRenderGrid();

        renderBackgroundColor();

        Vector2 offset = Grid.RENDER_GRID_OFFSET.cpy().sub(mainCharacter.getRenderOffset());
        renderGrid.render(batch, offset, runTime);

        dialogueDisplay.render(shapeRenderer, batch);

        if (!dialogueDisplay.isDisplaying()) {
            batch.begin();
            batch.draw(AssetLoader.getPauseButton(), 0, 0);
            batch.end();
        }
    }
}
