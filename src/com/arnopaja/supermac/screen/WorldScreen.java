package com.arnopaja.supermac.screen;

import com.arnopaja.supermac.MacGame;
import com.arnopaja.supermac.helpers.WorldInputHandler;
import com.arnopaja.supermac.render.WorldInterface;
import com.arnopaja.supermac.render.WorldRenderer;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;

/**
 * @author Ari Weiland
 */
public class WorldScreen implements Screen {

    private final MacGame game;

    private final WorldInterface world;
    private final WorldRenderer renderer;
    private final WorldInputHandler inputHandler;
    private final float gameWidth, gameHeight;

    private float runTime;

    public WorldScreen(MacGame game) {
        this.game = game;

        gameHeight = MacGame.GAME_HEIGHT;
        gameWidth = gameHeight * Gdx.graphics.getWidth() / Gdx.graphics.getHeight();

        world = new WorldInterface();
        renderer = new WorldRenderer(world, gameWidth, gameHeight);
        inputHandler = new WorldInputHandler(world, gameWidth, gameHeight,
                gameWidth/Gdx.graphics.getWidth(), gameHeight/Gdx.graphics.getHeight());
    }

    @Override
    public void render(float delta) {
        runTime += delta;
        world.update(delta);
        renderer.render(runTime);
    }

    @Override
    public void resize(int width, int height) {
        System.out.println("WorldScreen - resizing");
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(inputHandler);
        System.out.println("WorldScreen - show called");
    }

    @Override
    public void hide() {
        System.out.println("WorldScreen - hide called");
    }

    @Override
    public void pause() {
        System.out.println("WorldScreen - pause called");
    }

    @Override
    public void resume() {
        System.out.println("WorldScreen - resume called");
    }

    @Override
    public void dispose() {

    }

    public void goToBattle() {
        game.changeGameState(MacGame.GameState.BATTLE);
    }

    public MacGame getGame() {
        return game;
    }

    public WorldInterface getWorld() {
        return world;
    }

    public WorldRenderer getRenderer() {
        return renderer;
    }

    public float getGameHeight() {
        return gameHeight;
    }

    public float getGameWidth() {
        return gameWidth;
    }
}
