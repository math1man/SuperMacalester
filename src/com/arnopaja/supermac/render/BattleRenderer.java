package com.arnopaja.supermac.render;

import com.arnopaja.supermac.helpers.DialogueHandler;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

/**
 * @author Ari Weiland
 */
public class BattleRenderer extends BaseRenderer<BattleController> {

    public BattleRenderer(DialogueHandler dialogueHandler, float gameWidth, float gameHeight) {
        super(dialogueHandler, gameWidth, gameHeight);
    }

    public void render(float runTime) {
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
        batcher.draw(getController().getBackground(), 0, 0, gameWidth, gameHeight);
        batcher.enableBlending();
        // Render foreground and transparent sprites
        batcher.end();
    }
}
