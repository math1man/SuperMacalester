package com.arnopaja.supermac.plot;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Ari Weiland
 */
public class Quest {

    public static enum QuestState { INACTIVE, ACTIVE, COMPLETE }

    public final int ID;
    private final List<Goal> goals;

    private Set<Quest> prereqs  = new HashSet<Quest>();
    private Set<Quest> postreqs = new HashSet<Quest>();

    private QuestState state = QuestState.INACTIVE;
    private int currentGoal;

    public Quest(int questID, List<Goal> goals) {
        this.ID = questID;
        this.goals = goals;
        for (Goal goal : goals) {
            goal.setQuest(this);
        }
    }

    public void activate(Quest quest) {
        System.out.println("Quest Activated!");
        if (isInactive()) {
            prereqs.remove(quest);
            if (prereqs.isEmpty()) {
                clearGoals();
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
        System.out.println("Quest Completed!");
        state = QuestState.COMPLETE;
        for (Quest quest : postreqs) {
            quest.activate(this);
        }
    }

    public void load(int currentGoal) {
        clearGoals();
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

    private void clearGoals() {
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
}
