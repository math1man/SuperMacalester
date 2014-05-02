package com.arnopaja.supermac.battle;

import com.arnopaja.supermac.helpers.BaseRenderer;
import com.arnopaja.supermac.helpers.dialogue.DialogueHandler;
import com.arnopaja.supermac.world.grid.Grid;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;

/**
 * @author Ari Weiland
 */
public class BattleRenderer extends BaseRenderer<Battle> {

    public BattleRenderer(DialogueHandler dialogueHandler, float gameWidth, float gameHeight) {
        super(dialogueHandler, gameWidth, gameHeight);
    }

    public void render(float runTime) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

        renderBackgroundColor();

        getController().getBackgroundGrid().render(batch, Grid.RENDER_GRID_OFFSET, runTime);
        dialogueHandler.render(shapeRenderer, batch);
    }
}
