package com.arnopaja.supermac.screen;

import com.arnopaja.supermac.MacGame;
import com.arnopaja.supermac.helpers.DialogueHandler;
import com.arnopaja.supermac.helpers.WorldInputHandler;
import com.arnopaja.supermac.objects.Interaction;
import com.arnopaja.supermac.render.BattleInterface;
import com.arnopaja.supermac.render.WorldInterface;
import com.arnopaja.supermac.render.WorldRenderer;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;

/**
 * @author Ari Weiland
 */
public class WorldScreen implements Screen {

    public enum GameState { RUNNING, PAUSED, DIALOGUE, PREBATTLE }

    private final MacGame game;

    private final WorldInterface world;
    private final WorldRenderer renderer;
    private final WorldInputHandler inputHandler;
    private final DialogueHandler dialogueHandler;

    private final float gameWidth, gameHeight;

    private float runTime;
    private GameState state;

    private BattleInterface prebattle;

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
        if (isRunning()) {
            runTime += delta;
            world.update(delta);
        }
        // TODO: implement a pause menu for GameState.PAUSED
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

    public void dialogue() {
        System.out.println("WorldScreen - dialogue called");
        state = GameState.DIALOGUE;
    }

    public void prebattle(BattleInterface prebattle) {
        System.out.println("WorldScreen - prebattle called");
        state = GameState.PREBATTLE;
        this.prebattle = prebattle;
    }

    public void endDialogue() {
        System.out.println("WorldScreen - end dialogueInput called");
        if (state == GameState.DIALOGUE) {
            state = GameState.RUNNING;
        } else if (state == GameState.PREBATTLE) {
            goToBattle(prebattle);
        }
    }

    @Override
    public void resume() {
        System.out.println("WorldScreen - resume called");
        dialogueHandler.clear();
        if (state == GameState.PAUSED) {
            state = GameState.RUNNING;
        }
    }

    @Override
    public void dispose() {

    }

    public void goToBattle(BattleInterface battle) {
        game.goToBattle(battle);
    }

    public boolean isRunning() {
        return state == GameState.RUNNING;
    }

    public boolean isPause() {
        return state == GameState.PAUSED;
    }

    public boolean isDialogue() {
        return state == GameState.DIALOGUE;
    }

    public boolean isPrebattle() {
        return state == GameState.PREBATTLE;
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
