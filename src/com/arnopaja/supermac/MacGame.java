package com.arnopaja.supermac;

import com.arnopaja.supermac.helpers.AssetLoader;
import com.badlogic.gdx.Game;

public class MacGame extends Game {

    public static final float GAME_HEIGHT = 480;       // TODO: allow for variable aspect ratios (i.e. below)
    public static final float ASPECT_RATIO = 5.0f / 3; //Gdx.graphics.getWidth() / Gdx.graphics.getHeight();

    @Override
    public void create() {
        AssetLoader.load();
        setScreen(new GameScreen(this, GAME_HEIGHT * ASPECT_RATIO, GAME_HEIGHT));
    }

    @Override
    public void dispose() {
        super.dispose();
        getScreen().dispose();
        AssetLoader.dispose();
    }
}
