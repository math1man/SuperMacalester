package com.arnopaja.supermac.world;

import com.arnopaja.supermac.helpers.AssetLoader;
import com.arnopaja.supermac.helpers.Controller;
import com.arnopaja.supermac.helpers.Interaction;
import com.arnopaja.supermac.helpers.MapLoader;
import com.arnopaja.supermac.helpers.parser.Parser;
import com.arnopaja.supermac.world.grid.Building;
import com.arnopaja.supermac.world.grid.Direction;
import com.arnopaja.supermac.world.grid.Grid;
import com.arnopaja.supermac.world.grid.Location;
import com.arnopaja.supermac.world.objects.Entity;
import com.arnopaja.supermac.world.objects.MainMapCharacter;
import com.arnopaja.supermac.world.objects.MapNpc;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Ari Weiland
 */
public class World implements Controller {

    private Grid worldGrid;
    private Map<String, Building> buildings = new HashMap<String, Building>();
    private MainMapCharacter mainCharacter;

    public World() {
        worldGrid = MapLoader.generateMap("Macalester");
        mainCharacter = new MainMapCharacter(new Location(worldGrid, 36, 36, Direction.WEST));
        initBuildings();
        initCharacters();
    }

    private void initBuildings() {
        // TODO: initialize buildings here
        // initBuilding("Campus Center");
    }

    private void initBuilding(String name) {
        buildings.put(name, MapLoader.generateBuilding(name));
    }

    private void initCharacters() {
        // TODO: add characters here?
        MapNpc character = new MapNpc();
        character.setAsset(AssetLoader.getAsset("Betsy"));
        character.setInteractable(true);
        character.setInteraction(Interaction.dialogue(Parser.parseDialogue("Paul", AssetLoader.dialogueHandle)));
        character.changeGrid(new Location(worldGrid, 40, 40, Direction.NORTH));
    }

    @Override
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

    public Building getBuilding(String name) {
        return buildings.get(name);
    }

    public MainMapCharacter getMainCharacter() {
        return mainCharacter;
    }
}
