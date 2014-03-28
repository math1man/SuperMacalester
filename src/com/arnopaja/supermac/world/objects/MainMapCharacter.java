package com.arnopaja.supermac.world.objects;

import com.arnopaja.supermac.helpers.AssetLoader;
import com.arnopaja.supermac.helpers.Interaction;
import com.arnopaja.supermac.world.grid.Direction;
import com.arnopaja.supermac.world.grid.Location;

/**
 * @author Ari Weiland
 */
public class MainMapCharacter extends MapCharacter {

    public MainMapCharacter(Location location) {
        super(location, false, AssetLoader.mainChar, AssetLoader.mainCharAnim);
    }

    public Interaction interact() {
        return interact(this);
    }

    @Override
    public Interaction interact(MainMapCharacter character) {
        Entity entity = getGrid().getEntity(Direction.getAdjacent(getPosition(), getFacing()));
        if (entity != null) {
            return entity.interact(this);
        }
        return Interaction.NULL;
    }
}
