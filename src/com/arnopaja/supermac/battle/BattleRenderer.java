package com.arnopaja.supermac.battle;

import com.arnopaja.supermac.helpers.BaseRenderer;
import com.arnopaja.supermac.helpers.dialogue.DialogueHandler;
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

        batch.begin();
        batch.disableBlending();
        // Render background and solid sprites
        // TODO: eventually, set this up so it doesn't stretch depending on the resolution
        batch.draw(getController().getBackground(), 0, 0, gameWidth, gameHeight);
        batch.enableBlending();
        // Render foreground and transparent sprites
        batch.end();
    }
}
