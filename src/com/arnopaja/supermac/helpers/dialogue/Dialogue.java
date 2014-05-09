package com.arnopaja.supermac.helpers.dialogue;

import com.arnopaja.supermac.GameScreen;
import com.arnopaja.supermac.helpers.interaction.Interaction;
import com.arnopaja.supermac.helpers.interaction.Interactions;
import com.arnopaja.supermac.helpers.load.AssetLoader;
import com.arnopaja.supermac.helpers.load.SuperParser;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Superclass for DialogueText and DialogueOptions
 *
 * @author Ari Weiland
 */
public abstract class Dialogue implements Interaction {

    private final String name;
    private final DialogueStyle style;

    protected Dialogue(String name, DialogueStyle style) {
        this.name = name;
        this.style = style;
    }

    public String getName() {
        return name;
    }

    public abstract String getText();

    public abstract String getRaw();

    public DialogueStyle getStyle() {
        return style;
    }

    @Override
    public void run(GameScreen screen) {
        screen.getDialogueHandler().display(this);
        screen.dialogue();
    }

    public static class Parser extends SuperParser<Dialogue> {
        @Override
        public Dialogue fromJson(JsonElement element) {
            JsonObject object = element.getAsJsonObject();
            String name = null;
            if (object.has("name")) {
                name = getString(object, "name");
            }
            Dialogue cached = AssetLoader.dialogues.get(name);
            if (cached != null) {
                return cached;
            }
            if (object.has("text")) {
                String dialogue = getString(object, "text");
                Interaction interaction = getObject(object, Interaction.class, Interactions.END_DIALOGUE);
                return new DialogueText(name, dialogue, interaction, DialogueStyle.WORLD);
            } else if (object.has("options")) {
                JsonArray array = object.getAsJsonArray("options");
                String header = getString(object, "header");
                List<DialogueMember> members = new ArrayList<DialogueMember>(array.size() + 1);
                for (int i=0; i<array.size(); i++) {
                    JsonObject member = array.get(i).getAsJsonObject();
                    Interaction interaction = getObject(member, Interaction.class, Interactions.END_DIALOGUE);
                    members.add(new DialogueMember(getString(member, "text"), interaction));
                }
                return new DialogueOptions(name, header, members, DialogueStyle.WORLD);
            }
            return null;
        }

        @Override
        public JsonElement toJson(Dialogue object) {
            JsonObject json = new JsonObject();
            addString(json, "name", object.getName());
            if (object instanceof DialogueText) {
                DialogueText text = (DialogueText) object;
                addString(json, "text", text.getText());
                if (text.hasInteraction()) {
                    addObject(json, text.getInteraction(), Interaction.class);
                }
            } else {
                DialogueOptions options = (DialogueOptions) object;
                addString(json, "header", options.getHeader());
                JsonArray array = new JsonArray();
                for (int i=0; i<options.getCount(); i++) {
                    DialogueMember option = options.getOption(i);
                    JsonObject o = new JsonObject();
                    addString(o, "text", option.getText());
                    addObject(o, option.getInteraction(), Interaction.class);
                    array.add(o);
                }
                json.add("options", array);
            }
            return json;
        }
    }
}
