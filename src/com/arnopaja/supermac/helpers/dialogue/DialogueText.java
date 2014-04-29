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

    public DialogueText(String rawDialogue, DialogueStyle style) {
        this(rawDialogue, CLEAR_DIALOGUE, style);
    }

    public DialogueText(String rawDialogue, InteractionBuilder builder, DialogueStyle style) {
        this(rawDialogue, rawDialogue.split("<d>"), builder, style);
    }

    protected DialogueText(String rawDialogue, String[] text, InteractionBuilder builder, DialogueStyle style) {
        super(style);
        this.rawDialogue = rawDialogue;
        if (text.length > 1) {
            member = new DialogueMember(text[0], new DialogueText(rawDialogue,
                    Arrays.copyOfRange(text, 1, text.length), builder, style).toInteraction());
        } else if (builder == null) {
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
        return getRaw();
    }
}