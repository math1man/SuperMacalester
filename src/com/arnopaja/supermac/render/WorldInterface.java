package com.arnopaja.supermac.render;

import com.arnopaja.supermac.grid.Building;
import com.arnopaja.supermac.grid.Direction;
import com.arnopaja.supermac.grid.Grid;
import com.arnopaja.supermac.helpers.AssetLoader;
import com.arnopaja.supermac.objects.Entity;
import com.arnopaja.supermac.objects.MainMapCharacter;
import com.arnopaja.supermac.objects.NonPlayableMapCharacter;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Ari Weiland
 */
public class WorldInterface {

    private Grid worldGrid;
    private List<Building> buildings;

    private MainMapCharacter mainCharacter;

    public WorldInterface() {
        worldGrid = new Grid(AssetLoader.parseTileArray("World"));
        mainCharacter = new MainMapCharacter(worldGrid, new Vector2(1, 1), Direction.SOUTH);
        NonPlayableMapCharacter character = new NonPlayableMapCharacter(worldGrid, new Vector2(13, 5), Direction.WEST);
        character.setFacingSprites(AssetLoader.steven);
        character.setInteractable(true);
        worldGrid.putEntity(character);
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
        Grid currentGrid = mainCharacter.getGrid();
        for (Entity entity : currentGrid.getEntities()) {
            entity.update(delta);
            // mainCharacter.update is called inherently because
            // the main character is an element of the current grid
        }
    }

    public Grid getWorldGrid() {
        return worldGrid;
    }

    public MainMapCharacter getMainCharacter() {
        return mainCharacter;
    }
}
