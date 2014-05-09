package com.arnopaja.supermac;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * @author Ari Weiland
 */
public class SplashScreen implements Screen {

    private final MacGame game;

    private SpriteBatch spriteBatch;
    private Texture splash;
    private TextureRegion splashRegion;

    public SplashScreen(MacGame game) {
        this.game = game;
    }


    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        spriteBatch.begin();
        spriteBatch.draw(splashRegion, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        spriteBatch.end();

        if(Gdx.input.justTouched()) {
            game.setScreen(new GameScreen());
        }
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void show() {
        spriteBatch = new SpriteBatch();
        splash = new Texture(Gdx.files.internal("data/splash.png"));
        splashRegion = new TextureRegion(splash, 0, 0, 800, 480);    }

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
        splash.dispose();
    }
}
