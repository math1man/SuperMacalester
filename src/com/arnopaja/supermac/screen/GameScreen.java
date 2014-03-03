package com.arnopaja.supermac.screen;

import com.arnopaja.supermac.MacGame;
import com.arnopaja.supermac.helpers.MapInputHandler;
import com.arnopaja.supermac.render.WorldInterface;
import com.arnopaja.supermac.render.WorldRenderer;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;

/**
 * @author Ari Weiland
 */
public class GameScreen implements Screen {

    private final MacGame game;

    private final WorldInterface world;
    private final WorldRenderer renderer;
    private final float gameHeight;
    private final float gameWidth;

    private float runTime;

    public GameScreen(MacGame game) {
        this.game = game;

        gameHeight = MacGame.GAME_HEIGHT;
        gameWidth = gameHeight * Gdx.graphics.getWidth() / Gdx.graphics.getHeight();

        world = new WorldInterface();
        renderer = new WorldRenderer(world, gameWidth, gameHeight);
    }

    @Override
    public void render(float delta) {
        runTime += delta;
        world.update(delta);
        renderer.render(delta, runTime);
    }

    @Override
    public void resize(int width, int height) {
        System.out.println("GameScreen - resizing");
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(new MapInputHandler(world, gameWidth, gameHeight,
                gameWidth/Gdx.graphics.getWidth(), gameHeight/Gdx.graphics.getHeight()));
        System.out.println("GameScreen - show called");
    }

    @Override
    public void hide() {
        System.out.println("GameScreen - hide called");
    }

    @Override
    public void pause() {
        System.out.println("GameScreen - pause called");
    }

    @Override
    public void resume() {
        System.out.println("GameScreen - resume called");
    }

    @Override
    public void dispose() {

    }
}
