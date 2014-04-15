package com.arnopaja.supermac.helpers;

import com.arnopaja.supermac.battle.Battle;
import com.arnopaja.supermac.helpers.dialogue.Dialogue;
import com.arnopaja.supermac.inventory.*;
import com.arnopaja.supermac.plot.Goal;
import com.arnopaja.supermac.plot.Quest;
import com.arnopaja.supermac.plot.Settings;
import com.arnopaja.supermac.world.grid.Location;
import com.arnopaja.supermac.world.grid.MapSet;
import com.arnopaja.supermac.world.objects.*;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.*;

/**
 * Super class for all parsers.
 *
 * @author Ari Weiland
 */
public abstract class SuperParser<T> {

    private static final JsonParser parser = new JsonParser();
    private static final Map<Class, SuperParser> parsers = new HashMap<Class, SuperParser>();
    static {
        addParser(GenericItem.class,     new GenericItem.Parser());
        addParser(Armor.class,            new Armor.Parser());
        addParser(Battle.class,           new Battle.Parser());
        addParser(Chest.class,            new Chest.Parser());
        addParser(Dialogue.class,         new Dialogue.Parser());
        addParser(Door.class,             new Door.Parser());
        addParser(Entity.class,           new Entity.Parser());
        addParser(Goal.class,             new Goal.Parser());
        addParser(Interaction.class,      new Interaction.Parser());
        addParser(Inventory.class,        new Inventory.Parser());
        addParser(Item.class,             new Item.Parser());
        addParser(Location.class,         new Location.Parser());
        addParser(MainMapCharacter.class, new MainMapCharacter.Parser());
        addParser(MapNpc.class,           new MapNpc.Parser());
        addParser(Quest.class,            new Quest.Parser());
        addParser(Settings.class,         new Settings.Parser());
        addParser(SpecialItem.class,      new SpecialItem.Parser());
        addParser(Weapon.class,           new Weapon.Parser());
        // TODO: we probably need battle character and party parsers
    }

    protected static <U> void addParser(Class<U> clazz, SuperParser<U> parser) {
        parsers.put(clazz, parser);
    }

    protected static MapSet maps;

    public static void initParsers(MapSet m) {
        maps = m;
    }

    public static void initItems(String itemsJson) {
        parseAll(itemsJson, GenericItem.class);
    }

    /**
     * Converts a JSON element into an object of type T
     * @param element
     * @return
     */
    public abstract T fromJson(JsonElement element);

    public abstract JsonElement toJson(T object);

    /**
     * Finds and parses a member of name name from the specified JSON element.
     * Will return null if the name is not found, or fail catastrophically if
     * the name's element is incorrectly formatted.
     * @param name
     * @param element
     * @return
     */
    public T parse(String name, JsonElement element) {
        if (element == null) {
            return null;
        }
        JsonElement e = element.getAsJsonObject().get(name);
        if (e == null) {
            return null;
        }
        return fromJson(e);
    }

    /**
     * Returns the head of the JSON tree
     * @param json
     * @return
     */
    public JsonElement getJsonHead(String json) {
        return parser.parse(json);
    }

    /**
     * Finds and parses a member of name name from the specified JSON string.
     * Will return null if the name is not found, or fail catastrophically
     * if the name's element is incorrectly formatted.
     * @param name
     * @param json
     * @return
     */
    public T parse(String name, String json) {
        return parse(name, getJsonHead(json));
    }

    /**
     * Parses all members in the specified JSON string as if they were of type T.
     * Will probably fail catastrophically if any member is incorrectly formatted.
     * @param json
     * @return
     */
    public List<T> parseAll(String json) {
        JsonElement element = getJsonHead(json);
        List<T> parsables = new ArrayList<T>();
        Set<Map.Entry<String, JsonElement>> entries = element.getAsJsonObject().entrySet();
        for (Map.Entry<String, JsonElement> entry : entries) {
            parsables.add(fromJson(entry.getValue()));
        }
        return parsables;
    }

    public static <U> U fromJson(JsonElement element, Class<U> clazz) {
        return clazz.cast(parsers.get(clazz).fromJson(element));
    }

    public static <U> JsonElement toJson(U element, Class<U> clazz) {
        return parsers.get(clazz).toJson(element);
    }

    public static <U> U parse(String name, String json, Class<U> clazz) {
        return clazz.cast(parsers.get(clazz).parse(name, json));
    }

    public static <U> List<U> parseAll(String json, Class<U> clazz) {
        return parsers.get(clazz).parseAll(json);
    }

    public static Map<Integer, Quest> parseQuestMap(String json) {
        return ((Quest.Parser) parsers.get(Quest.class)).parseMap(json);
    }

    protected static boolean getBoolean(JsonObject json, String name) {
        return json.getAsJsonPrimitive(name).getAsBoolean();
    }

    protected static void addBoolean(JsonObject json, String name, boolean b) {
        json.addProperty(name, b);
    }

    protected static int getInt(JsonObject json, String name) {
        return json.getAsJsonPrimitive(name).getAsInt();
    }

    protected static void addInt(JsonObject json, String name, int i) {
        json.addProperty(name, i);
    }

    protected static String getString(JsonObject json, String name) {
        return json.getAsJsonPrimitive(name).getAsString();
    }

    protected static void addString(JsonObject json, String name, String string) {
        json.addProperty(name, string);
    }

    protected static <U> U getObject(JsonObject json, Class<U> clazz) {
        return getObject(json, clazz.getSimpleName().toLowerCase(), clazz);
    }

    protected static <U> U getObject(JsonObject object, String name, Class<U> clazz) {
        return fromJson(object.getAsJsonObject(name), clazz);
    }

    protected static <U> void addObject(JsonObject json, U object, Class<U> clazz) {
        addObject(json, clazz.getSimpleName().toLowerCase(), object, clazz);
    }

    protected static <U> void addObject(JsonObject json, String name, U object, Class<U> clazz) {
        json.add(name, toJson(object, clazz));
    }

    protected static <U> List<U> getList(JsonObject json, String name, Class<U> clazz) {
        JsonArray array = json.getAsJsonArray(name);
        List<U> contents = new ArrayList<U>();
        for (JsonElement e : array) {
            contents.add(fromJson(e, clazz));
        }
        return contents;
    }

    protected static <U> void addList(JsonObject json, String name, Collection<U> list, Class<U> clazz) {
        JsonArray array = new JsonArray();
        for (U element : list) {
            array.add(toJson(element, clazz));
        }
        json.add(name, array);
    }

    protected static String getClass(JsonObject json) {
        return json.getAsJsonPrimitive("class").getAsString();
    }

    protected static void addClass(JsonObject json, Object object) {
        json.addProperty("class", object.getClass().getSimpleName());
    }
}
