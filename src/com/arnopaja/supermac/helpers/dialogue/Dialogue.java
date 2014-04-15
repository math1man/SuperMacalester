package com.arnopaja.supermac.helpers.dialogue;

import com.arnopaja.supermac.helpers.Interaction;
import com.arnopaja.supermac.helpers.SuperParser;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

/**
 * Superclass for DialogueText and DialogueOptions
 *
 * @author Ari Weiland
 */
public abstract class Dialogue {

    public static class Parser extends SuperParser<Dialogue> {
        @Override
        public Dialogue fromJson(JsonElement element) {
            JsonObject object = element.getAsJsonObject();
            boolean hasText = object.has("text");
            boolean hasOptions = object.has("options");
            if (hasText) {
                String dialogue = object.getAsJsonPrimitive("text").getAsString();
                if (hasOptions) {
                    DialogueOptions options = optionsFromJson(object.getAsJsonObject("options"));
                    return new DialogueText(dialogue, options);
                } else if (object.has("interaction")) {
                    Interaction interaction = fromJson(object.get("interaction"), Interaction.class);
                    return new DialogueText(dialogue, interaction);
                }
            } else if (hasOptions) {
                return optionsFromJson(object.getAsJsonObject("options"));
            }
            return null;
        }

        @Override
        public JsonElement toJson(Dialogue object) {
            JsonObject json = new JsonObject();
            if (object instanceof DialogueText) {
                DialogueText text = (DialogueText) object;
                json.addProperty("text", text.getRaw());
                if (text.hasOptions()) {
                    json.add("options", optionsToJson(text.getOptions()));
                } else if (text.hasPostInteraction()) {
                    json.add("interaction", toJson(text.getPostInteraction(), Interaction.class));
                }
            } else {
                json.add("options", optionsToJson((DialogueOptions) object));
            }
            return json;
        }

        public DialogueOptions optionsFromJson(JsonObject object) {
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
                    interactions[i] = fromJson(interactionsJson.get(i).getAsJsonObject(), Interaction.class);
                }
            }
            return new DialogueOptions(header, options, interactions);
        }

        public JsonObject optionsToJson(DialogueOptions options) {
            JsonObject json = new JsonObject();
            json.addProperty("header", options.getHeader());
            JsonArray optionsJson = new JsonArray();
            JsonArray interactionsJson = new JsonArray();
            for (int i=0; i<options.getCount(); i++) {
                optionsJson.add(new JsonPrimitive(options.getOption(i)));
                interactionsJson.add(toJson(options.getInteraction(i), Interaction.class));
            }
            json.add("options", optionsJson);
            json.add("interactions", interactionsJson);
            return json;
        }
    }
}
