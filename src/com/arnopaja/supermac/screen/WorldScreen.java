package com.arnopaja.supermac.screen;

import com.arnopaja.supermac.MacGame;
import com.arnopaja.supermac.helpers.WorldInputHandler;
import com.arnopaja.supermac.objects.Interaction;
import com.arnopaja.supermac.render.WorldController;
import com.arnopaja.supermac.render.WorldRenderer;
import com.badlogic.gdx.Gdx;

/**
 * @author Ari Weiland
 */
public class WorldScreen extends BaseScreen<WorldController> {

    public WorldScreen(MacGame game) {
        super(game, MacGame.GAME_HEIGHT * MacGame.ASPECT_RATIO,
                MacGame.GAME_HEIGHT);
        renderer = new WorldRenderer(dialogueHandler, gameWidth, gameHeight);
        inputHandler = new WorldInputHandler(this, gameWidth, gameHeight,
                        gameWidth/Gdx.graphics.getWidth(), gameHeight/Gdx.graphics.getHeight());
        setController(new WorldController());
    }

    public void runInteraction(Interaction interaction) {
        interaction.runInteraction(this, dialogueHandler);
    }

    @Override
    public void render(float delta) {
        if (isRunning()) {
            runTime += delta;
            controller.update(delta);
        }
        // TODO: implement a pause menu for GameState.PAUSED
        renderer.render(runTime);
    }
}
