package com.arnopaja.supermac;

import com.arnopaja.supermac.helpers.AssetLoader;
import com.arnopaja.supermac.screen1.MapScreen;
import com.badlogic.gdx.Game;

public class MacGame extends Game {

    @Override
    public void create() {
        System.out.println("ZBGame Created!");
        AssetLoader.load();
        setScreen(new MapScreen());
    }

    @Override
    public void dispose() {
        super.dispose();
        AssetLoader.dispose();
    }
}
