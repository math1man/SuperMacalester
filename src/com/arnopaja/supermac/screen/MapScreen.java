package com.arnopaja.supermac.screen;

import com.arnopaja.supermac.helpers.InputHandler;
import com.arnopaja.supermac.world.GameWorld;
import com.arnopaja.supermac.world.MapRenderer;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;

/**
 * @author Ari Weiland
 */
public class MapScreen implements Screen {

    public static final float GAME_HEIGHT = 480;

    private GameWorld world;
    private MapRenderer renderer;
    private float runTime;

    public MapScreen() {
        System.out.println("Screen Created!");
        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();

        // TODO: figure these out
        float gameHeight = GAME_HEIGHT;
        float gameWidth = gameHeight * screenWidth / screenHeight;

        System.out.println("Creating world...");
        world = new GameWorld();
        Gdx.input.setInputProcessor(new InputHandler(world, gameWidth, gameHeight,
                gameWidth/screenWidth, gameHeight/screenHeight));
        renderer = new MapRenderer(world, gameWidth, gameHeight);
    }

    @Override
    public void render(float delta) {
        runTime += delta;
        world.update(delta);
        renderer.render(delta, runTime);
    }

    @Override
    public void resize(int width, int height) {
        System.out.println("MapScreen - resizing");
    }

    @Override
    public void show() {
        System.out.println("MapScreen - show called");
    }

    @Override
    public void hide() {
        System.out.println("MapScreen - hide called");
    }

    @Override
    public void pause() {
        System.out.println("MapScreen - pause called");
    }

    @Override
    public void resume() {
        System.out.println("MapScreen - resume called");
    }

    @Override
    public void dispose() {

    }
}
