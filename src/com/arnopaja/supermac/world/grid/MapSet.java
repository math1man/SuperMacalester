package com.arnopaja.supermac.world.grid;

import java.util.Map;

/**
 * @author Ari Weiland
 */
public class MapSet {

    private final Grid world;
    private final Map<String, Building> buildings;

    public MapSet(Grid world, Map<String, Building> buildings) {
        if (world == null) {
            throw new NullPointerException("World grid cannot be null");
        }
        this.world = world;
        this.buildings = buildings;
    }

    public Grid getGrid(String name) {
        String gridName = name.replaceAll("[^\\p{Alpha}]*", ""); // get the text portion
        String number = name.replaceAll("[\\D]*", "");           // get the numeric portion
        int floor;
        if (number.isEmpty()) {
            floor = 1;
        } else {
            floor = Integer.parseInt(number);
        }
        return getGrid(gridName, floor);
    }

    public Grid getGrid(String name, int floor) {
        if (name.equalsIgnoreCase("world")) {
            return getWorld();
        } else {
            return getBuilding(name).getFloorByNumber(floor);
        }
    }

    public Grid getWorld() {
        return world;
    }

    public Building getBuilding(String name) {
        return buildings.get(name);
    }
}
