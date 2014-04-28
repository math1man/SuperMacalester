package com.arnopaja.supermac.world.objects;

import com.arnopaja.supermac.helpers.Interaction;
import com.arnopaja.supermac.plot.QuestEntity;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

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

    public static class Parser extends Entity.Parser<NonRenderedQuestEntity> {
        @Override
        public NonRenderedQuestEntity fromJson(JsonElement element) {
            return new NonRenderedQuestEntity();
        }

        @Override
        public JsonElement toJson(NonRenderedQuestEntity object) {
            JsonObject json = new JsonObject();
            addClass(json, NonRenderedQuestEntity.class);
            return json;
        }
    }
}
