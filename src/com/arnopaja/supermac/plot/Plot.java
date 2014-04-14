package com.arnopaja.supermac.plot;

import com.arnopaja.supermac.helpers.Savable;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.Map;

/**
 * This class will handle the plot structure and everything
 * It will center on a map of Quests that do things TBD
 *
 * @author Ari Weiland
 */
public class Plot implements Savable {

    private Map<Integer, Quest> quests;

    public Plot(Map<Integer, Quest> quests) {
        this.quests = quests;
    }

    @Override
    public JsonElement toJson() {
        JsonArray me = new JsonArray();
        for (Quest quest : quests.values()) {
            if (!quest.isInactive()) {
                JsonObject questJson = new JsonObject();
                questJson.addProperty("id", quest.getId());
                if (quest.isActive()) {
                    questJson.addProperty("active", true);
                    questJson.addProperty("goal", quest.getCurrentGoal());
                }
                me.add(questJson);
            }
        }
        return me;
    }

    @Override
    public void fromJson(JsonElement element) {
        JsonArray array = element.getAsJsonArray();
        for (JsonElement e : array) {
            JsonObject quest = e.getAsJsonObject();
            int id = quest.getAsJsonPrimitive("id").getAsInt();
            if (quest.has("active")) {
                quests.get(id).load(quest.getAsJsonPrimitive("goal").getAsInt());
            } else {
                quests.get(id).complete();
            }
        }
    }
}
