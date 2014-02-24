package com.arnopaja.supermac.screen1;

import com.arnopaja.supermac.helpers.InputHandler;
import com.arnopaja.supermac.world1.GameWorld;
import com.arnopaja.supermac.world1.MapRenderer;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;

/**
 * @author Ari Weiland
 */
public class MapScreen implements Screen {

    private GameWorld world;
    private MapRenderer renderer;
    private float runTime;

    public MapScreen() {
        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();

        // TODO: figure these out
        float gameWidth = 136;
        float gameHeight = screenHeight / (screenWidth / gameWidth);

        world = new GameWorld();
        Gdx.input.setInputProcessor(new InputHandler(world, screenWidth / gameWidth, screenHeight / gameHeight));
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
