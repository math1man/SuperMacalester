package com.arnopaja.supermac.world;

import com.arnopaja.supermac.helpers.AssetLoader;
import com.arnopaja.supermac.helpers.Controller;
import com.arnopaja.supermac.helpers.SuperParser;
import com.arnopaja.supermac.helpers.dialogue.Dialogue;
import com.arnopaja.supermac.world.grid.GameMap;
import com.arnopaja.supermac.world.grid.Grid;
import com.arnopaja.supermac.world.grid.Location;
import com.arnopaja.supermac.world.objects.Entity;
import com.arnopaja.supermac.world.objects.MainMapCharacter;
import com.arnopaja.supermac.world.objects.MapNpc;
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

    private final Map<String, GameMap> maps;
    private final MainMapCharacter mainCharacter;

    public World(Map<String, GameMap> maps) {
        if (!maps.containsKey("world") || maps.get("world") == null || !maps.get("world").isGrid()) {
            throw new IllegalArgumentException("Missing or malformed world grid!");
        }
        this.maps = maps;
        mainCharacter = new MainMapCharacter(new Location(getWorld(), 36, 36));
        initCharacters();
    }

    public World(World world) {
        this(world.maps);
    }

    private void initCharacters() {
        MapNpc character = new MapNpc();
        character.setAsset(AssetLoader.getAsset("Betsy"));
        character.setInteractable(true);
        character.setInteraction(SuperParser.parse("Betsy", AssetLoader.dialogueHandle, Dialogue.class).toInteraction());
        character.changeGrid(new Location(getWorld(), 40, 40));
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
        for (GameMap map : maps.values()) {
            map.clear();
        }
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
        return maps.get(name).getGrid(floor);
    }

    public Grid getWorld() {
        return maps.get("world").getGrid(0);
    }

    public Collection<GameMap> getMaps() {
        return Collections.unmodifiableCollection(maps.values());
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
            world.clear();
            JsonObject object = element.getAsJsonObject();
            JsonArray entities = getEntitiesJson(object);
            for (JsonElement e : entities) {
                // Just instantiating entities puts them in the world,
                // so nothing else needs to be done here
                SuperParser.fromJson(e, Entity.class);
            }
            return world;
        }

        @Override
        public JsonElement toJson(World object) {
            JsonObject json = new JsonObject();
            JsonArray array = new JsonArray();
            for (GameMap map : object.getMaps()) {
                for (Entity entity : map.getEntities()) {
                    if (!entity.isQuestEntity() && !(entity instanceof MainMapCharacter)) {
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
