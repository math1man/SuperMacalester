package com.arnopaja.supermac.helpers.dialogue;

import com.arnopaja.supermac.helpers.interaction.Interaction;
import com.arnopaja.supermac.helpers.interaction.Interactions;

import java.util.Arrays;

/**
 * @author Ari Weiland
 */
public class DialogueText extends Dialogue {

    private final String rawDialogue;
    private final DialogueMember member;

    public DialogueText(String rawDialogue, DialogueStyle style) {
        this("", rawDialogue, style);
    }

    public DialogueText(String name, String rawDialogue, DialogueStyle style) {
        this(name, rawDialogue, null, style);
    }

    public DialogueText(String rawDialogue, Interaction interaction, DialogueStyle style) {
        this("", rawDialogue, interaction, style);
    }

    public DialogueText(String name, String rawDialogue, Interaction interaction, DialogueStyle style) {
        this(name, rawDialogue, rawDialogue.split("<d>"), interaction, style);
    }

    protected DialogueText(String name, String rawDialogue, String[] text, Interaction interaction, DialogueStyle style) {
        super(name, style);
        this.rawDialogue = rawDialogue;
        if (text.length > 1) {
            member = new DialogueMember(text[0], new DialogueText(name, rawDialogue,
                    Arrays.copyOfRange(text, 1, text.length), interaction, style));
        } else if (interaction == null) {
            member = new DialogueMember(text[0], Interactions.END_DIALOGUE);
        } else {
            member = new DialogueMember(text[0], interaction);
        }
    }

    public DialogueMember getMember() {
        return member;
    }

    @Override
    public String getText() {
        return member.getText();
    }

    @Override
    public String getRaw() {
        return rawDialogue;
    }

    public boolean hasInteraction() {
        return member.hasInteraction();
    }

    public Interaction getInteraction() {
        return member.getInteraction();
    }

    @Override
    public String toString() {
        return getText();
    }
}