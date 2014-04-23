package com.arnopaja.supermac.world.objects;

import com.arnopaja.supermac.world.grid.Location;

/**
 * A subclass of Entity that is static and therefore does not update.
 * @author Ari Weiland
 */
public abstract class StaticEntity extends Entity {

    protected StaticEntity(boolean isRendered, Location location, boolean isInteractable) {
        super(isRendered, location, isInteractable);
    }

    @Override
    public final void update(float delta) {
        // Does not update
    }
}
