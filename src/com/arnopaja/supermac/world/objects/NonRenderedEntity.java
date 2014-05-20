package com.arnopaja.supermac.world.objects;

import com.arnopaja.supermac.GameScreen;
import com.arnopaja.supermac.helpers.interaction.Interaction;
import com.arnopaja.supermac.helpers.SuperParser;
import com.arnopaja.supermac.world.grid.Location;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * Subclass of StaticEntity that is not rendered.
 * Because it is not rendered, it is also necessarily
 * interactable or its existence would be pointless.
 * @author Ari Weiland
 */
public class NonRenderedEntity extends StaticEntity {

    private final Interaction interaction;

    public NonRenderedEntity(Location location, Interaction interaction) {
        super(false, location, true);
        this.interaction = interaction;
    }


    @Override
    public void run(GameScreen screen) {
        interaction.run(screen);
    }

    @Override
    public final void delay() {
        // there is no point delaying the removal of an entity that is not rendered
    }

    @Override
    public final TextureRegion getSprite(float runTime) {
        return null;
    }

    public static class Parser extends SuperParser<NonRenderedEntity> {
        @Override
        public NonRenderedEntity fromJson(JsonElement element) {
            JsonObject object = element.getAsJsonObject();
            Location location = getObject(object, Location.class);
            Interaction interaction = getObject(object, Interaction.class);
            return new NonRenderedEntity(location, interaction);
        }

        @Override
        public JsonElement toJson(NonRenderedEntity object) {
            JsonObject json = new JsonObject();
            addObject(json, object.getLocation(), Location.class);
            addObject(json, object.interaction, Interaction.class);
            return json;
        }
    }
}
