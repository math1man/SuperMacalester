package com.arnopaja.supermac.helpers.dialogue;

import com.arnopaja.supermac.helpers.Interaction;

/**
 * @author Ari Weiland
 */
public class DialogueMember {
    private final String text;
    private final Interaction interaction;

    public DialogueMember(String text) {
        this(text, Interaction.NULL);
    }

    public DialogueMember(String text, Interaction interaction) {
        this.text = text;
        if (interaction != null && interaction.getPrimary() instanceof Dialogue || interaction == Interaction.NULL) {
            this.interaction = interaction;
        } else {
            this.interaction = Dialogue.CLEAR_DIALOGUE.attach(interaction);
        }
    }

    public boolean hasInteraction() {
        return interaction != Interaction.NULL;
    }

    public String getText() {
        return text;
    }

    public Interaction getInteraction() {
        return interaction;
    }

    @Override
    public String toString() {
        return text;
    }
}
