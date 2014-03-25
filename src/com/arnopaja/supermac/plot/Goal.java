package com.arnopaja.supermac.plot;

import com.arnopaja.supermac.helpers.Interaction;
import com.arnopaja.supermac.world.grid.Location;
import com.arnopaja.supermac.world.objects.Entity;

import java.util.List;

/**
 * A goal is a single plot interaction.
 *
 * It has an entity, an interaction, and a location.  On activation,
 * the entity is given the interaction and placed at the location.
 * On completion, the entity will be removed from the grid (or
 * something like that, we need to figure that out). Also, any goals
 * that result from the completion of this goal will be activated.
 *
 * An entity can be used my multiple goals, so the interaction must
 * be given on activation, and we will need to design a system or
 * just structure the plot so that two goals with the same entity
 * are never active at the same time.
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

    /**
     * Aside from the very first goal(s), this should only be called by
     * other goals via the complete method.
     */
    public void activate() {
        if (isInactive()) {
            state = GoalState.ACTIVE;
            entity.putInGrid(location);
        }
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
