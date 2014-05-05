package com.arnopaja.supermac.helpers.dialogue;

import com.arnopaja.supermac.helpers.interaction.Interaction;
import com.arnopaja.supermac.helpers.interaction.Interactions;
import com.arnopaja.supermac.helpers.interaction.MultiInteraction;

/**
 * @author Ari Weiland
 */
public class DialogueMember {
    private final String text;
    private final Interaction interaction;

    public DialogueMember(String text) {
        this(text, Interactions.NULL);
    }

    public DialogueMember(String text, Interaction interaction) {
        this.text = text;
        if (interaction != null && interaction instanceof Dialogue || interaction == Interactions.NULL) {
            this.interaction = interaction;
        } else {
            this.interaction = new MultiInteraction(Interactions.END_DIALOGUE, interaction);
        }
    }

    public boolean hasInteraction() {
        return interaction != Interactions.NULL;
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
