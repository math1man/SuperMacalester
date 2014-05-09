package com.arnopaja.supermac.plot;

import com.arnopaja.supermac.helpers.load.SuperParser;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * This class will handle the plot structure and everything
 * It will center on a map of Quests that do things TBD
 *
 * @author Ari Weiland
 */
public class Plot {

    private Map<Integer, Quest> quests;

    public Plot(Map<Integer, Quest> quests) {
        this.quests = quests;
        quests.get(0).activate();
    }

    public static class Parser extends SuperParser<Plot> {
        @Override
        public Plot fromJson(JsonElement element) {
            JsonObject object = element.getAsJsonObject();
            List<Quest> quests = getList(object, "quests", Quest.class);
            Map<Integer, Quest> questMap = new HashMap<Integer, Quest>();
            for (Quest quest : quests) {
                questMap.put(quest.getId(), quest);
            }
            JsonArray array = object.getAsJsonArray("dependencies");
            addDependencies(questMap, dependenciesFromJson(array));
            return new Plot(questMap);
        }

        @Override
        public JsonElement toJson(Plot object) {
            JsonObject json = new JsonObject();
            addList(json, "quests", object.quests.values(), Quest.class);
            json.add("dependencies", dependenciesToJson(getDependencies(object.quests)));
            return json;
        }

        private static Map<Integer, Integer[]> getDependencies(Map<Integer, Quest> quests) {
            Map<Integer, Integer[]> dependencies = new HashMap<Integer, Integer[]>();
            for (Map.Entry<Integer, Quest> entry : quests.entrySet()) {
                Set<Quest> posts = entry.getValue().getPostreqs();
                Integer[] postIds = new Integer[posts.size()];
                int i=0;
                for (Quest post : posts) {
                    postIds[i] = post.getId();
                    i++;
                }
                dependencies.put(entry.getKey(), postIds);
            }
            return dependencies;
        }

        private static JsonArray dependenciesToJson(Map<Integer, Integer[]> dependecies) {
            JsonArray json = new JsonArray();
            for (Map.Entry<Integer, Integer[]> entry : dependecies.entrySet()) {
                JsonObject object = new JsonObject();
                JsonArray array = new JsonArray();
                for (int i : entry.getValue()) {
                    array.add(new JsonPrimitive(i));
                }
                addInt(object, "quest", entry.getKey());
                object.add("unlocks", array);
                json.add(object);
            }
            return json;
        }

        public static void addDependencies(Map<Integer, Quest> quests, Map<Integer, Integer[]> dependencies) {
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

        private static Map<Integer, Integer[]> dependenciesFromJson(JsonArray array) {
            Map<Integer, Integer[]> map = new HashMap<Integer, Integer[]>();
            for (JsonElement e : array) {
                JsonObject object = e.getAsJsonObject();
                int pre = getInt(object, "quest");
                JsonArray a = object.getAsJsonArray("unlocks");
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
