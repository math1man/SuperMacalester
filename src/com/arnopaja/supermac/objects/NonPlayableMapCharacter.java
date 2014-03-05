package com.arnopaja.supermac.objects;

import com.arnopaja.supermac.grid.Direction;
import com.arnopaja.supermac.grid.Grid;
import com.badlogic.gdx.math.Vector2;

/**
 * @author Ari Weiland
 */
public class NonPlayableMapCharacter extends MapCharacter {

    public static final float SECONDS_PER_RANDOM_MOVE = 3;

    public NonPlayableMapCharacter(Grid grid, Vector2 position, Direction facing) {
        this(grid, position, facing, false);
    }

    public NonPlayableMapCharacter(Grid grid, Vector2 position, Direction facing, boolean isInteractable) {
        super(grid, position, facing, isInteractable);
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        if (!isMoving()) {
            double random = Math.random() * SECONDS_PER_RANDOM_MOVE / delta;
            if (random < 1) {
                int ordinal = (int) (Math.random() * 4);
                move(Direction.values()[ordinal]);
            }
        }
    }

    @Override
    public Interaction getInteraction(MainMapCharacter character) {
        if (isInteractable()) {
            setFacing(Direction.getDirectionToward(getPosition(), character.getPosition()));
            return Interaction.getDialogueInteraction(this, character, getDialogue());
        }
        return Interaction.getNullInteraction();
    }
}
