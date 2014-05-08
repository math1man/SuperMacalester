package com.arnopaja.supermac.world;

import com.arnopaja.supermac.helpers.Controller;
import com.arnopaja.supermac.helpers.load.AssetLoader;
import com.arnopaja.supermac.helpers.load.SuperParser;
import com.arnopaja.supermac.world.grid.Grid;
import com.arnopaja.supermac.world.objects.Entity;
import com.arnopaja.supermac.world.objects.MainMapCharacter;
import com.arnopaja.supermac.world.objects.QuestNpc;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

/**
 * @author Ari Weiland
 */
public class World implements Controller {

    private final Map<String, Grid> grids;
    private final MainMapCharacter mainCharacter;

    public World(Map<String, Grid> grids, MainMapCharacter mainCharacter) {
        if (!grids.containsKey("world") || grids.get("world") == null) {
            throw new IllegalArgumentException("Missing or malformed world grid!");
        }
        this.grids = grids;
        this.mainCharacter = mainCharacter;
    }

    public World(World world) {
        this(world.grids, world.mainCharacter);
    }

    @Override
    public void update(float delta) {
        Grid currentGrid = mainCharacter.getGrid();
        // Cannot just iterate over currentGrid.getEntities() because we cannot modify that iterable
        Object[] entities = currentGrid.getEntities().toArray();
        for (Object entity : entities) {
            ((Entity) entity).update(delta);
        }
    }

    public void clear() {
        for (Grid grid : grids.values()) {
            grid.clear();
        }
    }

    public Grid getGrid(String name) {
        return grids.get(name);
    }

    public Grid getWorld() {
        return getGrid("world");
    }

    public Collection<Grid> getGrids() {
        return Collections.unmodifiableCollection(grids.values());
    }

    public Map<String, Grid> getGridMap() {
        return grids;
    }

    public MainMapCharacter getMainCharacter() {
        return mainCharacter;
    }

    /**
     * Because a single instance of world is statically maintained in
     * the SuperParser, and is the same instance as GameScreen's world,
     * the return value will always be that same reference, so any
     * methods that return a World can generally be ignored.
     */
    public static class Parser extends SuperParser<World> {
        @Override
        public World fromJson(JsonElement element) {
            for (Grid map : AssetLoader.grids.values()) {
                map.clear();
            }
            JsonObject object = element.getAsJsonObject();
            JsonArray entities = getEntitiesJson(object);
            MainMapCharacter mainCharacter = null;
            for (JsonElement e : entities) {
                // Just instantiating entities puts them in the world,
                // so nothing else needs to be done here
                Entity entity = SuperParser.fromJson(e, Entity.class);
                if (entity instanceof MainMapCharacter) {
                    mainCharacter = (MainMapCharacter) entity;
                }
            }
            if (mainCharacter == null) {
                throw new IllegalArgumentException("Main character not found!");
            }
            return new World(AssetLoader.grids, mainCharacter);
        }

        @Override
        public JsonElement toJson(World object) {
            JsonObject json = new JsonObject();
            JsonArray array = new JsonArray();
            for (Grid map : object.getGrids()) {
                for (Entity entity : map.getEntities()) {
                    if (!(entity instanceof QuestNpc)) {
                        array.add(SuperParser.toJson(entity, Entity.class));
                    }
                }
            }
            json.add("entities", array);
            return json;
        }

        private JsonArray getEntitiesJson(JsonObject object) {
            JsonArray entities = object.getAsJsonArray("entities");
            if (object.has("origin")) {
                JsonArray array = object.getAsJsonArray("origin");
                int originX = array.get(0).getAsInt();
                int originY = array.get(1).getAsInt();
                for (int i=0; i<entities.size(); i++) {
                    JsonObject entity = entities.get(i).getAsJsonObject();
                    JsonObject location = entity.getAsJsonObject("location");
                    if (location.getAsJsonPrimitive("grid").getAsString().equals("world")) {
                        shiftLocation(location, originX, originY);
                    }
                    if (entity.has("destination")) {
                        JsonObject destination = entity.getAsJsonObject("destination");
                        if (destination.getAsJsonPrimitive("grid").getAsString().equals("world")) {
                            shiftLocation(destination, originX, originY);
                        }
                    }
                }
            }
            return entities;
        }

        private void shiftLocation(JsonObject location, int shiftX, int shiftY) {
            // this should adjust the coordinates
            int x = location.getAsJsonPrimitive("x").getAsInt() + shiftX;
            int y = location.getAsJsonPrimitive("y").getAsInt() + shiftY;
            location.addProperty("x", x);
            location.addProperty("y", y);
        }
    }
}
