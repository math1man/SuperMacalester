package com.arnopaja.supermac;

import com.arnopaja.supermac.battle.Battle;
import com.arnopaja.supermac.battle.BattleInputHandler;
import com.arnopaja.supermac.battle.BattleRenderer;
import com.arnopaja.supermac.battle.characters.BattleClass;
import com.arnopaja.supermac.battle.characters.Hero;
import com.arnopaja.supermac.battle.characters.MainParty;
import com.arnopaja.supermac.helpers.Controller;
import com.arnopaja.supermac.helpers.InputHandler;
import com.arnopaja.supermac.helpers.Renderer;
import com.arnopaja.supermac.helpers.dialogue.DialogueHandler;
import com.arnopaja.supermac.helpers.dialogue.DialogueStyle;
import com.arnopaja.supermac.helpers.dialogue.DialogueText;
import com.arnopaja.supermac.helpers.interaction.Interactions;
import com.arnopaja.supermac.helpers.load.AssetLoader;
import com.arnopaja.supermac.helpers.load.SaverLoader;
import com.arnopaja.supermac.helpers.load.SuperParser;
import com.arnopaja.supermac.inventory.Inventory;
import com.arnopaja.supermac.inventory.Spell;
import com.arnopaja.supermac.plot.Plot;
import com.arnopaja.supermac.plot.Settings;
import com.arnopaja.supermac.world.World;
import com.arnopaja.supermac.world.WorldInputHandler;
import com.arnopaja.supermac.world.WorldRenderer;
import com.arnopaja.supermac.world.objects.MainMapCharacter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;

import java.util.Collections;

/**
 * @author Ari Weiland
 */
public class GameScreen implements Screen {

    // TODO: allow for variable aspect ratios (i.e. below)
    public static final float ASPECT_RATIO = 5.0f / 3; // Gdx.graphics.getWidth() / Gdx.graphics.getHeight();
    public static final float GAME_HEIGHT = 352;
    public static final float GAME_WIDTH = GAME_HEIGHT * ASPECT_RATIO;

    public static enum GameMode { WORLD, BATTLE }
    public static enum GameState { RUNNING, PAUSED, DIALOGUE }

    private final DialogueHandler dialogueHandler;

    private final WorldRenderer worldRenderer;
    private final WorldInputHandler worldInputHandler;

    private final BattleRenderer battleRenderer;
    private final BattleInputHandler battleInputHandler;

    private Renderer currentRenderer;
    private InputHandler currentInputHandler;
    private Controller currentController;
    private Music currentMusic;

    private GameMode mode;
    private GameState state;
    private float runTime;

    private Plot plot;
    private World world;
    private MainParty party;
    private Battle battle;

    public GameScreen() {

        Settings.load();

        dialogueHandler = new DialogueHandler();

        float scaleFactorX = GAME_WIDTH  / Gdx.graphics.getWidth();
        float scaleFactorY = GAME_HEIGHT / Gdx.graphics.getHeight();

        worldRenderer = new WorldRenderer(dialogueHandler, GAME_WIDTH, GAME_HEIGHT);
        worldInputHandler = new WorldInputHandler(this, GAME_WIDTH, GAME_HEIGHT, scaleFactorX, scaleFactorY);

        battleRenderer = new BattleRenderer(dialogueHandler, GAME_WIDTH, GAME_HEIGHT);
        battleInputHandler = new BattleInputHandler(this, GAME_WIDTH, GAME_HEIGHT,
                scaleFactorX, scaleFactorY);

        AssetLoader.setCleanDialogue(Settings.isClean());

        Interactions.RESET.run(this);

        load();

        changeMode(GameMode.WORLD);
        state = GameState.RUNNING;
        runTime = 0;

        new DialogueText(AssetLoader.dialogues.get("Prologue").getRaw(), DialogueStyle.FULL_SCEEN).run(this);
    }

    public void changeMode(GameMode mode) {
        if(currentMusic != null){
            currentMusic.stop();
        }
        this.mode = mode;
        switch (this.mode) {
            case WORLD:
                currentController = world;
                currentRenderer = worldRenderer;
                currentInputHandler = worldInputHandler;
                currentMusic = AssetLoader.worldMusic;
                break;
            case BATTLE:
                battleRenderer.setController(battle);
                currentController = battle;
                currentRenderer = battleRenderer;
                currentInputHandler = battleInputHandler;
                if(battle.isBossFight()) currentMusic = AssetLoader.bossMusic;
                else currentMusic = AssetLoader.battleMusic;
                break;
        }
        Gdx.input.setInputProcessor(currentInputHandler);
        currentMusic.play();
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
        Settings.save();
        save();
        worldRenderer.dispose();
        battleRenderer.dispose();
    }

    public void save() {
        SaverLoader.save(plot, Plot.class);
        SaverLoader.save(world, World.class);
        SaverLoader.save(party, MainParty.class);
        Inventory.save();
        SaverLoader.flush();
    }

    public void load() {
        world = SaverLoader.load(World.class, SuperParser.parse(AssetLoader.entitiesHandle, World.class));
        worldRenderer.setController(world);
        plot = SaverLoader.load(Plot.class, SuperParser.parse(AssetLoader.plotHandle, Plot.class));
        Hero hero = new Hero("Tom", BattleClass.COMP_SCI, 1);
        hero.addSpell(Spell.getCached(0));
        party = SaverLoader.load(MainParty.class, new MainParty(Collections.singletonList(hero)));
        Inventory.load();
    }

    public void battle(Battle battle) {
        this.battle = battle;
        this.battle.ready(party, world.getMainCharacter().getLocation().getRenderGrid(), this);
        changeMode(GameMode.BATTLE);
    }

    public void world() {
        changeMode(GameMode.WORLD);
    }

    public boolean isWorld() {
        return mode == GameMode.WORLD;
    }

    public boolean isBattle() {
        return mode == GameMode.BATTLE;
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

    public WorldRenderer getWorldRenderer() {
        return worldRenderer;
    }

    public WorldInputHandler getWorldInputHandler() {
        return worldInputHandler;
    }

    public BattleRenderer getBattleRenderer() {
        return battleRenderer;
    }

    public BattleInputHandler getBattleInputHandler() {
        return battleInputHandler;
    }

    public Renderer getCurrentRenderer() {
        return currentRenderer;
    }

    public InputHandler getCurrentInputHandler() {
        return currentInputHandler;
    }

    public Controller getCurrentController() {
        return currentController;
    }

    public Plot getPlot() {
        return plot;
    }

    public World getWorld() {
        return world;
    }

    public MainMapCharacter getMainCharacter() {
        return getWorld().getMainCharacter();
    }

    public MainParty getParty() {
        return party;
    }

    public Battle getBattle() {
        return battle;
    }
}
