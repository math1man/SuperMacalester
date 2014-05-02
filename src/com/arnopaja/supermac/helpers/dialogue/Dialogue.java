package com.arnopaja.supermac.helpers.dialogue;

import com.arnopaja.supermac.GameScreen;
import com.arnopaja.supermac.helpers.AssetLoader;
import com.arnopaja.supermac.helpers.Interaction;
import com.arnopaja.supermac.helpers.InteractionBuilder;
import com.arnopaja.supermac.helpers.SuperParser;
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
public abstract class Dialogue implements InteractionBuilder {

    public static final Interaction CLEAR_DIALOGUE = new Interaction() {
        @Override
        public void run(GameScreen screen) {
            screen.endDialogue();
        }

        @Override
        public String toString() {
            return "CLEAR_DIALOGUE";
        }
    };

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
    public Interaction toInteraction() {
        final Dialogue dialogue = this;
        return new Interaction(dialogue) {
            @Override
            public void run(GameScreen screen) {
                screen.getDialogueHandler().display(dialogue);
                screen.dialogue();
            }
        };
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
                Interaction interaction = CLEAR_DIALOGUE;
                if (has(object, Interaction.class)) {
                    interaction = getObject(object, Interaction.class);
                }
                return new DialogueText(dialogue, interaction, DialogueStyle.WORLD);
            } else if (object.has("options")) {
                JsonArray array = object.getAsJsonArray("options");
                List<DialogueMember> members = new ArrayList<DialogueMember>(array.size() + 1);
                members.add(new DialogueMember(getString(object, "header")));
                for (int i=0; i<array.size(); i++) {
                    JsonObject member = array.get(i).getAsJsonObject();
                    Interaction interaction = Dialogue.CLEAR_DIALOGUE;
                    if (has(member, Interaction.class)) {
                        interaction = getObject(member, Interaction.class);
                    }
                    members.add(new DialogueMember(getString(member, "text"), interaction));
                }
                return new DialogueOptions(name, members, DialogueStyle.WORLD);
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
