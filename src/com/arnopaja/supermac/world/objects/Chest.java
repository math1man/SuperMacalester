package com.arnopaja.supermac.world.objects;

import com.arnopaja.supermac.helpers.Interaction;
import com.arnopaja.supermac.inventory.AbstractItem;
import com.arnopaja.supermac.world.grid.Location;

import java.util.List;

/**
 * @author Ari Weiland
 */
public class Chest extends Entity {

    private int currency; // TODO: should chests contain money?
    private List<AbstractItem> contents;

    public Chest(boolean isRendered, Location location, boolean isInteractable) {
        super(isRendered, location, isInteractable);
        setInteraction(Interaction.chest());
    }

    @Override
    public void update(float delta) {
        // does nothing
    }
}
