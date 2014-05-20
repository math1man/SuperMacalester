package com.arnopaja.supermac;

import com.arnopaja.supermac.helpers.load.AssetLoader;
import com.arnopaja.supermac.helpers.load.Config;
import com.badlogic.gdx.Game;

public class MacGame extends Game {

    @Override
    public void create() {
        setScreen(new SplashScreen(this));
        AssetLoader.load(Config.retrieve());
    }

    @Override
    public void dispose() {
        super.dispose();
        getScreen().dispose();
        AssetLoader.dispose();
    }
}
