package com.arnopaja.supermac.world.objects;

import com.arnopaja.supermac.world.grid.Location;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Subclass of StaticEntity that is not rendered.
 * Because it is not rendered, it is also necessarily
 * interactable or its existence would be pointless.
 * @author Ari Weiland
 */
public abstract class NonRenderedEntity extends StaticEntity {

    protected NonRenderedEntity(Location location) {
        super(false, location, true);
    }

    @Override
    public void delay() {
        // there is no point delaying the removal of an entity that is not rendered
    }

    @Override
    public final TextureRegion getSprite(float runTime) {
        return null;
    }
}
