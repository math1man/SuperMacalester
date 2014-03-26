package com.arnopaja.supermac.world;

import com.arnopaja.supermac.helpers.AssetLoader;
import com.arnopaja.supermac.helpers.BaseController;
import com.arnopaja.supermac.world.grid.Building;
import com.arnopaja.supermac.world.grid.Direction;
import com.arnopaja.supermac.world.grid.Grid;
import com.arnopaja.supermac.world.grid.Location;
import com.arnopaja.supermac.world.objects.Entity;
import com.arnopaja.supermac.world.objects.MainMapCharacter;
import com.badlogic.gdx.math.Vector2;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Ari Weiland
 */
public class WorldController implements BaseController {

    private Grid worldGrid;
    private Map<String, Building> buildings;

    private MainMapCharacter mainCharacter;

    public WorldController() {
        worldGrid = new Grid(AssetLoader.parseTileArray("World"));
        mainCharacter = new MainMapCharacter(new Location(worldGrid, new Vector2(1, 1), Direction.WEST));
        initBuildings();
        initCharacters();
    }

    private void initBuildings() {
        buildings = new HashMap<String, Building>();
        // TODO: add buildings here
    }

    private void addBuilding(Building building) {
        buildings.put(building.getName(), building);
    }

    private void initCharacters() {
        // TODO: add characters here?
//        MapNPC character = new MapNPC(new Location(worldGrid, new Vector2(5, 5), Direction.NORTH));
//        character.setFacingSprites(AssetLoader.steven);
//        character.setFacingAnimations(AssetLoader.stevenStepping);
//        character.setInteractable(true);
//        character.setInteraction(Interaction.dialogue(new Dialogue("Hi! My name is Paul and I like to code things. " +
//                "I have decided I am going to work at a startup in Minneapolis this summer.<d>" +
//                "I spent this past summer here working for Libby Shoop designing a website.",
//                new DialogueOptions("Do you like to code?", DialogueOptions.YES_NO_OPTIONS))));
//        worldGrid.putEntity(character);
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

    public Map<String, Building> getBuildings() {
        return buildings;
    }

    public MainMapCharacter getMainCharacter() {
        return mainCharacter;
    }
}
