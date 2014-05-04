package com.arnopaja.supermac.helpers.dialogue;

import com.arnopaja.supermac.helpers.Interaction;
import com.arnopaja.supermac.helpers.InteractionBuilder;

import java.util.Arrays;

/**
 * @author Ari Weiland
 */
public class DialogueText extends Dialogue {

    private final String rawDialogue;
    private final DialogueMember member;

    public DialogueText(Dialogue dialogue, DialogueStyle style) {
        this(dialogue.getName(), dialogue.getRaw(), style);
    }

    public DialogueText(String rawDialogue, DialogueStyle style) {
        this("", rawDialogue, style);
    }

    public DialogueText(String name, String rawDialogue, DialogueStyle style) {
        this(name, rawDialogue, null, style);
    }

    public DialogueText(String rawDialogue, InteractionBuilder builder, DialogueStyle style) {
        this("", rawDialogue, builder, style);
    }

    public DialogueText(String name, String rawDialogue, InteractionBuilder builder, DialogueStyle style) {
        this(name, rawDialogue, rawDialogue.split("<d>"), builder, style);
    }

    protected DialogueText(String name, String rawDialogue, String[] text, InteractionBuilder builder, DialogueStyle style) {
        super(name, style);
        this.rawDialogue = rawDialogue;
        if (text.length > 1) {
            member = new DialogueMember(text[0], new DialogueText(name, rawDialogue,
                    Arrays.copyOfRange(text, 1, text.length), builder, style).toInteraction());
        } else if (builder == null || builder == Interaction.NULL) {
            member = new DialogueMember(text[0], CLEAR_DIALOGUE);
        } else {
            member = new DialogueMember(text[0], builder.toInteraction());
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