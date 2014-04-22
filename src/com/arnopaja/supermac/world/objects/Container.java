package com.arnopaja.supermac.world.objects;

import com.arnopaja.supermac.GameScreen;
import com.arnopaja.supermac.helpers.Interaction;
import com.arnopaja.supermac.inventory.GenericItem;
import com.arnopaja.supermac.inventory.Inventory;
import com.arnopaja.supermac.world.grid.Location;

/**
 * @author Ari Weiland
 */
public abstract class Container extends Entity {

    private final Inventory contents;

    protected Container(Location location, Inventory contents) {
        super(true, location, true);
        this.contents = contents;
    }

    @Override
    public void update(float delta) {
        // does nothing
    }

    public void removeItem(GenericItem item) {
        contents.take(item);
    }

    public boolean isEmpty() {
        return contents.isEmpty();
    }

    public Inventory getContents() {
        return contents;
    }

    protected Interaction takeItemInteraction(final GenericItem item) {
        final Container container = this;
        return new Interaction(item, container) {
            @Override
            public void run(GameScreen screen) {
                container.removeItem(item);      // take item from container
                Inventory.getMain().store(item); // place in inventory
            }
        };
    }
}
