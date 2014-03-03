package com.arnopaja.supermac.objects;

import com.arnopaja.supermac.grid.Direction;
import com.arnopaja.supermac.grid.Grid;
import com.arnopaja.supermac.helpers.Dialogue;
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
    public void interact(MainMapCharacter character) {
        // TODO: display actual dialogue
        Dialogue.displayDialogue("Hi!");
    }
}
