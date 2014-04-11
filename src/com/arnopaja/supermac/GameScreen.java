package com.arnopaja.supermac;

import com.arnopaja.supermac.battle.Battle;
import com.arnopaja.supermac.battle.BattleInputHandler;
import com.arnopaja.supermac.battle.BattleRenderer;
import com.arnopaja.supermac.helpers.Controller;
import com.arnopaja.supermac.helpers.BaseInputHandler;
import com.arnopaja.supermac.helpers.BaseRenderer;
import com.arnopaja.supermac.helpers.dialogue.DialogueHandler;
import com.arnopaja.supermac.plot.Plot;
import com.arnopaja.supermac.plot.Settings;
import com.arnopaja.supermac.world.World;
import com.arnopaja.supermac.world.WorldInputHandler;
import com.arnopaja.supermac.world.WorldRenderer;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;

/**
 * @author Ari Weiland
 */
public class GameScreen implements Screen {

    // TODO: allow for variable aspect ratios (i.e. below)
    public static final float ASPECT_RATIO = 5.0f / 3; //Gdx.graphics.getWidth() / Gdx.graphics.getHeight();
    public static final int GAME_HEIGHT = 480;

    public static enum GameMode { WORLD, BATTLE, MENU }
    public static enum GameState { RUNNING, PAUSED, DIALOGUE }

    private final DialogueHandler dialogueHandler;
    private final Plot plot;

    private final World world;
    private final WorldRenderer worldRenderer;
    private final WorldInputHandler worldInputHandler;

    private Battle battle;
    private final BattleRenderer battleRenderer;
    private final BattleInputHandler battleInputHandler;

    private BaseRenderer currentRenderer;
    private BaseInputHandler currentInputHandler;
    private Controller currentController;

    private GameMode mode;
    private GameState state;
    private float runTime;

    public GameScreen() {
        Settings.gameHeight = GAME_HEIGHT;
        Settings.gameWidth = GAME_HEIGHT * ASPECT_RATIO;
        Settings.load();

        dialogueHandler = new DialogueHandler(Settings.gameWidth, Settings.gameHeight);

        world = new World();

        plot = new Plot(world);

        float scaleFactorX = Settings.gameWidth/Gdx.graphics.getWidth();
        float scaleFactorY = Settings.gameHeight/Gdx.graphics.getHeight();

        worldRenderer = new WorldRenderer(dialogueHandler, Settings.gameWidth, Settings.gameHeight);
        worldRenderer.setController(world);
        worldInputHandler = new WorldInputHandler(this, Settings.gameWidth, Settings.gameHeight,
                scaleFactorX, scaleFactorY);

        battleRenderer = new BattleRenderer(dialogueHandler, Settings.gameWidth, Settings.gameHeight);
        battleInputHandler = new BattleInputHandler(this, Settings.gameWidth, Settings.gameHeight,
                scaleFactorX, scaleFactorY);

        changeMode(GameMode.WORLD);
        state = GameState.RUNNING;
        runTime = 0;
    }

    public void changeMode(GameMode mode) {
        this.mode = mode;
        switch (this.mode) {
            case WORLD:
                currentController = world;
                currentRenderer = worldRenderer;
                currentInputHandler = worldInputHandler;
                break;
            case BATTLE:
                currentController = battle;
                battleRenderer.setController(battle);
                currentRenderer = battleRenderer;
                currentInputHandler = battleInputHandler;
                break;
            case MENU:
                // TODO: implement menu stuff
                break;
        }
        Gdx.input.setInputProcessor(currentInputHandler);
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void render(float delta) {
        if (isRunning()) {
            runTime += delta;
            currentController.update(delta);
        }
        // TODO: implement a pause menu for GameState.PAUSED
        currentRenderer.render(runTime);
    }

    @Override
    public void show() {
        resume();
    }

    @Override
    public void hide() {
        pause();
    }

    public void dialogue() {
        state = GameState.DIALOGUE;
    }

    @Override
    public void pause() {
        if (isRunning()) {
            state = GameState.PAUSED;
        }
    }

    public void endDialogue() {
        if (isDialogue()) {
            dialogueHandler.clear();
            state = GameState.RUNNING;
        }
    }

    @Override
    public void resume() {
        if (isPaused()) {
            state = GameState.RUNNING;
        }
    }

    @Override
    public void dispose() {
        worldRenderer.dispose();
        battleRenderer.dispose();
    }

    public void goToBattle(Battle battle) {
        setBattle(battle);
        changeMode(GameMode.BATTLE);
    }

    public boolean isWorld() {
        return mode == GameMode.WORLD;
    }

    public boolean isBattle() {
        return mode == GameMode.BATTLE;
    }

    public boolean isMenu() {
        return mode == GameMode.MENU;
    }

    public boolean isRunning() {
        return state == GameState.RUNNING;
    }

    public boolean isPaused() {
        return state == GameState.PAUSED;
    }

    public boolean isDialogue() {
        return state == GameState.DIALOGUE;
    }

    public DialogueHandler getDialogueHandler() {
        return dialogueHandler;
    }

    public World getWorld() {
        return world;
    }

    public WorldRenderer getWorldRenderer() {
        return worldRenderer;
    }

    public WorldInputHandler getWorldInputHandler() {
        return worldInputHandler;
    }

    public Battle getBattle() {
        return battle;
    }

    public void setBattle(Battle battle) {
        this.battle = new Battle(battle, dialogueHandler);
    }

    public BattleRenderer getBattleRenderer() {
        return battleRenderer;
    }

    public BattleInputHandler getBattleInputHandler() {
        return battleInputHandler;
    }

    public BaseRenderer getCurrentRenderer() {
        return currentRenderer;
    }

    public BaseInputHandler getCurrentInputHandler() {
        return currentInputHandler;
    }

    public Controller getCurrentController() {
        return currentController;
    }
}
