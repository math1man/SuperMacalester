package com.arnopaja.supermac.helpers.parser;

import com.arnopaja.supermac.helpers.Interaction;
import com.arnopaja.supermac.plot.Goal;
import com.arnopaja.supermac.plot.Quest;
import com.arnopaja.supermac.world.World;
import com.arnopaja.supermac.world.grid.Location;
import com.arnopaja.supermac.world.objects.Entity;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Ari Weiland
 */
public class QuestParser extends Parser<Quest> {

    private final EntityParser entityParser;
    private final LocationParser locationParser;
    private final InteractionParser interactionParser;

    public QuestParser(World world) {
        this.entityParser = new EntityParser();
        this.locationParser = new LocationParser(world);
        this.interactionParser = new InteractionParser();
    }

    @Override
    public Quest parse(JsonObject object) {
        int questId = object.getAsJsonPrimitive("id").getAsInt();
        JsonArray goalsJson = object.getAsJsonArray("goals");
        List<Goal> goals = new ArrayList<Goal>(goalsJson.size());
        for (JsonElement element : goalsJson) {
            goals.add(parseGoal(element.getAsJsonObject()));
        }
        // TODO: add prereqs and postreqs
        return new Quest(questId, goals);
    }

    public void addDependencies(Object dependencies) {
        // TODO: figure out how to list quest dependencies
        // TODO: apply quest dependencies
    }

    public Goal parseGoal(JsonObject object) {
        Entity entity = entityParser.parse(object.getAsJsonObject("entity"));
        Location location = locationParser.parse(object.getAsJsonObject("location"));
        Interaction interaction = interactionParser.parse(object.getAsJsonObject("interaction"));
        return new Goal(entity, location, interaction);
    }
}
