package com.arnopaja.supermac.helpers;

import com.arnopaja.supermac.battle.Battle;
import com.arnopaja.supermac.battle.characters.*;
import com.arnopaja.supermac.helpers.dialogue.Dialogue;
import com.arnopaja.supermac.inventory.*;
import com.arnopaja.supermac.plot.Goal;
import com.arnopaja.supermac.plot.Plot;
import com.arnopaja.supermac.plot.Quest;
import com.arnopaja.supermac.plot.Settings;
import com.arnopaja.supermac.world.World;
import com.arnopaja.supermac.world.grid.Direction;
import com.arnopaja.supermac.world.grid.Location;
import com.arnopaja.supermac.world.objects.*;
import com.badlogic.gdx.files.FileHandle;
import com.google.gson.*;

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
        addParser(Armor.class,            new Armor.Parser());
        addParser(Asteroid.class,         new Asteroid.Parser());
        addParser(Battle.class,           new Battle.Parser());
        addParser(BattleClass.class,      new EnumParser<BattleClass>(BattleClass.class));
        addParser(Chest.class,            new Chest.Parser());
        addParser(Dialogue.class,         new Dialogue.Parser());
        addParser(Direction.class,        new EnumParser<Direction>(Direction.class));
        addParser(Door.class,             new Door.Parser());
        addParser(Enemy.class,            new Enemy.Parser());
        addParser(EnemyParty.class,       new EnemyParty.Parser());
        addParser(Entity.class,           new Entity.Parser());
        addParser(GarbageCan.class,       new GarbageCan.Parser());
        addParser(GenericItem.class,      new GenericItem.Parser());
        addParser(Goal.class,             new Goal.Parser());
        addParser(Hero.class,             new Hero.Parser());
        addParser(Interaction.class,      new Interaction.Parser());
        addParser(Inventory.class,        new Inventory.Parser());
        addParser(Item.class,             new Item.Parser());
        addParser(Location.class,         new Location.Parser());
        addParser(MainMapCharacter.class, new MainMapCharacter.Parser());
        addParser(MainParty.class,        new MainParty.Parser());
        addParser(MapNpc.class,           new MapNpc.Parser());
        addParser(Plot.class,             new Plot.Parser());
        addParser(Quest.class,            new Quest.Parser());
        addParser(Settings.class,         new Settings.Parser());
        addParser(SpecialItem.class,      new SpecialItem.Parser());
        addParser(Spell.class,            new Spell.Parser());
        addParser(Weapon.class,           new Weapon.Parser());
    }

    private static <U> void addParser(Class<U> clazz, SuperParser<U> parser) {
        parsers.put(clazz, parser);
    }

    protected static World world;

    public static void initParsers(World w) {
        world = w;
    }

    public static void initItems(FileHandle handle) {
        parseAll(handle, GenericItem.class);
    }

    public static void initSpells(FileHandle handle) {
        parseAll(handle, Spell.class);
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
        JsonObject object = element.getAsJsonObject();
        if (object.has(name)) {
            return fromJson(object.get(name));
        }
        return null;
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
        JsonElement element = getJsonHead(json);
        if (element == null || !element.isJsonObject()) {
            return null;
        }
        JsonObject object = element.getAsJsonObject();
        if (object.has(name)) {
            return fromJson(object.get(name));
        }
        return null;
    }

    public T parse(String json) {
        return fromJson(getJsonHead(json));
    }

    /**
     * Parses all members in the specified JSON string as if they were of type T.
     * Will fail catastrophically if any member is incorrectly formatted.
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

    public static <U> U parse(String name, FileHandle handle, Class<U> clazz) {
        return parse(name, handle.readString(), clazz);
    }

    public static <U> U parse(String json, Class<U> clazz) {
        return clazz.cast(parsers.get(clazz).parse(json));
    }

    public static <U> U parse(FileHandle handle, Class<U> clazz) {
        return parse(handle.readString(), clazz);
    }

    public static <U> List<U> parseAll(String json, Class<U> clazz) {
        return parsers.get(clazz).parseAll(json);
    }

    public static <U> List<U> parseAll(FileHandle handle, Class<U> clazz) {
        return parseAll(handle.readString(), clazz);
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

    protected static void addInt(JsonObject json, String name, float i) {
        json.addProperty(name, i);
    }

    protected static float getFloat(JsonObject json, String name) {
        return json.getAsJsonPrimitive(name).getAsFloat();
    }

    protected static void addFloat(JsonObject json, String name, float f) {
        json.addProperty(name, f);
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

    protected static <U> U getObject(JsonObject json, String name, Class<U> clazz) {
        return fromJson(json.getAsJsonObject(name), clazz);
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

    protected static boolean has(JsonObject json, Class clazz) {
        return json.has(clazz.getSimpleName().toLowerCase());
    }

    protected static String getClass(JsonObject json) {
        return json.getAsJsonPrimitive("class").getAsString();
    }

    protected static void addClass(JsonObject json, Class clazz) {
        json.addProperty("class", clazz.getSimpleName());
    }

    protected static boolean hasClass(JsonObject json) {
        return json.has("class");
    }

    public static class EnumParser<T extends Enum> extends SuperParser<T> {

        private final Class<T> clazz;

        public EnumParser(Class<T> clazz) {
            this.clazz = clazz;
        }

        @Override
        public T fromJson(JsonElement element) {
            String enumName = element.getAsString().toUpperCase().trim();
            enumName = enumName.replace(" ", "_");
            return clazz.cast(Enum.valueOf(clazz, enumName));
        }

        @Override
        public JsonElement toJson(T object) {
            return new JsonPrimitive(object.name());
        }
    }
}
