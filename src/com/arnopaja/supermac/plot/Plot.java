package com.arnopaja.supermac.plot;

import com.arnopaja.supermac.world.objects.Entity;

import java.util.ArrayList;
import java.util.List;

/**
 * This will be a static class that handles the plot structure and everything
 * It will center on a list of Goals that do something TBD
 *
 * @author Ari Weiland
 */
public class Plot {

    private static List<Goal> goals;
    private static List<Entity> plotEntities;

    public static void init() {
        goals = new ArrayList<Goal>();
        plotEntities = new ArrayList<Entity>();
        // TODO: create goals, entities, and everything that goes into them
        // this will be a very long method if it is hard-coded

        // TODO: make some way to parse goals from an external doc?
        // TODO: make some way to parse entities from an external doc?
    }

    public static void save() {
        // this method will somehow store which goals are active and complete
    }

    public static void load() {
        // this method will read which goals are active and complete and set them appropriately
    }
}
