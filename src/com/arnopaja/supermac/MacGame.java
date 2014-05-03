package com.arnopaja.supermac;

import com.arnopaja.supermac.helpers.AssetLoader;
import com.badlogic.gdx.Game;

public class MacGame extends Game {

    @Override
    public void create() {
        AssetLoader.load();
        setScreen(new GameScreen(this));
    }

    @Override
    public void dispose() {
        super.dispose();
        getScreen().dispose();
        AssetLoader.dispose();
    }
}
