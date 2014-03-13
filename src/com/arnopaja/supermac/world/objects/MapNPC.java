package com.arnopaja.supermac.world.objects;

import com.arnopaja.supermac.world.grid.Direction;
import com.arnopaja.supermac.world.grid.Grid;
import com.arnopaja.supermac.helpers.Interaction;
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
            float random = RANDOM.nextFloat() * SECONDS_BETWEEN_RANDOM_MOVES / delta;
            if (random < 1) {
                int ordinal = RANDOM.nextInt(4);
                move(Direction.values()[ordinal]);
            }
        }
    }

    @Override
    public Interaction getInteraction(MainMapCharacter character) {
        if (isInteractable()) {
            setFacing(Direction.getDirectionToward(getPosition(), character.getPosition()));
            return Interaction.dialogue(getDialogue());
        }
        return Interaction.getNull();
    }

    public boolean canMove() {
        return canMove;
    }

    public void setCanMove(boolean canMove) {
        this.canMove = canMove;
    }
}
