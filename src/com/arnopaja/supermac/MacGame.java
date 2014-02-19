package com.arnopaja.supermac;

import com.arnopaja.supermac.screen.GameScreen;
import com.badlogic.gdx.Game;

public class MacGame extends Game {

    @Override
    public void create() {
        System.out.println("ZBGame Created!");
        setScreen(new GameScreen());
    }
}
