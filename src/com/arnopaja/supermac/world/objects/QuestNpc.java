package com.arnopaja.supermac.world.objects;

import com.arnopaja.supermac.helpers.Interaction;
import com.arnopaja.supermac.plot.QuestEntity;
import com.arnopaja.supermac.world.grid.Direction;

/**
 * @author Ari Weiland
 */
public class QuestNpc extends MapNpc implements QuestEntity {

    public QuestNpc(String name, Direction direction) {
        super(name, null, direction, true, Interaction.NULL);
    }

    @Override
    public void setInteraction(Interaction interaction) {
        this.interaction = interaction;
    }

    // TODO: make me a parser
}
