package com.arnopaja.supermac.plot;

import com.arnopaja.supermac.GameScreen;
import com.arnopaja.supermac.helpers.InteractionBuilder;
import com.arnopaja.supermac.helpers.Interaction;
import com.arnopaja.supermac.helpers.SuperParser;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Ari Weiland
 */
public class Quest implements InteractionBuilder {

    public static enum QuestState { INACTIVE, ACTIVE, COMPLETE }

    private final int id;
    private final List<Goal> goals;

    private Set<Quest> prereqs  = new HashSet<Quest>();
    private Set<Quest> postreqs = new HashSet<Quest>();

    private QuestState state = QuestState.INACTIVE;
    private int currentGoal;

    public Quest(int questId, List<Goal> goals) {
        this.id = questId;
        this.goals = goals;
        for (Goal goal : goals) {
            goal.setQuest(this);
        }
    }

    public void activate(Quest quest) {
        if (isInactive()) {
            prereqs.remove(quest);
            if (prereqs.isEmpty()) {
                reset();
                state = QuestState.ACTIVE;
                currentGoal = 0;
                activateCurrentGoal();
            }
        }
    }

    public void nextGoal() {
        completeCurrentGoal();
        currentGoal++;
        activateCurrentGoal();
    }

    public void complete() {
        state = QuestState.COMPLETE;
        for (Quest quest : postreqs) {
            quest.activate(this);
        }
    }

    public void load(int currentGoal) {
        reset();
        state = QuestState.ACTIVE;
        this.currentGoal = currentGoal;
        activateCurrentGoal();
    }

    private void activateCurrentGoal() {
        if (currentGoal < goals.size()) {
            goals.get(currentGoal).activate();
        } else {
            complete();
        }
    }

    private void completeCurrentGoal() {
        goals.get(currentGoal).deactivate();
    }

    private void reset() {
        for (Goal goal : goals) {
            if (goal.isActive()) {
                goal.deactivate();
            }
        }
    }

    public boolean isInactive() {
        return state == QuestState.INACTIVE;
    }

    public boolean isActive() {
        return state == QuestState.ACTIVE;
    }

    public boolean isComplete() {
        return state == QuestState.COMPLETE;
    }

    public int getCurrentGoal() {
        return currentGoal;
    }

    public Set<Quest> getPrereqs() {
        return prereqs;
    }

    public void addPrereqs(Quest... prereqs) {
        this.prereqs.addAll(Arrays.asList(prereqs));
    }

    public void setPrereqs(Set<Quest> prereqs) {
        this.prereqs = prereqs;
    }

    public Set<Quest> getPostreqs() {
        return postreqs;
    }

    public void addPostreqs(Quest... postreqs) {
        this.postreqs.addAll(Arrays.asList(postreqs));
    }

    public void setPostreqs(Set<Quest> postreqs) {
        this.postreqs = postreqs;
    }

    public int getId() {
        return id;
    }

    @Override
    public Interaction toInteraction() {
        final Quest quest = this;
        return new Interaction(quest) {
            @Override
            public void run(GameScreen screen) {
                quest.nextGoal();
            }
        };
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Quest)) return false;

        Quest quest = (Quest) o;

        return id == quest.id;

    }

    public static class Parser extends SuperParser<Quest> {
        @Override
        public Quest fromJson(JsonElement element) {
            JsonObject object = element.getAsJsonObject();
            int questId = getInt(object, "id");
            List<Goal> goals = getList(object, "goals", Goal.class);
            Quest quest = new Quest(questId, goals);
            if (object.has("currentGoal")) {
                quest.load(getInt(object, "currentGoal"));
            } else if (object.has("complete") && getBoolean(object, "complete")) {
                quest.complete();
            }
            return quest;
        }

        @Override
        public JsonElement toJson(Quest object) {
            JsonObject json = new JsonObject();
            addInt(json, "id", object.getId());
            addList(json, "goals", object.goals, Goal.class);
            if (object.isActive()) {
                addInt(json, "currentGoal", object.currentGoal);
            } else if (object.isComplete()) {
                addBoolean(json, "complete", true);
            }
            return json;
        }
    }
}
