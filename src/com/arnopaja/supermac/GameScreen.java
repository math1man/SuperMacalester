package com.arnopaja.supermac;

import com.arnopaja.supermac.battle.Battle;
import com.arnopaja.supermac.battle.BattleInputHandler;
import com.arnopaja.supermac.battle.BattleRenderer;
import com.arnopaja.supermac.helpers.*;
import com.arnopaja.supermac.helpers.dialogue.Dialogue;
import com.arnopaja.supermac.helpers.dialogue.DialogueHandler;
import com.arnopaja.supermac.helpers.dialogue.DialogueStyle;
import com.arnopaja.supermac.inventory.Inventory;
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
    public static final float ASPECT_RATIO = 5.0f / 3; // Gdx.graphics.getWidth() / Gdx.graphics.getHeight();
    public static final float GAME_HEIGHT = 360;
    public static final float GAME_WIDTH = GAME_HEIGHT * ASPECT_RATIO;

    public static enum GameMode { WORLD, BATTLE, MENU }
    public static enum GameState { RUNNING, PAUSED, DIALOGUE }

    private final DialogueHandler dialogueHandler;
    private Plot plot;

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
        AssetLoader.prefs.clear();
        AssetLoader.prefs.flush();

        Settings.load();

        dialogueHandler = new DialogueHandler();

        world = MapLoader.generateWorld(AssetLoader.mapHandle);
        SuperParser.initParsers(world);
        SuperParser.initItems(AssetLoader.itemHandle);
        SuperParser.initSpells(AssetLoader.spellHandle);

        load();

        float scaleFactorX = GAME_WIDTH  / Gdx.graphics.getWidth();
        float scaleFactorY = GAME_HEIGHT / Gdx.graphics.getHeight();

        worldRenderer = new WorldRenderer(dialogueHandler, GAME_WIDTH, GAME_HEIGHT);
        worldRenderer.setController(world);
        worldInputHandler = new WorldInputHandler(this, GAME_WIDTH, GAME_HEIGHT, scaleFactorX, scaleFactorY);

        battleRenderer = new BattleRenderer(dialogueHandler, GAME_WIDTH, GAME_HEIGHT);
        battleInputHandler = new BattleInputHandler(this, GAME_WIDTH, GAME_HEIGHT,
                scaleFactorX, scaleFactorY);

        changeMode(GameMode.WORLD);
        state = GameState.RUNNING;
        runTime = 0;

//        dialogueHandler.setStyle(DialogueStyle.FULL_SCEEN);
        Dialogue prologue = SuperParser.parse("Prologue", AssetLoader.dialogueHandle, Dialogue.class);
        prologue.setStyle(DialogueStyle.FULL_SCEEN);
        prologue.toInteraction().run(this);
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
        save();
        Settings.save();
        worldRenderer.dispose();
        battleRenderer.dispose();
    }

    public void save() {
        SaverLoader.save(plot, Plot.class);
        SaverLoader.save(world, World.class);
        Inventory.save();
        SaverLoader.flush();
    }

    public void load() {
        plot = SaverLoader.load(Plot.class, SuperParser.parse(AssetLoader.plotHandle, Plot.class));
        SaverLoader.load(World.class, SuperParser.parse(AssetLoader.entitiesHandle, World.class));
        Inventory.load();
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
        this.battle = battle;
        this.battle.ready(world.getMainCharacter().getParty(), this);
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
