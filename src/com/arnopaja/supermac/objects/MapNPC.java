package com.arnopaja.supermac.objects;

import com.arnopaja.supermac.grid.Direction;
import com.arnopaja.supermac.grid.Grid;
import com.badlogic.gdx.math.Vector2;

/**
 * @author Ari Weiland
 */
public class MapNPC extends MapCharacter {

    public static final float SECONDS_BETWEEN_RANDOM_MOVES = 4;

    private boolean canMove = true;

    public MapNPC(Grid grid, Vector2 position, Direction facing) {
        this(grid, position, facing, false);
    }

    public MapNPC(Grid grid, Vector2 position, Direction facing, boolean isInteractable) {
        this(grid, position, facing, isInteractable, true);
    }

    public MapNPC(Grid grid, Vector2 position, Direction facing, boolean isInteractable, boolean canMove) {
        super(grid, position, facing, isInteractable);
        this.canMove = canMove;
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        if (!isMoving() && canMove()) {
            double random = Math.random() * SECONDS_BETWEEN_RANDOM_MOVES / delta;
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
            return Interaction.getDialogueInteraction(getDialogue());
        }
        return Interaction.getNullInteraction();
    }

    public boolean canMove() {
        return canMove;
    }

    public void setCanMove(boolean canMove) {
        this.canMove = canMove;
    }
}
