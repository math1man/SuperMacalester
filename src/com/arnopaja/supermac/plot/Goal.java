package com.arnopaja.supermac.plot;

import com.arnopaja.supermac.helpers.load.Parsable;
import com.arnopaja.supermac.helpers.load.SuperParser;
import com.arnopaja.supermac.world.objects.QuestNpc;
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
public class Goal implements Parsable {

    private final List<QuestNpc> questEntities;
    private Quest quest;
    private boolean isActive = false;

    public Goal(List<QuestNpc> questEntities) {
        this.questEntities = questEntities;
    }

    public void activate() {
        for (QuestNpc entity : questEntities) {
            entity.activate(quest);
        }
        isActive = true;
    }

    public void deactivate() {
        for (QuestNpc entity : questEntities) {
            entity.deactivate();
        }
        isActive = false;
    }

    protected void setQuest(Quest quest) {
        this.quest = quest;
    }

    public List<QuestNpc> getQuestEntities() {
        return questEntities;
    }

    public boolean isActive() {
        return isActive;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Goal)) return false;

        Goal goal = (Goal) o;

        return questEntities.equals(goal.questEntities);
    }

    public static class Parser extends SuperParser<Goal> {
        @Override
        public Goal fromJson(JsonElement element) {
            JsonObject object = element.getAsJsonObject();
            List<QuestNpc> entities = getList(object, "entities", QuestNpc.class);
            return new Goal(entities);
        }

        @Override
        public JsonElement toJson(Goal object) {
            JsonObject json = new JsonObject();
            addList(json, "entities", object.getQuestEntities(), QuestNpc.class);
            return json;
        }
    }
}
