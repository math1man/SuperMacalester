package com.arnopaja.supermac.battle;

import com.arnopaja.supermac.battle.characters.Hero;
import com.arnopaja.supermac.battle.characters.Party;
import com.arnopaja.supermac.helpers.Renderer;
import com.arnopaja.supermac.helpers.dialogue.Dialogue;
import com.arnopaja.supermac.helpers.dialogue.DialogueHandler;
import com.arnopaja.supermac.helpers.dialogue.DialogueStyle;
import com.arnopaja.supermac.helpers.dialogue.DialogueText;
import com.arnopaja.supermac.helpers.interaction.Interactions;
import com.arnopaja.supermac.world.grid.Grid;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;

/**
 * @author Ari Weiland
 */
public class BattleRenderer extends Renderer<Battle> {

    public BattleRenderer(DialogueHandler dialogueHandler, float gameWidth, float gameHeight) {
        super(dialogueHandler, gameWidth, gameHeight);
    }

    public void render(float runTime) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        renderBackgroundColor();

        getController().getBackgroundGrid().render(batch, Grid.RENDER_GRID_OFFSET, runTime);

        Party<Hero> mainParty = getController().getMainParty();

        Dialogue status = new DialogueText(mainParty.status(), Interactions.NULL, DialogueStyle.BATTLE_STATUS);
        dialogueHandler.display(status);
        dialogueHandler.render(shapeRenderer, batch);

        // TODO: render more shit
    }
}
