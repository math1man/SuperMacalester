package com.arnopaja.supermac.render;

import com.arnopaja.supermac.grid.Building;
import com.arnopaja.supermac.grid.Direction;
import com.arnopaja.supermac.grid.Grid;
import com.arnopaja.supermac.helpers.AssetLoader;
import com.arnopaja.supermac.helpers.Dialogue;
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
        NonPlayableMapCharacter character = new NonPlayableMapCharacter(worldGrid, new Vector2(5, 5), Direction.WEST);
        character.setFacingSprites(AssetLoader.steven);
        character.setFacingAnimations(AssetLoader.stevenStepping);
        character.setInteractable(true);
        character.setDialogue(new Dialogue("Hi! My name is Paul and I like to code things. I smoke lots of weed and am " +
                "trying to get a job for this summer.<d>" +
                "Actually, I have decided I am going to work at a startup in Minneapolis this summer.",
                "<options:Do you like weed?>\n" +
                "<option:Yes>\n" +
                "<option:No>\n" +
                "<option:Maybe>\n" +
                "<option:Never tried it>\n" +
                "</options>"));
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
        // Cannot just iterate over currentGrid.getEntities() because we cannot modify that iterable
        Object[] entities = currentGrid.getEntities().toArray();
        for (Object entity : entities) {
            ((Entity) entity).update(delta);
        }
    }

    public Grid getWorldGrid() {
        return worldGrid;
    }

    public MainMapCharacter getMainCharacter() {
        return mainCharacter;
    }
}
