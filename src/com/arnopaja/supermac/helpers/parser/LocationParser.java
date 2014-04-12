package com.arnopaja.supermac.helpers.parser;

import com.arnopaja.supermac.world.World;
import com.arnopaja.supermac.world.grid.Direction;
import com.arnopaja.supermac.world.grid.Grid;
import com.arnopaja.supermac.world.grid.Location;
import com.google.gson.JsonObject;

/**
 * @author Ari Weiland
 */
public class LocationParser extends Parser<Location> {

    private final World world;

    public LocationParser(World world) {
        this.world = world;
    }

    @Override
    public Location parse(JsonObject location) {
        String gridName = location.getAsJsonPrimitive("grid").getAsString();
        Grid grid;
        if (gridName.trim().equalsIgnoreCase("world")) {
            grid = world.getWorldGrid();
        } else {
            String name = gridName.replaceAll("\\p{Alpha}", "").toLowerCase();
            int floor = Integer.parseInt(gridName.replaceAll("\\D", ""));
            grid = world.getBuilding(name).getFloorByNumber(floor);
        }
        int x = location.getAsJsonPrimitive("x").getAsInt();
        int y = location.getAsJsonPrimitive("y").getAsInt();
        String dir = location.getAsJsonPrimitive("direction").getAsString();
        Direction direction;
        switch (dir.charAt(0)) {
            case 'n':
                direction = Direction.NORTH;
                break;
            case 'e':
                direction = Direction.EAST;
                break;
            case 's':
                direction = Direction.SOUTH;
                break;
            case 'w':
            default:
                direction = Direction.WEST;
                break;
        }
        // TODO: how to identify the grid?
        return new Location(grid, x, y, direction);
    }
}
