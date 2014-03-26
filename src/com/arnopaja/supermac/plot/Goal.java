package com.arnopaja.supermac.plot;

import com.arnopaja.supermac.helpers.Interaction;
import com.arnopaja.supermac.world.grid.Location;
import com.arnopaja.supermac.world.objects.Entity;

/**
 * A goal is a single plot interaction.
 *
 * It has an entity, an interaction, and a location.  On activation,
 * the entity is given the interaction and placed at the location.
 * On completion, the entity will be removed from the grid (or
 * something like that, we need to figure that out). Also, any goal
 * that results from the completion of this goal will be activated.
 *
 * An entity can be used my multiple goals, so the interaction must
 * be given on activation, and we will need to design a system or
 * just structure the plot so that two goals with the same entity
 * are never active at the same time.
 *
 * @author Ari Weiland
 */
public class Goal {

    private final Entity entity;
    private final Interaction interaction;
    private final Location location;
    private boolean isActive;

    public Goal(Entity entity, Location location, Interaction interaction, Quest quest) {
        this.entity = entity;
        // TODO: the first interaction is the slow one, so this may cause problems
        this.interaction = Interaction.combine(interaction, Interaction.nextGoal(quest));
        this.location = location;
        this.isActive = false;
    }

    public void activate() {
        entity.putInGrid(location);
        isActive = true;
    }

    public void deactivate() {
        entity.removeFromGrid();
        isActive = false;
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

    public boolean isActive() {
        return isActive;
    }
}
