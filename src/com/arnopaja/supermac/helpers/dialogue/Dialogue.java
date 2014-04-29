package com.arnopaja.supermac.helpers.dialogue;

import com.arnopaja.supermac.GameScreen;
import com.arnopaja.supermac.helpers.Interaction;
import com.arnopaja.supermac.helpers.InteractionBuilder;
import com.arnopaja.supermac.helpers.SuperParser;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

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
    };

    private DialogueStyle style;

    protected Dialogue(DialogueStyle style) {
        this.style = style;
    }

    public abstract String getText();

    public abstract String getRaw();

    public DialogueStyle getStyle() {
        return style;
    }

    public void setStyle(DialogueStyle style) {
        this.style = style;
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
            if (object.has("text")) {
                String dialogue = getString(object, "text");
                Interaction interaction = CLEAR_DIALOGUE;
                if (has(object, Interaction.class)) {
                    interaction = getObject(object, Interaction.class);
                }
                return new DialogueText(dialogue, interaction, DialogueStyle.WORLD);
            } else if (object.has("options")) {
                JsonArray array = object.getAsJsonArray("options");
                DialogueMember[] members = new DialogueMember[array.size() + 1];
                members[0] = new DialogueMember(getString(object, "header"));
                for (int i=0; i<array.size(); i++) {
                    JsonObject member = array.get(i).getAsJsonObject();
                    Interaction interaction = Dialogue.CLEAR_DIALOGUE;
                    if (has(member, Interaction.class)) {
                        interaction = getObject(member, Interaction.class);
                    }
                    members[i+1] = new DialogueMember(getString(member, "text"), interaction);
                }
                return new DialogueOptions(members, DialogueStyle.WORLD);
            }
            return null;
        }

        @Override
        public JsonElement toJson(Dialogue object) {
            if (object instanceof DialogueText) {
                JsonObject json = new JsonObject();
                DialogueText text = (DialogueText) object;
                addString(json, "text", text.getText());
                if (text.hasInteraction()) {
                    addObject(json, text.getInteraction(), Interaction.class);
                }
                return json;
            } else {
                JsonObject json = new JsonObject();
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
                return json;
            }
        }
    }
}
