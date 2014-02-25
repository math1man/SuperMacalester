package com.arnopaja.supermac.world;

import com.arnopaja.supermac.grid.WorldGrid;
import com.arnopaja.supermac.objects.Building;
import com.arnopaja.supermac.objects.Character;
import com.arnopaja.supermac.objects.MainCharacter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Ari Weiland
 */
public class GameWorld {

    public enum GameState { MENU, READY, RUNNING }

    private GameState currentState;

    private WorldGrid tileGrid;
    private WorldGrid entityGrid;
    private List<Building> buildings;

    private MainCharacter mainCharacter;
    private List<Character> characters;

    public GameWorld() {
        currentState = GameState.RUNNING;
        tileGrid = new WorldGrid();
        tileGrid.fillMacMap();
        entityGrid = new WorldGrid();
    }

    private void initBuildings() {
        buildings = new ArrayList<Building>();
        // TODO: add buildings here
    }

    public void update(float delta) {
        if (currentState == GameState.MENU || currentState == GameState.READY) {
            updateReady(delta);
        } else if (currentState == GameState.RUNNING) {
            updateRunning(delta);
        }
    }

    public void updateReady(float delta) {

    }

    public void updateRunning(float delta) {
        mainCharacter.update(delta);
        for (Character character : characters) {
            character.update(delta);
        }
    }

    public void ready() {
        currentState = GameState.READY;
    }

    public void start() {
        currentState = GameState.RUNNING;
    }

    public void restart() {
        currentState = GameState.READY;
    }

    public boolean isReady() {
        return (currentState == GameState.READY);
    }

    public boolean isMenu() {
        return currentState == GameState.MENU;
    }

    public boolean isRunning() {
        return currentState == GameState.RUNNING;
    }

    public WorldGrid getTileGrid() {
        return tileGrid;
    }

    public WorldGrid getEntityGrid() {
        return entityGrid;
    }

    public MainCharacter getMainCharacter() {
        return mainCharacter;
    }
}
