package com.arnopaja.supermac.plot;

import com.arnopaja.supermac.helpers.AssetLoader;
import com.arnopaja.supermac.helpers.dialogue.Dialogue;
import com.arnopaja.supermac.helpers.dialogue.DialogueOptions;
import com.arnopaja.supermac.helpers.Interaction;
import com.arnopaja.supermac.world.WorldController;
import com.arnopaja.supermac.world.grid.Direction;
import com.arnopaja.supermac.world.grid.Location;
import com.arnopaja.supermac.world.objects.MapNPC;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * This class will handle the plot structure and everything
 * It will center on a map of Quests that do things TBD
 *
 * @author Ari Weiland
 */
public class Plot {

    private static final AtomicInteger nextQuestID = new AtomicInteger(0);

    private final WorldController world;
    private Map<Integer, Quest> quests;

    public Plot(WorldController world) {
        this.world = world;
        this.quests = new HashMap<Integer, Quest>();
        // TODO: create Quests and everything that goes into them
        // this will be a very long method if it is hard-coded

        // TODO: make some way to parse quests from an external doc?

//        initTestQuest();
    }

    private void initTestQuest() {
        MapNPC character = new MapNPC();
        character.setFacingSprites(AssetLoader.steven);
        character.setFacingAnimations(AssetLoader.stevenStepping);

        Location location = new Location(world.getWorldGrid(), new Vector2(5, 5), Direction.NORTH);

        Interaction interaction = Interaction.dialogue(new Dialogue("Hi! My name is Paul and I like to code things. " +
                "I have decided I am going to work at a startup in Minneapolis this summer.<d>" +
                "I spent this past summer here working for Libby Shoop designing a website.",
                new DialogueOptions("Do you like to code?", DialogueOptions.YES_NO_OPTIONS)));

        List<Goal> testGoals = new ArrayList<Goal>();
        testGoals.add(new Goal(character, location, interaction));

        Quest test = new Quest(nextQuestID.getAndIncrement(), testGoals);
        test.activate(null);
        addQuest(test);
    }

    public void addQuest(Quest quest) {
        quests.put(quest.ID, quest);
    }

    public void save() {
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

    public void load() {
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