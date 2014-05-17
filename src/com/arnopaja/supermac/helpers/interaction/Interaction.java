package com.arnopaja.supermac.helpers.interaction;

import com.arnopaja.supermac.GameScreen;
import com.arnopaja.supermac.helpers.load.Parsable;
import com.arnopaja.supermac.helpers.load.SuperParser;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.Map;

/**
 * This class dictates how an interaction proceeds via its subrun method.
 * It also provides various static utility methods.
 *
 * @author Ari Weiland
 */
public interface Interaction extends Parsable {

    void run(GameScreen screen);

    public static class Parser extends SuperParser<Interaction> {
        @Override
        public Interaction fromJson(JsonElement element) {
            JsonObject object = element.getAsJsonObject();
            MultiInteraction interaction = new MultiInteraction();
            for (Map.Entry<String, JsonElement> entry : object.entrySet()) {
                if (!entry.getKey().equals("class")) {
                    if (entry.getValue().isJsonObject()) {
                        interaction.attach(fromJson(entry.getValue(), Interaction.class));
                    } else {
                        interaction.attach(fromJson(entry.getValue(), Interactions.class));
                    }
                }
            }
            return interaction;
        }

        @Override
        public JsonElement toJson(Interaction object) {
            JsonObject json = new JsonObject();
            if (object instanceof MultiInteraction) {
                int i=0;
                for (Interaction interaction : ((MultiInteraction) object).getInteractions()) {
                    addObject(json, "element_" + i, interaction, Interaction.class);
                    i++;
                }
            } else if (object instanceof Interactions) {
                addObject(json, "element", (Interactions) object, Interactions.class);
            } else {
                addObject(json, "element", object, Interaction.class);
            }
            return json;
        }
    }
}
