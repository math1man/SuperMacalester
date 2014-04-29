package com.arnopaja.supermac.world.objects;

import com.arnopaja.supermac.world.grid.Location;

/**
 * @author Ari Weiland
 */
public interface EntityInterface {
    void update(float delta);
    void removeFromGrid();
    void removeFromGrid(boolean delay);
    void changeGrid(Location destination);
    void changeGrid(Location destination, boolean delay);
    void forceChangeGrid(Location destination);
    void forceChangeGrid();
    void delay();
    boolean isDelayed();
    boolean isInGrid();
    Location getLocation();
    boolean isInteractable();
    void setInteractable(boolean interactable);
}
