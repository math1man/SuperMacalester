package com.arnopaja.supermac.screen;

import com.arnopaja.supermac.MacGame;
import com.arnopaja.supermac.helpers.BattleInputHandler;
import com.arnopaja.supermac.render.BattleInterface;
import com.arnopaja.supermac.render.BattleRenderer;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;

/**
 * @author Ari Weiland
 */
public class BattleScreen implements Screen {

    public static final float ASPECT_RATIO = 5.0f / 3;

    private final MacGame game;

    private final BattleInterface battle;
    private final BattleRenderer renderer;
    private final BattleInputHandler inputHandler;
    private final float gameHeight;
    private final float gameWidth;

    private float runTime;

    public BattleScreen(MacGame game) {
        this.game = game;

        gameHeight = MacGame.GAME_HEIGHT;
        // TODO: eventually, revert aspect ratio to screenHeight/screenWidth
        gameWidth = gameHeight * ASPECT_RATIO;

        battle = new BattleInterface();
        renderer = new BattleRenderer(battle, gameWidth, gameHeight);
        inputHandler = new BattleInputHandler(battle, gameWidth, gameHeight,
                gameWidth/Gdx.graphics.getWidth(), gameHeight/Gdx.graphics.getHeight());
    }

    @Override
    public void render(float delta) {
        runTime += delta;
        battle.update(delta);
        renderer.render(delta, runTime);
    }

    @Override
    public void resize(int i, int i2) {

    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(inputHandler);
    }

    @Override
    public void hide() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {

    }

    public MacGame getGame() {
        return game;
    }

    public BattleInterface getBattle() {
        return battle;
    }

    public BattleRenderer getRenderer() {
        return renderer;
    }

    public float getGameHeight() {
        return gameHeight;
    }

    public float getGameWidth() {
        return gameWidth;
    }
}
