package com.arnopaja.supermac.helpers.dialogue;

import com.arnopaja.supermac.helpers.Interaction;
import com.arnopaja.supermac.helpers.SuperParser;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * Superclass for DialogueText and DialogueOptions
 *
 * @author Ari Weiland
 */
public abstract class Dialogue {

    public static class Parser extends SuperParser<Dialogue> {
        @Override
        public Dialogue convert(JsonElement element) {
            JsonObject object = element.getAsJsonObject();
            boolean hasDialogue = object.has("text");
            boolean hasOptions = object.has("options");
            if (hasDialogue) {
                String dialogue = object.getAsJsonPrimitive("text").getAsString();
                if (hasOptions) {
                    DialogueOptions options = parseOptions(object.getAsJsonObject("options"));
                    return new DialogueText(dialogue, options);
                } else if (object.has("interaction")) {
                    Interaction interaction = convert(object.get("interaction"), Interaction.class);
                    return new DialogueText(dialogue, interaction);
                }
            } else if (hasOptions) {
                return parseOptions(object.getAsJsonObject("options"));
            }
            return null;
        }

        public DialogueOptions parseOptions(JsonObject object) {
            String header = object.getAsJsonPrimitive("header").getAsString();
            JsonArray optionsJson = object.getAsJsonArray("options");
            String[] options = new String[optionsJson.size()];
            for (int i=0; i<optionsJson.size(); i++) {
                options[i] = optionsJson.get(i).getAsString();
            }
            Interaction[] interactions = new Interaction[0];
            if (object.has("interactions")) {
                JsonArray interactionsJson = object.getAsJsonArray("interactions");
                interactions = new Interaction[interactionsJson.size()];
                for (int i=0; i<interactionsJson.size(); i++) {
                    interactions[i] = convert(interactionsJson.get(i).getAsJsonObject(), Interaction.class);
                }
            }
            return new DialogueOptions(header, options, interactions);
        }
    }
}
