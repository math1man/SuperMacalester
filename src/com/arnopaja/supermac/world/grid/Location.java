package com.arnopaja.supermac.world.grid;

import com.arnopaja.supermac.GameScreen;
import com.arnopaja.supermac.helpers.interaction.Interaction;
import com.arnopaja.supermac.helpers.load.AssetLoader;
import com.arnopaja.supermac.helpers.load.SuperParser;
import com.arnopaja.supermac.world.objects.Entity;
import com.badlogic.gdx.math.Vector2;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * @author Ari Weiland
 */
public class Location implements Interaction {

    private final Grid grid;
    private Vector2 position;

    public Location(Grid grid) {
        this(grid, 0, 0);
    }

    public Location(Grid grid, int x, int y) {
        this(grid, new Vector2(x, y));
    }

    public Location(Grid grid, Vector2 position) {
        this.grid = grid;
        this.position = position;
    }

    public RenderGrid getRenderGrid() {
        return grid.getRenderGrid(position);
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

    public Location getAdjacent(Direction direction) {
        return new Location(grid, Direction.getAdjacent(position, direction));
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

    @Override
    public void run(GameScreen screen) {
        if (getEntity() != null) {
            getEntity().forceChangeGrid(null);
        }
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

    @Override
    public String toString() {
        return "Location{" +
                "grid=" + grid +
                ", position=" + position +
                '}';
    }

    public static class Parser extends SuperParser<Location> {
        @Override
        public Location fromJson(JsonElement element) {
            JsonObject object = element.getAsJsonObject();
            String gridName = getString(object, "grid");
            Grid grid = AssetLoader.grids.get(gridName);
            int x = getInt(object, "x");
            int y = getInt(object, "y");
            return new Location(grid, x, y);
        }

        @Override
        public JsonElement toJson(Location object) {
            JsonObject json = new JsonObject();
            addString(json, "grid", object.grid.getName());
            addInt(json, "x", (int) object.position.x);
            addInt(json, "y", (int) object.position.y);
            return json;
        }
    }
}
