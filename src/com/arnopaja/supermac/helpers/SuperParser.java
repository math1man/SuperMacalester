package com.arnopaja.supermac.helpers;

import com.arnopaja.supermac.battle.Battle;
import com.arnopaja.supermac.helpers.dialogue.Dialogue;
import com.arnopaja.supermac.inventory.*;
import com.arnopaja.supermac.plot.Quest;
import com.arnopaja.supermac.world.World;
import com.arnopaja.supermac.world.grid.Location;
import com.arnopaja.supermac.world.objects.*;
import com.google.gson.JsonElement;
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
        addParser(AbstractItem.class,     new AbstractItem.Parser());
        addParser(Armor.class,            new Armor.Parser());
        addParser(Battle.class,           new Battle.Parser());
        addParser(Chest.class,            new Chest.Parser());
        addParser(Dialogue.class,         new Dialogue.Parser());
        addParser(Door.class,             new Door.Parser());
        addParser(Entity.class,           new Entity.Parser());
        addParser(Interaction.class,      new Interaction.Parser());
        addParser(Item.class,             new Item.Parser());
        addParser(Location.class,         new Location.Parser());
        addParser(MainMapCharacter.class, new MainMapCharacter.Parser());
        addParser(MapNpc.class,           new MapNpc.Parser());
        addParser(Quest.class,            new Quest.Parser());
        addParser(SpecialItem.class,      new SpecialItem.Parser());
        addParser(Weapon.class,           new Weapon.Parser());
    }

    protected static <U> void addParser(Class<U> clazz, SuperParser<U> parser) {
        parsers.put(clazz, parser);
    }

    protected static World world;

    public static void initParsers(World w) {
        world = w;
    }

    /**
     * Converts a JSON element into an object of type T
     * @param element
     * @return
     */
    public abstract T convert(JsonElement element);

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
        return convert(e);
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
            parsables.add(convert(entry.getValue()));
        }
        return parsables;
    }

    public static <U> U convert(JsonElement element, Class<U> clazz) {
        return clazz.cast(parsers.get(clazz).convert(element));
    }

    public static <U> U parse(String name, String json, Class<U> clazz) {
        System.out.println(parsers);
        return clazz.cast(parsers.get(clazz).parse(name, json));
    }

    public static <U> List<U> parseAll(String json, Class<U> clazz) {
        return parsers.get(clazz).parseAll(json);
    }

    public static Map<Integer, Quest> parseQuestMap(String json) {
        return ((Quest.Parser) parsers.get(Quest.class)).parseMap(json);
    }
}
