package com.arnopaja.supermac.helpers.dialogue;

import com.arnopaja.supermac.helpers.Interaction;
import com.arnopaja.supermac.helpers.InteractionBuilder;

import java.util.Arrays;

/**
 * @author Ari Weiland
 */
public class DialogueText extends Dialogue {

    private final DialogueMember member;

    public DialogueText(String rawDialogue) {
        this(rawDialogue, CLEAR_DIALOGUE);
    }

    public DialogueText(String... text) {
        this(CLEAR_DIALOGUE, text);
    }

    public DialogueText(String rawDialogue, InteractionBuilder builder) {
        this(builder, rawDialogue.split("<d>"));
    }

    public DialogueText(InteractionBuilder builder, String... text) {
        if (text.length > 1) {
            member = new DialogueMember(text[0],
                    new DialogueText(builder, Arrays.copyOfRange(text, 1, text.length)).toInteraction());
        } else if (builder == null) {
            member = new DialogueMember(text[0], CLEAR_DIALOGUE);
        } else {
            member = new DialogueMember(text[0], builder.toInteraction());
        }
    }

    public DialogueMember getMember() {
        return member;
    }

    public String getText() {
        return member.getText();
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