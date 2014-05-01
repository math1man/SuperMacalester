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
    private static final Map<String, SuperParser> parsers = new HashMap<String, SuperParser>();
    static {
        addParser(Armor.class,                  new Armor.Parser());
        addParser(Asteroid.class,               new Asteroid.Parser());
        addParser(Battle.class,                 new Battle.Parser());
        addParser(BattleClass.class,            new EnumParser<BattleClass>(BattleClass.class));
        addParser(Chest.class,                  new Chest.Parser());
        addParser(Dialogue.class,               new Dialogue.Parser());
        addParser(Direction.class,              new EnumParser<Direction>(Direction.class));
        addParser(Door.class,                   new Door.Parser());
        addParser(Enemy.class,                  new Enemy.Parser());
        addParser(EnemyParty.class,             new EnemyParty.Parser());
        addParser(GarbageCan.class,             new GarbageCan.Parser());
        addParser(Goal.class,                   new Goal.Parser());
        addParser(Hero.class,                   new Hero.Parser());
        addParser(Interaction.class,            new Interaction.Parser());
        addParser(Inventory.class,              new Inventory.Parser());
        addParser(Item.class,                   new Item.Parser());
        addParser(Location.class,               new Location.Parser());
        addParser(MainMapCharacter.class,       new MainMapCharacter.Parser());
        addParser(MainParty.class,              new MainParty.Parser());
        addParser(MapNpc.class,                 new MapNpc.Parser());
        addParser(NonRenderedQuestEntity.class, new NonRenderedQuestEntity.Parser());
        addParser(Plot.class,                   new Plot.Parser());
        addParser(Quest.class,                  new Quest.Parser());
        addParser(QuestNpc.class,               new QuestNpc.Parser());
        addParser(Settings.class,               new Settings.Parser());
        addParser(SpecialItem.class,            new SpecialItem.Parser());
        addParser(Spell.class,                  new Spell.Parser());
        addParser(Weapon.class,                 new Weapon.Parser());
        addParser(World.class,                  new World.Parser());
    }

    private static <U> void addParser(Class<U> clazz, SuperParser<U> parser) {
        parsers.put(clazz.getSimpleName(), parser);
    }

    protected static <U> SuperParser<U> getParser(Class<U> clazz) {
        return getParser(clazz.getSimpleName());
    }

    protected static <U> SuperParser<U> getParser(String className) {
        return parsers.get(className);
    }

    //--------------------------------
    //        Abstract methods
    //--------------------------------

    public abstract T fromJson(JsonElement element);

    public abstract JsonElement toJson(T object);

    //--------------------------------
    //  Static generic class methods
    //--------------------------------

    /**
     * Creates an object of the specified class from the element.
     * The returned object may actually be a subclass of clazz.
     * @param element
     * @param clazz
     * @param <U>
     * @return
     */
    public static <U> U fromJson(JsonElement element, Class<U> clazz) {
        SuperParser parser;
        if (clazz.isEnum()) {
            parser = getParser(clazz);
        } else {
            parser = getParser(getClass(element.getAsJsonObject()));
        }
        if (parser == null) {
            parser = getParser(clazz);
        }
        return clazz.cast(parser.fromJson(element));
    }

    /**
     * Creates a JSON of the element. The element must be of the specified
     * class, but the class does not otherwise affect the code. It is only
     * used to differentiate this method from the non-static equivalent.
     * @param element
     * @param clazz
     * @param <U>
     * @return
     */
    public static <U> JsonElement toJson(U element, Class<U> clazz) {
        if (clazz.isEnum()) {
            return getParser(clazz).toJson(element);
        } else {
            SuperParser parser = getParser(element.getClass());
            if (parser == null) {
                parser = getParser(clazz);
            }
            JsonObject json = parser.toJson(element).getAsJsonObject();
            addClass(json, element.getClass());
            return json;
        }
    }

    /**
     * Finds and parses a member of name name from the specified JSON string.
     * Will return null if the name is not found, or fail catastrophically
     * if the name's element is incorrectly formatted.
     * @param name
     * @param json
     * @return
     */
    public static <U> U parse(String name, String json, Class<U> clazz) {
        JsonElement element = getJsonHead(json);
        if (element == null || !element.isJsonObject()) {
            return null;
        }
        JsonObject object = element.getAsJsonObject();
        if (object.has(name)) {
            return fromJson(object.get(name), clazz);
        }
        return null;
    }

    /**
     * Finds and parses a member of name name from the specified FileHandle.
     * Will return null if the name is not found, or fail catastrophically
     * if the name's element is incorrectly formatted.
     * @param name
     * @param handle
     * @return
     */
    public static <U> U parse(String name, FileHandle handle, Class<U> clazz) {
        return parse(name, handle.readString(), clazz);
    }

    /**
     * Parses the JSON string as a whole as an object of clazz.
     * @param json
     * @param clazz
     * @param <U>
     * @return
     */
    public static <U> U parse(String json, Class<U> clazz) {
        return fromJson(getJsonHead(json), clazz);
    }

    /**
     * Parses the FileHandle as a whole as an object of clazz.
     * @param handle
     * @param clazz
     * @param <U>
     * @return
     */
    public static <U> U parse(FileHandle handle, Class<U> clazz) {
        return parse(handle.readString(), clazz);
    }

    /**
     * Parses all members in the specified JSON string as if they were of type T.
     * Will fail catastrophically if any member is incorrectly formatted.
     * @param json
     * @return
     */
    public static <U> Map<String, U> parseAll(String json, Class<U> clazz) {
        JsonElement element = getJsonHead(json);
        Map<String, U> parsables = new HashMap<String, U>();
        Set<Map.Entry<String, JsonElement>> entries = element.getAsJsonObject().entrySet();
        for (Map.Entry<String, JsonElement> entry : entries) {
            parsables.put(entry.getKey(), fromJson(entry.getValue(), clazz));
        }
        return parsables;
    }

    /**
     * Parses all members in the FileHandle as if they were of type T.
     * Will fail catastrophically if any member is incorrectly formatted.
     * @param handle
     * @return
     */
    public static <U> Map<String, U> parseAll(FileHandle handle, Class<U> clazz) {
        return parseAll(handle.readString(), clazz);
    }

    //--------------------------------
    //     Convenience methods
    //     for subclasses
    //--------------------------------

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
        return fromJson(json.get(name), clazz);
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

    protected static void addNull(JsonObject json, String name) {
        json.add(name, JsonNull.INSTANCE);
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

    private static JsonElement getJsonHead(String json) {
        return parser.parse(json);
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
