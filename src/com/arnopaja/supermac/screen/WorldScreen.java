package com.arnopaja.supermac.screen;

import com.arnopaja.supermac.MacGame;
import com.arnopaja.supermac.helpers.DialogueHandler;
import com.arnopaja.supermac.helpers.WorldInputHandler;
import com.arnopaja.supermac.objects.Interaction;
import com.arnopaja.supermac.render.WorldInterface;
import com.arnopaja.supermac.render.WorldRenderer;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;

/**
 * @author Ari Weiland
 */
public class WorldScreen implements Screen {

    public enum GameState { RUNNING, PAUSED, PREBATTLE }

    private final MacGame game;

    private final WorldInterface world;
    private final WorldRenderer renderer;
    private final WorldInputHandler inputHandler;
    private final DialogueHandler dialogueHandler;

    private final float gameWidth, gameHeight;

    private float runTime;
    private GameState state;

    public WorldScreen(MacGame game) {
        this.game = game;
        this.state = GameState.RUNNING;

        gameHeight = MacGame.GAME_HEIGHT;
        gameWidth = gameHeight * Gdx.graphics.getWidth() / Gdx.graphics.getHeight();

        world = new WorldInterface();
        dialogueHandler = new DialogueHandler(gameWidth, gameHeight);
        renderer = new WorldRenderer(world, dialogueHandler, gameWidth, gameHeight);
        inputHandler = new WorldInputHandler(this, gameWidth, gameHeight,
                gameWidth/Gdx.graphics.getWidth(), gameHeight/Gdx.graphics.getHeight());
    }

    public void runInteraction(Interaction interaction) {
        interaction.runInteraction(this, dialogueHandler);
    }

    @Override
    public void render(float delta) {
        if (state == GameState.RUNNING) {
            runTime += delta;
            world.update(delta);
        }
        renderer.render(runTime);
    }

    @Override
    public void resize(int width, int height) {
        System.out.println("WorldScreen - resizing");
    }

    @Override
    public void show() {
        System.out.println("WorldScreen - show called");
        Gdx.input.setInputProcessor(inputHandler);
        state = GameState.RUNNING;
    }

    @Override
    public void hide() {
        System.out.println("WorldScreen - hide called");
        state = GameState.PAUSED;
    }

    @Override
    public void pause() {
        System.out.println("WorldScreen - pause called");
        if (state == GameState.RUNNING) {
            state = GameState.PAUSED;
        }
    }

    public void prebattle() {
        System.out.println("WorldScreen - pause called");
        state = GameState.PREBATTLE;
    }

    @Override
    public void resume() {
        System.out.println("WorldScreen - resume called");
        if (state == GameState.PAUSED) {
            state = GameState.RUNNING;
        } else if (state == GameState.PREBATTLE) {
            goToBattle();
        }
    }

    @Override
    public void dispose() {

    }

    public void goToBattle() {
        game.changeGameState(MacGame.ScreenState.BATTLE);
    }

    public WorldInterface getWorld() {
        return world;
    }

    public WorldRenderer getRenderer() {
        return renderer;
    }

    public DialogueHandler getDialogueHandler() {
        return dialogueHandler;
    }

    public float getGameHeight() {
        return gameHeight;
    }

    public float getGameWidth() {
        return gameWidth;
    }

    public GameState getState() {
        return state;
    }
}
