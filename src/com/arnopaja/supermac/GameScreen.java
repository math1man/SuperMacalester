package com.arnopaja.supermac;

import com.arnopaja.supermac.battle.BattleController;
import com.arnopaja.supermac.battle.BattleInputHandler;
import com.arnopaja.supermac.battle.BattleRenderer;
import com.arnopaja.supermac.helpers.*;
import com.arnopaja.supermac.world.WorldController;
import com.arnopaja.supermac.world.WorldInputHandler;
import com.arnopaja.supermac.world.WorldRenderer;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;

/**
 * @author Ari Weiland
 */
public class GameScreen implements Screen {

    public enum GameMode { WORLD, BATTLE, MENU }
    public enum GameState { RUNNING, PAUSED, DIALOGUE }

    private final DialogueHandler dialogueHandler;
    private final float gameWidth, gameHeight;

    private final WorldController world;
    private final WorldRenderer worldRenderer;
    private final WorldInputHandler worldInputHandler;

    private BattleController battle;
    private final BattleRenderer battleRenderer;
    private final BattleInputHandler battleInputHandler;

    private BaseRenderer currentRenderer;
    private BaseInputHandler currentInputHandler;
    private BaseController currentController;

    private GameMode mode;
    private GameState state;
    private float runTime;
    private boolean isPreBattle = false;

    public GameScreen(float gameWidth, float gameHeight) {
        this.dialogueHandler = new DialogueHandler(gameWidth, gameHeight);
        this.gameWidth = gameWidth;
        this.gameHeight = gameHeight;

        world = new WorldController();
        worldRenderer = new WorldRenderer(dialogueHandler, gameWidth, gameHeight);
        worldRenderer.setController(world);
        worldInputHandler = new WorldInputHandler(this, gameWidth, gameHeight,
                gameWidth/Gdx.graphics.getWidth(), gameHeight/Gdx.graphics.getHeight());

        battleRenderer = new BattleRenderer(dialogueHandler, gameWidth, gameHeight);
        battleInputHandler = new BattleInputHandler(this, gameWidth, gameHeight,
                gameWidth/ Gdx.graphics.getWidth(), gameHeight/Gdx.graphics.getHeight());

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

    public void preBattle(BattleController battle) {
        dialogue();
        isPreBattle = true;
        setBattle(battle);
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
        if (isPreBattle) {
            goToBattle(battle);
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

    }

    public void goToBattle(BattleController battle) {
        isPreBattle = false;
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

    public boolean isPrebattle() {
        return isDialogue() && isPreBattle;
    }

    public DialogueHandler getDialogueHandler() {
        return dialogueHandler;
    }

    public float getGameWidth() {
        return gameWidth;
    }

    public float getGameHeight() {
        return gameHeight;
    }

    public WorldController getWorld() {
        return world;
    }

    public WorldRenderer getWorldRenderer() {
        return worldRenderer;
    }

    public WorldInputHandler getWorldInputHandler() {
        return worldInputHandler;
    }

    public BattleController getBattle() {
        return battle;
    }

    public void setBattle(BattleController battle) {
        this.battle = new BattleController(dialogueHandler, battle);
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

    public BaseController getCurrentController() {
        return currentController;
    }
}
