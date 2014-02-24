package com.arnopaja.supermac.world;

import com.arnopaja.supermac.objects.Building;
import com.arnopaja.supermac.grid.WorldGrid;
import com.arnopaja.supermac.objects.MainCharacter;

import java.util.List;

/**
 * @author Ari Weiland
 */
public class GameWorld {

    public enum GameState { MENU, READY, RUNNING }

    private GameState currentState;

    private WorldGrid worldTileGrid;
    private WorldGrid worldEntityGrid;
    private List<Building> buildings;

    private MainCharacter mainCharacter;

    public GameWorld() {
        currentState = GameState.MENU;
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

    public WorldGrid getWorldTileGrid() {
        return worldTileGrid;
    }

    public WorldGrid getWorldEntityGrid() {
        return worldEntityGrid;
    }

    public MainCharacter getMainCharacter() {
        return mainCharacter;
    }
}
