package com.arnopaja.supermac.objects;

import com.arnopaja.supermac.grid.Direction;
import com.arnopaja.supermac.grid.Grid;
import com.badlogic.gdx.math.Vector2;

/**
 * @author Ari Weiland
 */
public class NonPlayableMapCharacter extends MapCharacter {

    public NonPlayableMapCharacter(Grid grid, Vector2 position, Direction facing) {
        this(grid, position, facing, false);
    }

    public NonPlayableMapCharacter(Grid grid, Vector2 position, Direction facing, boolean isInteractable) {
        super(grid, position, facing, isInteractable);
    }

    @Override
    public Interaction getInteraction(MainMapCharacter character) {
        if (isInteractable()) {
            setFacing(Direction.getDirectionToward(getPosition(), character.getPosition()));
            return Interaction.getDialogueInteraction(this, character, "Hi!");
        }
        return Interaction.getNullInteraction();
    }
}
