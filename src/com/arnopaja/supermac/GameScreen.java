package com.arnopaja.supermac;

import com.arnopaja.supermac.battle.Battle;
import com.arnopaja.supermac.battle.BattleRenderer;
import com.arnopaja.supermac.battle.characters.BattleClass;
import com.arnopaja.supermac.battle.characters.Hero;
import com.arnopaja.supermac.battle.characters.Party;
import com.arnopaja.supermac.helpers.GameMode;
import com.arnopaja.supermac.helpers.InputHandler;
import com.arnopaja.supermac.helpers.dialogue.DialogueDisplay;
import com.arnopaja.supermac.helpers.dialogue.DialogueStep;
import com.arnopaja.supermac.helpers.dialogue.DialogueStyle;
import com.arnopaja.supermac.helpers.interaction.Interactions;
import com.arnopaja.supermac.helpers.load.AssetLoader;
import com.arnopaja.supermac.helpers.load.SaverLoader;
import com.arnopaja.supermac.helpers.SuperParser;
import com.arnopaja.supermac.inventory.Inventory;
import com.arnopaja.supermac.inventory.Spell;
import com.arnopaja.supermac.plot.Plot;
import com.arnopaja.supermac.plot.Settings;
import com.arnopaja.supermac.world.World;
import com.arnopaja.supermac.world.WorldRenderer;
import com.arnopaja.supermac.world.objects.MainMapCharacter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;

import java.util.Collections;

/**
 * @author Ari Weiland
 */
public class GameScreen implements Screen {

    // TODO: allow for variable aspect ratios (i.e. below)
    public static final float ASPECT_RATIO = 5.0f / 3; // Gdx.graphics.getWidth() / Gdx.graphics.getHeight();
    public static final float GAME_HEIGHT = 352;
    public static final float GAME_WIDTH = GAME_HEIGHT * ASPECT_RATIO;

    public static enum GameState { RUNNING, PAUSED, DIALOGUE }

    private final DialogueDisplay dialogueDisplay;

    private final GameMode<World> worldMode;
    private final GameMode<Battle> battleMode;

    private GameMode mode;
    private GameState state;
    private float runTime;

    private Plot plot;
    private Party<Hero> party;

    public GameScreen() {

        Settings.load();

        dialogueDisplay = new DialogueDisplay();
        float scaleFactorX = GAME_WIDTH  / Gdx.graphics.getWidth();
        float scaleFactorY = GAME_HEIGHT / Gdx.graphics.getHeight();
        InputHandler inputHandler = new InputHandler(this, GAME_WIDTH, GAME_HEIGHT, scaleFactorX, scaleFactorY);
        Gdx.input.setInputProcessor(inputHandler);

        worldMode = new GameMode<World>(new WorldRenderer(dialogueDisplay, GAME_WIDTH, GAME_HEIGHT));
        battleMode = new GameMode<Battle>(new BattleRenderer(dialogueDisplay, GAME_WIDTH, GAME_HEIGHT));

        // TODO: get rid of this
        Interactions.RESET.run(this);

        load();

        changeMode(worldMode);
        state = GameState.RUNNING;
        runTime = 0;

        // TODO: first load only
        new DialogueStep(AssetLoader.getDialogue("Prologue").getRaw(), DialogueStyle.FULL_SCEEN).run(this);
    }

    public void changeMode(GameMode newMode) {
        if (mode != null) {
            mode.getMusic().stop();
        }
        mode = newMode;
        mode.getMusic().play();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void render(float delta) {
        if (isRunning()) {
            runTime += delta;
            mode.getController().update(delta);
        }
        mode.getRenderer().render(runTime);
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
        save();
        if (isRunning()) {
            state = GameState.PAUSED;
        }
    }

    public void endDialogue() {
        if (isDialogue()) {
            dialogueDisplay.clear();
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
        worldMode.getRenderer().dispose();
        battleMode.getRenderer().dispose();
    }

    public void save() {
        SaverLoader.save(getPlot(), Plot.class);
        SaverLoader.save(getWorld(), World.class);
        SaverLoader.save(getParty(), Party.class);
        Inventory.save();
        SaverLoader.flush();
    }

    public void load() {
        worldMode.setController(SaverLoader.load(World.class, SuperParser.parse(AssetLoader.getEntitiesFile(), World.class)));
        plot = SaverLoader.load(Plot.class, SuperParser.parse(AssetLoader.getPlotFile(), Plot.class));
        Hero hero = new Hero("Tom", BattleClass.COMP_SCI, 1);
        hero.addSpell(Spell.getCached(0));
        party = SaverLoader.load(Party.class, new Party<Hero>(Collections.singletonList(hero)));
        Inventory.load();
    }

    public void battle(Battle newBattle) {
        newBattle.ready(party, getMainCharacter().getLocation().getRenderGrid(), this);
        battleMode.setController(newBattle);
        changeMode(battleMode);
    }

    public void world() {
        changeMode(worldMode);
    }

    public boolean isWorld() {
        return mode == worldMode;
    }

    public boolean isBattle() {
        return mode == battleMode;
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

    public DialogueDisplay getDialogueDisplay() {
        return dialogueDisplay;
    }

    public GameMode getMode() {
        return mode;
    }

    public Plot getPlot() {
        return plot;
    }

    public Party<Hero> getParty() {
        return party;
    }

    public World getWorld() {
        return worldMode.getController();
    }

    public MainMapCharacter getMainCharacter() {
        return getWorld().getMainCharacter();
    }

    public Battle getBattle() {
        return battleMode.getController();
    }
}
