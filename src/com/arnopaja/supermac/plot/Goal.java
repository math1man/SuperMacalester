package com.arnopaja.supermac.plot;

import com.arnopaja.supermac.helpers.Interaction;
import com.arnopaja.supermac.world.grid.Location;
import com.arnopaja.supermac.world.objects.Entity;

import java.util.List;

/**
 * A goal is a single plot event that will need to be fleshed out
 * It will likely have some characters, dialogue, and possibly battle scenarios
 *
 * @author Ari Weiland
 */
public class Goal {

    public static enum GoalState { INACTIVE, ACTIVE, COMPLETE }

    private final Entity entity;
    private final Interaction interaction;
    private final Location location;

    private GoalState state = GoalState.INACTIVE;
    private Goal prerequisite;
    private List<Goal> postrequisites;

    public Goal(Entity entity, Location location, Interaction interaction) {
        this.entity = entity;
        this.interaction = interaction;
        this.location = location;
    }

    public void activate() {
        state = GoalState.ACTIVE;
        location.putInGrid(entity);
    }

    public void complete() {
        state = GoalState.COMPLETE;
        entity.removeFromGrid();
        for (Goal goal : postrequisites) {
            goal.activate();
        }
    }

    public boolean isInactive() {
        return state == GoalState.INACTIVE;
    }

    public boolean isActive() {
        return state == GoalState.ACTIVE;
    }

    public boolean isComplete() {
        return state == GoalState.COMPLETE;
    }

    public Entity getEntity() {
        return entity;
    }

    public Interaction getInteraction() {
        return interaction;
    }

    public Location getLocation() {
        return location;
    }

    public Goal getPrerequisite() {
        return prerequisite;
    }

    public void setPrerequisite(Goal prerequisite) {
        this.prerequisite = prerequisite;
    }

    public List<Goal> getPostrequisites() {
        return postrequisites;
    }

    public void setPostrequisites(List<Goal> postrequisites) {
        this.postrequisites = postrequisites;
    }
}
