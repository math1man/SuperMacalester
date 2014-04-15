package com.arnopaja.supermac.plot;

import com.arnopaja.supermac.helpers.SuperParser;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.*;

/**
 * @author Ari Weiland
 */
public class Quest {

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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Quest)) return false;

        Quest quest = (Quest) o;

        return id == quest.id;

    }

    /**
     * Parses quests from a JSON file.  The file must be formatted in a
     * very specific way.  It must contain two primary members: an array
     * labeled dependencies and an object called quests. Each object in
     * the dependencies array has 2 members, the ID of a quest and an
     * array of IDs of quests that the initial quest unlocks. The quests
     * object contains quests, which are identified by a unique name. A
     * quest contains its unique ID labeled id and an array of goals.
     * Each goal is composed of an Entity, a Location, and an Interaction,
     * which are parsed based on their respective parsers. In the following
     * example, all names must be exactly as show except for the quest name
     * (myQuest). All values can be set appropriately.
     *
     * Example Quest JSON:
     * {
     *     "dependencies" : [
     *         {
     *             "pre" : 0,
     *             "posts" : [
     *                 1,
     *                 2
     *             ]
     *         }
     *     ],
     *     "quests" : {
     *         "myQuest" : {
     *             "id" : 0,
     *             "goals" : [
     *                 {
     *                     "entity" : { [entity JSON object] }
     *                     "location" : { [location JSON object] }
     *                     "interaction" : { [interaction JSON object] }
     *                 }
     *             ]
     *         }
     *     }
     * }
     */
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

        @Override
        public JsonElement getJsonHead(String json) {
            return super.getJsonHead(json).getAsJsonObject().get("quests");
        }

        public JsonArray getDependenciesJsonHead(String json)  {
            return super.getJsonHead(json).getAsJsonObject().getAsJsonArray("dependencies");
        }

        public Map<Integer, Quest> parseMap(String json) {
            List<Quest> quests = parseAll(json);
            Map<Integer, Quest> questMap = new HashMap<Integer, Quest>();
            for (Quest quest : quests) {
                questMap.put(quest.getId(), quest);
            }
            JsonArray array = getDependenciesJsonHead(json);
            addDependencies(questMap, dependenciesFromJson(array));
            return questMap;
        }

        public void addDependencies(Map<Integer, Quest> quests, Map<Integer, Integer[]> dependencies) {
            for (int preId : dependencies.keySet()) {
                Quest pre = quests.get(preId);
                Integer[] postIds = dependencies.get(preId);
                Quest[] posts = new Quest[postIds.length];
                for (int i=0; i<postIds.length; i++) {
                    Quest post = quests.get(postIds[i]);
                    if (!pre.isComplete()) {
                        post.addPrereqs(pre);
                    }
                    posts[i] = post;
                }
                pre.addPostreqs(posts);
            }
        }

        private Map<Integer, Integer[]> dependenciesFromJson(JsonArray array) {
            Map<Integer, Integer[]> map = new HashMap<Integer, Integer[]>();
            for (JsonElement e : array) {
                JsonObject object = e.getAsJsonObject();
                int pre = getInt(object, "pre");
                JsonArray a = object.getAsJsonArray("posts");
                Integer[] posts = new Integer[a.size()];
                for (int i=0; i<a.size(); i++) {
                    posts[i] = a.get(i).getAsInt();
                }
                map.put(pre, posts);
            }
            return map;
        }
    }
}
