package com.arnopaja.supermac.world.objects;

import com.arnopaja.supermac.helpers.CharacterAsset;
import com.arnopaja.supermac.world.grid.Direction;
import com.arnopaja.supermac.world.grid.Location;

/**
 * @author Ari Weiland
 */
public class MapNpc extends MapCharacter {

    public static final float SECONDS_BETWEEN_RANDOM_MOVES = 4;

    private boolean canMove = true;

    public MapNpc() {
        this(null);
    }

    public MapNpc(Location location) {
        this(location, false);
    }

    public MapNpc(Location location, boolean isInteractable) {
        this(location, isInteractable, true);
    }

    public MapNpc(Location location, boolean isInteractable, boolean canMove) {
        this(location, isInteractable, canMove, null);
    }

    public MapNpc(Location location, boolean isInteractable, boolean canMove,
                  CharacterAsset asset) {
        super(location, isInteractable, asset);
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

    public boolean canMove() {
        return canMove;
    }

    public void setCanMove(boolean canMove) {
        this.canMove = canMove;
    }
}
