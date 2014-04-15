package com.arnopaja.supermac.world;

import com.arnopaja.supermac.helpers.*;
import com.arnopaja.supermac.helpers.dialogue.Dialogue;
import com.arnopaja.supermac.world.grid.*;
import com.arnopaja.supermac.world.objects.Entity;
import com.arnopaja.supermac.world.objects.MainMapCharacter;
import com.arnopaja.supermac.world.objects.MapNpc;

/**
 * @author Ari Weiland
 */
public class World implements Controller {

    private final MapSet maps;
    private final MainMapCharacter mainCharacter;

    public World(MapSet maps) {
        this.maps = maps;
        mainCharacter = new MainMapCharacter(new Location(this.maps.getWorld(), 36, 36, Direction.WEST));
        initCharacters();
    }

    private void initCharacters() {
        // TODO: add characters here?
        MapNpc character = new MapNpc();
        character.setAsset(AssetLoader.getAsset("Betsy"));
        character.setInteractable(true);
        character.setInteraction(Interaction.dialogue(SuperParser.parse("Betsy", AssetLoader.dialogueHandle.readString(), Dialogue.class)));
        character.changeGrid(new Location(maps.getWorld(), 40, 40, Direction.NORTH));
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

    public MainMapCharacter getMainCharacter() {
        return mainCharacter;
    }
}
