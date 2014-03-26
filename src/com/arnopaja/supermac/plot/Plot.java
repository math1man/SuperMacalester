package com.arnopaja.supermac.plot;

import com.arnopaja.supermac.helpers.AssetLoader;

import java.util.HashMap;
import java.util.Map;

/**
 * This class will handle the plot structure and everything
 * It will center on a map of Quests that do things TBD
 *
 * @author Ari Weiland
 */
public class Plot {

    // TODO: if possible, I think I would like to make this non-static
    private static Map<Integer, Quest> quests;

    public static void init() {
        quests = new HashMap<Integer, Quest>();
        // TODO: create Quests and everything that goes into them
        // this will be a very long method if it is hard-coded

        // TODO: make some way to parse quests from an external doc?
    }

    public static void save() {
        StringBuilder sb = new StringBuilder();
        for (Quest quest : quests.values()) {
            if (!quest.isInactive()) {
                // All active and complete quests are saved with their ID
                sb.append(quest.ID);
                if (quest.isComplete()) {
                    // All active quests have their current goal appended
                    sb.append("-").append(quest.getCurrentGoal());
                }
                sb.append("\n");
            }
        }
        AssetLoader.prefs.putString("Quests", sb.toString());
        AssetLoader.prefs.flush();
    }

    public static void load() {
        // this method will first read which quests are complete and complete them
        // it then needs to set active quests to the proper current goal
        String load = AssetLoader.prefs.getString("Quests");
        String[] questIDs = load.split("\n");
        for (String questID : questIDs) {
            if (questID.contains("-")) {
                // loads all active quests to the proper goal
                String[] split = questID.split("-");
                int id = new Integer(split[0]);
                int currentGoal = new Integer(split[1]);
                quests.get(id).load(currentGoal);
            } else {
                // completes all quests that were previously completed
                int id = new Integer(questID);
                quests.get(id).complete();
            }
        }
    }
}
