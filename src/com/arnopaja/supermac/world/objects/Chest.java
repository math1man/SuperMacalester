package com.arnopaja.supermac.world.objects;

import com.arnopaja.supermac.world.grid.Location;

/**
 * @author Ari Weiland
 */
public class Chest extends Entity {
    public Chest(boolean isRendered, Location location, boolean isInteractable) {
        super(isRendered, location, isInteractable);
    }

    @Override
    public void update(float delta) {
        // does nothing
    }
}
