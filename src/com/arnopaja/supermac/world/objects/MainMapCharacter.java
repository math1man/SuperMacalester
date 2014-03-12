package com.arnopaja.supermac.world.objects;

import com.arnopaja.supermac.world.grid.Direction;
import com.arnopaja.supermac.world.grid.Grid;
import com.arnopaja.supermac.helpers.AssetLoader;
import com.arnopaja.supermac.helpers.Interaction;
import com.badlogic.gdx.math.Vector2;

/**
 * @author Ari Weiland
 */
public class MainMapCharacter extends MapCharacter {

    public MainMapCharacter(Grid grid, Vector2 position, Direction facing) {
        super(grid, position, facing);
        setFacingSprites(AssetLoader.steven);
        setFacingAnimations(AssetLoader.stevenStepping);
    }

    public Interaction interact() {
        Entity entity = getGrid().getEntity(Direction.getAdjacent(getPosition(), getFacing()));
        if (entity != null) {
            return entity.getInteraction(this);
        }
        return Interaction.getNull();
    }

    @Override
    public Interaction getInteraction(MainMapCharacter character) {
        // insert masturbation joke here
        return Interaction.getNull();
    }

    @Override
    public String toString() {
        return "MainMapCharacter{" +
                "grid=" + getGrid() +
                ", position=" + getPosition() +
                ", facing=" + getFacing() +
                ", isRendered=" + isRendered() +
                '}';
    }
}
