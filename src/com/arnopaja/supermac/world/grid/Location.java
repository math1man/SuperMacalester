package com.arnopaja.supermac.world.grid;

import com.arnopaja.supermac.helpers.AssetLoader;
import com.arnopaja.supermac.helpers.SuperParser;
import com.arnopaja.supermac.world.objects.Entity;
import com.badlogic.gdx.math.Vector2;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * @author Ari Weiland
 */
public class Location {

    private final Grid grid;
    private Vector2 position;
    private Direction facing;

    public Location(Grid grid) {
        this(grid, 0, 0, Direction.WEST);
    }

    public Location(Grid grid, int x, int y, Direction facing) {
        this(grid, new Vector2(x, y), facing);
    }

    public Location(Grid grid, Vector2 position, Direction facing) {
        this.grid = grid;
        this.position = position;
        this.facing = facing;
    }

    public RenderGrid getRenderGrid(int renderGridWidth, int renderGridHeight) {
        return grid.getRenderGrid(position, renderGridWidth, renderGridHeight);
    }

    public Location getNearestValidLocation() {
        return grid.getNearestValidLocation(this);
    }

    /**
     * Gets the entity at this location.
     * In other words, gets the entity at this position in this grid.
     *
     * @return the entity
     */
    public Entity getEntity() {
        return grid.getEntity(position);
    }

    public void save(String key) {
        AssetLoader.prefs.putFloat(key + "_x", position.x);
        AssetLoader.prefs.putFloat(key + "_y", position.y);
        AssetLoader.prefs.putString(key + "_direction", facing.name());
        AssetLoader.prefs.flush();
    }

    public void load(String key) {
        float x = AssetLoader.prefs.getFloat(key + "_x");
        float y = AssetLoader.prefs.getFloat(key + "_y");
        String direction = AssetLoader.prefs.getString(key + "_direction");
        setPosition(new Vector2(x, y));
        setFacing(Direction.valueOf(direction));
    }

    public Grid getGrid() {
        return grid;
    }

    public Vector2 getPosition() {
        return position;
    }

    public void setPosition(Vector2 position) {
        this.position = position;
    }

    public Direction getFacing() {
        return facing;
    }

    public void setFacing(Direction facing) {
        this.facing = facing;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Location)) return false;

        Location location = (Location) o;

        // if grid is not null but position is, it deserves to throw NullPointerExceptions
        return grid == null ? location.grid == null :
                (grid.equals(location.grid) && position.equals(location.position));

    }

    public static class Parser extends SuperParser<Location> {
        @Override
        public Location convert(JsonElement element) {
            JsonObject location = element.getAsJsonObject();
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
            String dir = location.getAsJsonPrimitive("direction").getAsString().trim();
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
            return new Location(grid, x, y, direction);
        }
    }
}
