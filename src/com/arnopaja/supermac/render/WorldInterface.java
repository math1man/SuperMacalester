package com.arnopaja.supermac.render;

import com.arnopaja.supermac.grid.Building;
import com.arnopaja.supermac.grid.Direction;
import com.arnopaja.supermac.grid.Grid;
import com.arnopaja.supermac.helpers.AssetLoader;
import com.arnopaja.supermac.helpers.Dialogue;
import com.arnopaja.supermac.helpers.DialogueOptions;
import com.arnopaja.supermac.objects.Entity;
import com.arnopaja.supermac.objects.MainMapCharacter;
import com.arnopaja.supermac.objects.MapNPC;
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
        mainCharacter = new MainMapCharacter(worldGrid, new Vector2(1, 1), Direction.WEST);
        initBuildings();
        initCharacters();
    }

    private void initBuildings() {
        buildings = new ArrayList<Building>();
        // TODO: add buildings here
    }

    private void initCharacters() {
        // TODO: add characters here?
        MapNPC character = new MapNPC(worldGrid, new Vector2(5, 5), Direction.NORTH);
        character.setFacingSprites(AssetLoader.steven);
        character.setFacingAnimations(AssetLoader.stevenStepping);
        character.setInteractable(true);
        character.setDialogue(new Dialogue("Hi! My name is Paul and I like to code things. " +
                "I have decided I am going to work at a startup in Minneapolis this summer.<d>" +
                "I spent this past summer here working for Libby Shoop designing a website.",
                new DialogueOptions("Do you like to code?", DialogueOptions.YES_NO_OPTIONS)));
        worldGrid.putEntity(character);
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
