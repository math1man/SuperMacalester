package com.arnopaja.supermac.plot;

import com.arnopaja.supermac.helpers.Interaction;
import com.arnopaja.supermac.helpers.SuperParser;
import com.arnopaja.supermac.world.grid.Location;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

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

    private final QuestEntity questNpc;
    private final Location location;
    private final Interaction mainInteraction;
    private final boolean delay;
    private Interaction netInteraction = Interaction.NULL;
    private boolean isActive = false;

    public Goal(QuestEntity questNpc, Location location, Interaction mainInteraction, boolean delay) {
        this.questNpc = questNpc;
        this.location = location;
        this.mainInteraction = mainInteraction;
        this.delay = delay;
    }

    public void activate() {
        questNpc.setInteraction(netInteraction);
        questNpc.changeGrid(location);
        isActive = true;
    }

    public void deactivate() {
        questNpc.setInteractable(false);
        questNpc.removeFromGrid(delay);
        isActive = false;
    }

    protected void setQuest(Quest quest) {
        netInteraction = mainInteraction.attach(quest);
    }

    public QuestEntity getQuestNpc() {
        return questNpc;
    }

    public Interaction getMainInteraction() {
        return mainInteraction;
    }

    public Location getLocation() {
        return location;
    }

    public boolean isActive() {
        return isActive;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Goal)) return false;

        Goal goal = (Goal) o;

        return questNpc.equals(goal.questNpc) && location.equals(goal.location)
                && mainInteraction.equals(goal.mainInteraction);
    }

    public static class Parser extends SuperParser<Goal> {
        @Override
        public Goal fromJson(JsonElement element) {
            JsonObject object = element.getAsJsonObject();
            QuestEntity entity = getObject(object, QuestEntity.class);
            Location location = getObject(object, Location.class);
            Interaction interaction = getObject(object, Interaction.class);
            boolean delay = true;
            if (object.has("delay")) {
                delay = getBoolean(object, "delay");
            }
            return new Goal(entity, location, interaction, delay);
        }

        @Override
        public JsonElement toJson(Goal object) {
            JsonObject json = new JsonObject();
            addObject(json, object.getQuestNpc(), QuestEntity.class);
            addObject(json, object.getLocation(), Location.class);
            addObject(json, object.getMainInteraction(), Interaction.class);
            return json;
        }
    }
}
