package com.arnopaja.supermac.screen;

import com.arnopaja.supermac.MacGame;
import com.arnopaja.supermac.helpers.BattleInputHandler;
import com.arnopaja.supermac.render.BattleController;
import com.arnopaja.supermac.render.BattleRenderer;
import com.badlogic.gdx.Gdx;

/**
 * @author Ari Weiland
 */
public class BattleScreen extends BaseScreen<BattleController> {

    public static final float ASPECT_RATIO = 5.0f / 3;

    public BattleScreen(MacGame game) {
        super(game, MacGame.GAME_HEIGHT * ASPECT_RATIO, MacGame.GAME_HEIGHT);
        renderer = new BattleRenderer(dialogueHandler, gameWidth, gameHeight);
        inputHandler = new BattleInputHandler(this, gameWidth, gameHeight,
                gameWidth/ Gdx.graphics.getWidth(), gameHeight/Gdx.graphics.getHeight());

    }

    @Override
    public void render(float delta) {
        runTime += delta;
        controller.update(delta);
        renderer.render(runTime);
    }
}
