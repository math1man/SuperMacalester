package com.arnopaja.supermac.screen;

import com.arnopaja.supermac.MacGame;
import com.arnopaja.supermac.helpers.BaseInputHandler;
import com.arnopaja.supermac.helpers.DialogueHandler;
import com.arnopaja.supermac.render.BaseController;
import com.arnopaja.supermac.render.BaseRenderer;
import com.arnopaja.supermac.render.BattleController;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;

/**
 * @author Ari Weiland
 */
public abstract class BaseScreen<T extends BaseController> implements Screen {

    public enum GameState { RUNNING, PAUSED, DIALOGUE }

    protected final MacGame game;
    protected final DialogueHandler dialogueHandler;
    protected final float gameWidth, gameHeight;

    protected BaseRenderer<T> renderer;
    protected BaseInputHandler inputHandler;
    protected T controller;

    protected GameState state;
    protected float runTime;

    protected boolean isPreBattle = false;
    protected BattleController prebattle;

    protected BaseScreen(MacGame game, float gameWidth, float gameHeight) {
        this.game = game;
        this.dialogueHandler = new DialogueHandler(gameWidth, gameHeight);
        this.gameWidth = gameWidth;
        this.gameHeight = gameHeight;
        this.state = GameState.RUNNING;
    }

    @Override
    public void resize(int width, int height) {
        System.out.println("WorldScreen - resizing");
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(inputHandler);
        state = GameState.RUNNING;
    }

    @Override
    public void hide() {
        state = GameState.PAUSED;
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

    public void prebattle(BattleController prebattle) {
        dialogue();
        isPreBattle = true;
        this.prebattle = prebattle;
    }

    public void endDialogue() {
        if (isDialogue()) {
            dialogueHandler.clear();
            state = GameState.RUNNING;
        }
        if (isPreBattle) {
            goToBattle(prebattle);
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
        game.goToBattle(battle);
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

    public MacGame getGame() {
        return game;
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

    public BaseRenderer<T> getRenderer() {
        return renderer;
    }

    public BaseInputHandler getInputHandler() {
        return inputHandler;
    }

    public T getController() {
        return controller;
    }

    public void setController(T controller) {
        this.controller = controller;
    }

    public GameState getState() {
        return state;
    }

    public float getRunTime() {
        return runTime;
    }
}
