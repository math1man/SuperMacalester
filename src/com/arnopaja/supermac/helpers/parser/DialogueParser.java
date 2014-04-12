package com.arnopaja.supermac.helpers.parser;

import com.arnopaja.supermac.helpers.Interaction;
import com.arnopaja.supermac.helpers.dialogue.Dialogue;
import com.arnopaja.supermac.helpers.dialogue.DialogueDisplayable;
import com.arnopaja.supermac.helpers.dialogue.DialogueOptions;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

/**
 * @author Ari Weiland
 */
public class DialogueParser extends Parser<DialogueDisplayable> {

    private InteractionParser parser = new InteractionParser();

    @Override
    public DialogueDisplayable parse(JsonObject object) {
        boolean hasDialogue = object.has("dialogue");
        boolean hasOptions = object.has("options");
        if (hasDialogue) {
            String dialogue = object.getAsJsonPrimitive("dialogue").getAsString();
            if (hasOptions) {
                DialogueOptions options = parseOptions(object.getAsJsonObject("options"));
                return new Dialogue(dialogue, options);
            } else if (object.has("interaction")) {
                Interaction interaction = parser.parse(object.getAsJsonObject("interaction"));
                return new Dialogue(dialogue, interaction);
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
                interactions[i] = parser.parse(interactionsJson.get(i).getAsJsonObject());
            }
        }
        return new DialogueOptions(header, options, interactions);
    }
}
