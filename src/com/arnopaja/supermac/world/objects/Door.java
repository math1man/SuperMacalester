package com.arnopaja.supermac.world.objects;

import com.arnopaja.supermac.GameScreen;
import com.arnopaja.supermac.helpers.Interaction;
import com.arnopaja.supermac.world.grid.Location;

/**
 * @author Ari Weiland
 */
public class Door extends Entity {

    private final Location destination;

    public Door(Location location, Location destination) {
        super(false, location, true);
        this.destination = destination;
    }

    @Override
    public void update(float delta) {
        // nothing happens
    }

    @Override
    public Interaction interact(final MainMapCharacter character) {
        setInteraction(new Interaction() {
            @Override
            public void run(GameScreen screen) {
                character.changeGrid(destination);
            }
        });
        return super.interact(character);
    }

    public Location getDestination() {
        return destination;
    }
}
