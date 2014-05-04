package com.arnopaja.supermac.plot;

import com.arnopaja.supermac.helpers.Interaction;
import com.arnopaja.supermac.helpers.load.SuperParser;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.List;

/**
 * A goal is a single plot interaction.
 *
 * It has an questNpc, an interaction, and a location.  On activation,
 * the questNpc is given the interaction and placed at the location.
 * On completion, the questNpc will be removed from the grid (or
 * something like that, we need to figure that out). Also, any goal
 * that results from the completion of this goal will be activated.
 *
 * An questNpc can be used my multiple goals, so the interaction must
 * be given on activation, and we will need to design a system or
 * just structure the plot so that two goals with the same questNpc
 * are never active at the same time.
 *
 * @author Ari Weiland
 */
public class Goal {

    private final List<QuestEntity> questEntities;
    private final Interaction mainInteraction;
    private final boolean delay;
    private Interaction netInteraction = Interaction.NULL;
    private boolean isActive = false;

    public Goal(List<QuestEntity> questEntities, Interaction mainInteraction, boolean delay) {
        this.questEntities = questEntities;
        this.mainInteraction = mainInteraction;
        this.delay = delay;
    }

    public void activate() {
        for (QuestEntity entity : questEntities) {
            entity.activate(netInteraction);
        }
        isActive = true;
    }

    public void deactivate() {
        for (QuestEntity entity : questEntities) {
            entity.deactivate(delay);
        }
        isActive = false;
    }

    protected void setQuest(Quest quest) {
        netInteraction = mainInteraction.attach(quest);
    }

    public List<QuestEntity> getQuestEntities() {
        return questEntities;
    }

    public Interaction getMainInteraction() {
        return mainInteraction;
    }

    public boolean isActive() {
        return isActive;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Goal)) return false;

        Goal goal = (Goal) o;

        return questEntities.equals(goal.questEntities) && mainInteraction.equals(goal.mainInteraction);
    }

    public static class Parser extends SuperParser<Goal> {
        @Override
        public Goal fromJson(JsonElement element) {
            JsonObject object = element.getAsJsonObject();
            List<QuestEntity> entities = getList(object, "entities", QuestEntity.class);
            Interaction interaction = getObject(object, Interaction.class);
            boolean delay = true;
            if (object.has("delay")) {
                delay = getBoolean(object, "delay");
            }
            return new Goal(entities, interaction, delay);
        }

        @Override
        public JsonElement toJson(Goal object) {
            JsonObject json = new JsonObject();
            addList(json, "entities", object.getQuestEntities(), QuestEntity.class);
            addObject(json, object.getMainInteraction(), Interaction.class);
            addBoolean(json, "delay", object.delay);
            return json;
        }
    }
}
