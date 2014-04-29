package com.arnopaja.supermac.world.objects;

import com.arnopaja.supermac.helpers.Interaction;
import com.arnopaja.supermac.helpers.SuperParser;
import com.arnopaja.supermac.helpers.dialogue.DialogueStyle;
import com.arnopaja.supermac.helpers.dialogue.DialogueText;
import com.arnopaja.supermac.world.grid.Location;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * @author Ari Weiland
 */
public class Asteroid extends NonRenderedEntity {

    public Asteroid(Location location) {
        super(location);
    }

    @Override
    public Interaction toInteraction() {
        return new DialogueText("This asteroid seems to be pulsing with a strange energy", DialogueStyle.WORLD).toInteraction();
    }

    public static class Parser extends SuperParser<Asteroid> {
        @Override
        public Asteroid fromJson(JsonElement element) {
            JsonObject object = element.getAsJsonObject();
            Location location = getObject(object, Location.class);
            return new Asteroid(location);
        }

        @Override
        public JsonElement toJson(Asteroid object) {
            JsonObject json = new JsonObject();
            addObject(json, object.getLocation(), Location.class);
            return json;
        }
    }
}
