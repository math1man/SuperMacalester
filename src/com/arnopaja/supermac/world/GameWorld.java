package com.arnopaja.supermac.world;

import com.arnopaja.supermac.grid.Direction;
import com.arnopaja.supermac.grid.Grid;
import com.arnopaja.supermac.helpers.AssetLoader;
import com.arnopaja.supermac.grid.Building;
import com.arnopaja.supermac.objects.Entity;
import com.arnopaja.supermac.objects.MainCharacter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Ari Weiland
 */
public class GameWorld {

    public enum GameState { MENU, READY, RUNNING }

    private GameState currentState;

    private Grid worldGrid;
    private List<Building> buildings;

    private MainCharacter mainCharacter;

    public GameWorld() {
        currentState = GameState.RUNNING;
        worldGrid = new Grid(AssetLoader.parseTileArray("World"));
        mainCharacter = new MainCharacter(worldGrid, 1, 1, Direction.SOUTH);
        initBuildings();
        initCharacters();
    }

    private void initBuildings() {
        buildings = new ArrayList<Building>();
        // TODO: add buildings here
    }

    private void initCharacters() {
        // TODO: add characters here?
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
        Grid currentGrid = mainCharacter.getGrid();
        for (Entity entity : currentGrid.getEntities()) {
            entity.update(delta);
            // mainCharacter.update is called inherently because
            // the main character is an element of the current grid
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

    public Grid getWorldGrid() {
        return worldGrid;
    }

    public MainCharacter getMainCharacter() {
        return mainCharacter;
    }
}
