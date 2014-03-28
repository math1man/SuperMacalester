package com.arnopaja.supermac.world.objects;

import com.arnopaja.supermac.helpers.Interaction;
import com.arnopaja.supermac.world.grid.Location;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

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
        setInteraction(Interaction.changeGrid(character, destination));
        return super.interact(character);
    }

    public Location getDestination() {
        return destination;
    }

    @Override
    public TextureRegion getSprite(float runTime) {
        return null;
    }
}
