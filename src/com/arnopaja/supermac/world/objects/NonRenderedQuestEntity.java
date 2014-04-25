package com.arnopaja.supermac.world.objects;

import com.arnopaja.supermac.helpers.Interaction;
import com.arnopaja.supermac.plot.QuestEntity;

/**
 * @author Ari Weiland
 */
public class NonRenderedQuestEntity extends NonRenderedEntity implements QuestEntity {

    private Interaction interaction = Interaction.NULL;

    public NonRenderedQuestEntity() {
        super(null);
    }

    @Override
    public void setInteraction(Interaction interaction) {
        this.interaction = interaction;
    }

    @Override
    public Interaction toInteraction() {
        return interaction;
    }

    // TODO: make me a parser
}
