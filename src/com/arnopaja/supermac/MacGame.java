package com.arnopaja.supermac;

import com.arnopaja.supermac.helpers.AssetLoader;
import com.arnopaja.supermac.render.BattleController;
import com.arnopaja.supermac.screen.BattleScreen;
import com.arnopaja.supermac.screen.MenuScreen;
import com.arnopaja.supermac.screen.WorldScreen;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;

public class MacGame extends Game {

    public static final float GAME_HEIGHT = 480;
    public static final float ASPECT_RATIO = Gdx.graphics.getWidth() / Gdx.graphics.getHeight();

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
        goToWorld();
    }

    public void goToMenu() {
        currentState = ScreenState.MENU;
        setScreen(menuScreen);
    }

    public void goToWorld() {
        currentState = ScreenState.WORLD;
        setScreen(worldScreen);
        // TODO: this may need to be fancier than just resume
        worldScreen.resume();
    }

    public void goToBattle(BattleController battle) {
        currentState = ScreenState.BATTLE;
        battleScreen.setController(battle);
        setScreen(battleScreen);
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
