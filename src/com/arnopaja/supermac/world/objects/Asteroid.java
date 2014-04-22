package com.arnopaja.supermac.world.objects;

import com.arnopaja.supermac.helpers.Interaction;
import com.arnopaja.supermac.world.grid.Location;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * @author Ari Weiland
 */
public class Asteroid extends Entity {

    public Asteroid(Location location) {
        super(true, location, true);
    }

    @Override
    public void update(float delta) {

    }

    @Override
    public Interaction toInteraction() {
        return null;
    }

    @Override
    public TextureRegion getSprite(float runTime) {
        return null;
    }
}
