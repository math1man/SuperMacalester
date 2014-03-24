package com.arnopaja.supermac.plot;

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

    public static void init() {
        goals = new ArrayList<Goal>();
        // TODO: create goals and everything that goes into them
        // this will be a very long method if it is hard-coded
        // TODO: make some way to parse goals from an external doc?
    }

    public static void load() {

    }

    public static void save() {

    }
}
