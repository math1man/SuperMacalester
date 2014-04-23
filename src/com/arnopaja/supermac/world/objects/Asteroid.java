package com.arnopaja.supermac.world.objects;

import com.arnopaja.supermac.helpers.AssetLoader;
import com.arnopaja.supermac.helpers.Interaction;
import com.arnopaja.supermac.helpers.dialogue.DialogueText;
import com.arnopaja.supermac.world.grid.Location;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * @author Ari Weiland
 */
public class Asteroid extends StaticEntity {

    public Asteroid(Location location) {
        super(true, location, true);
    }

    @Override
    public Interaction toInteraction() {
        return new DialogueText("This asteriod seems to be pulsing with a strange energy").toInteraction();
    }

    @Override
    public TextureRegion getSprite(float runTime) {
        return AssetLoader.asteroid.getKeyFrame(runTime);
    }

    public static class Parser extends Entity.Parser<Asteroid> {
        @Override
        public Asteroid fromJson(JsonElement element) {
            JsonObject object = element.getAsJsonObject();
            Location location = getObject(object, Location.class);
            return new Asteroid(location);
        }

        @Override
        public JsonElement toJson(Asteroid object) {
            return toBaseJson(object);
        }
    }
}
