package com.arnopaja.supermac;

import com.arnopaja.supermac.helpers.AssetLoader;
import com.arnopaja.supermac.screen.BattleScreen;
import com.arnopaja.supermac.screen.MenuScreen;
import com.arnopaja.supermac.screen.WorldScreen;
import com.badlogic.gdx.Game;

public class MacGame extends Game {

    public static final float GAME_HEIGHT = 480;

    public enum ScreenState { MENU, WORLD, BATTLE }

    private ScreenState currentState;

    private WorldScreen worldScreen;
    private BattleScreen battleScreen;
    private MenuScreen menuScreen;

    @Override
    public void create() {
        System.out.println("MacGame Created!");
        AssetLoader.load();

        worldScreen = new WorldScreen(this);
        battleScreen = new BattleScreen(this);
        menuScreen = new MenuScreen(this);

        // TODO: start in Menu, and allow navigation
        changeGameState(ScreenState.WORLD);
    }

    public void changeGameState(ScreenState state) {
        currentState = state;
        switch (currentState) {
            case MENU:
                setScreen(menuScreen);
                break;
            case WORLD:
                setScreen(worldScreen);
                worldScreen.resume();
                break;
            case BATTLE:
                setScreen(battleScreen);
                break;
        }
    }

    public ScreenState getCurrentState() {
        return currentState;
    }

    @Override
    public void dispose() {
        super.dispose();
        worldScreen.dispose();
        battleScreen.dispose();
        menuScreen.dispose();
        AssetLoader.dispose();
    }
}
