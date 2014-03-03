package com.arnopaja.supermac;

import com.arnopaja.supermac.helpers.AssetLoader;
import com.arnopaja.supermac.helpers.Dialogue;
import com.arnopaja.supermac.screen.BattleScreen;
import com.arnopaja.supermac.screen.MenuScreen;
import com.arnopaja.supermac.screen.WorldScreen;
import com.badlogic.gdx.Game;

public class MacGame extends Game {

    public static final float GAME_HEIGHT = 480;

    public enum GameState { MENU, DIALOGUE, WORLD, BATTLE }

    private GameState currentState;

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

        Dialogue.init(this);

        // TODO: start in Menu, and allow navigation
        changeGameState(GameState.WORLD);
    }

    public void changeGameState(GameState state) {
        currentState = state;
        switch (currentState) {
            case DIALOGUE:
                // TODO: make a pause screen?
            case MENU:
                setScreen(menuScreen);
                break;
            case WORLD:
                setScreen(worldScreen);
                break;
            case BATTLE:
                setScreen(battleScreen);
                break;
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        AssetLoader.dispose();
    }
}
