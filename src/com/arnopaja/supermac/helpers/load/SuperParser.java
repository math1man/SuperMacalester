package com.arnopaja.supermac.helpers.load;

import com.badlogic.gdx.files.FileHandle;
import com.google.gson.*;
import org.reflections.Reflections;

import java.util.*;

/**
 * Super class for all parsers.
 *
 * @author Ari Weiland
 */
public abstract class SuperParser<T extends Parsable> {

    private static final Gson gson = new Gson();
    private static final JsonParser parser = new JsonParser();

    private static final Reflections reflections = new Reflections("com.arnopaja.supermac");
    private static final Set<Class<? extends Parsable>> classes = reflections.getSubTypesOf(Parsable.class);
    private static final Map<Class<? extends Parsable>, SuperParser> parserCache = new HashMap<Class<? extends Parsable>, SuperParser>(classes.size());

    //--------------------------------
    //       Parser Retrieval
    //--------------------------------

    private static SuperParser getParser(JsonElement element, Class clazz) {
        if (element.isJsonObject()) {
            Class tryClass = getClass(element.getAsJsonObject());
            return getParser(tryClass, clazz);
        } else {
            return getParser(clazz);
        }
    }

    private static SuperParser getParser(Class tryClass, Class defClass) {
        SuperParser parser = getParser(tryClass);
        if (parser == null) {
            parser = getParser(defClass);
        }
        return parser;
    }

    private static SuperParser getParser(Class clazz) {
        if (!parserCache.containsKey(clazz)) {
            if (clazz.isEnum()) {
                parserCache.put(clazz, new EnumParser(clazz));
            } else try {
                parserCache.put(clazz, (SuperParser) Class.forName(clazz.getName() + "$Parser").newInstance());
            } catch (ClassNotFoundException e) {
                return null;
            } catch (InstantiationException e) {
                throw new RuntimeException(e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        return parserCache.get(clazz);
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
    public static <U extends Parsable> U fromJson(JsonElement element, Class<U> clazz) {
        SuperParser parser = getParser(element, clazz);
        return clazz.cast(parser.fromJson(element));
    }

    /**
     * Creates a JSON of the element. The element must be of the specified
     * class, but the class does not otherwise affect the code. It is only
     * used to differentiate this method from the non-static equivalent.
     * @param object
     * @param clazz
     * @param <U>
     * @return
     */
    public static <U extends Parsable> JsonElement toJson(U object, Class<U> clazz) {
        // TODO: get rid of this conditional
        if (clazz.isEnum()) {
            return getParser(clazz).toJson(object);
        } else {
            SuperParser parser = getParser(object.getClass(), clazz);
            JsonObject json = parser.toJson(object).getAsJsonObject();
            addClass(json, object.getClass());
            return json;
        }
    }

    public static <U extends Parsable> String toJsonString(U element, Class<U> clazz) {
        return gson.toJson(toJson(element, clazz));
    }

    /**
     * Finds and parses a member of name name from the specified JSON string.
     * Will return null if the name is not found, or fail catastrophically
     * if the name's element is incorrectly formatted.
     * @param name
     * @param json
     * @return
     */
    public static <U extends Parsable> U parse(String name, String json, Class<U> clazz) {
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
    public static <U extends Parsable> U parse(String name, FileHandle handle, Class<U> clazz) {
        return parse(name, handle.readString(), clazz);
    }

    /**
     * Parses the JSON string as a whole as an object of clazz.
     * @param json
     * @param clazz
     * @param <U>
     * @return
     */
    public static <U extends Parsable> U parse(String json, Class<U> clazz) {
        return fromJson(getJsonHead(json), clazz);
    }

    /**
     * Parses the FileHandle as a whole as an object of clazz.
     * @param handle
     * @param clazz
     * @param <U>
     * @return
     */
    public static <U extends Parsable> U parse(FileHandle handle, Class<U> clazz) {
        return parse(handle.readString(), clazz);
    }

    /**
     * Parses all members in the specified JSON string as if they were of type T.
     * Will fail catastrophically if any member is incorrectly formatted.
     * @param json
     * @return
     */
    public static <U extends Parsable> Map<String, U> parseAll(String json, Class<U> clazz) {
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
    public static <U extends Parsable> Map<String, U> parseAll(FileHandle handle, Class<U> clazz) {
        return parseAll(handle.readString(), clazz);
    }

    //--------------------------------
    //     Convenience methods
    //     for subclasses
    //--------------------------------

    protected static boolean getBoolean(JsonObject json, String name) {
        return json.getAsJsonPrimitive(name).getAsBoolean();
    }

    protected static boolean getBoolean(JsonObject json, String name, boolean defval) {
        if (json.has(name)) {
            return getBoolean(json, name);
        }
        return defval;
    }

    protected static void addBoolean(JsonObject json, String name, boolean b) {
        json.addProperty(name, b);
    }

    protected static int getInt(JsonObject json, String name) {
        return json.getAsJsonPrimitive(name).getAsInt();
    }

    protected static int getInt(JsonObject json, String name, int defval) {
        if (json.has(name)) {
            return getInt(json, name);
        }
        return defval;
    }

    protected static void addInt(JsonObject json, String name, int i) {
        json.addProperty(name, i);
    }

    protected static float getFloat(JsonObject json, String name) {
        return json.getAsJsonPrimitive(name).getAsFloat();
    }

    protected static float getFloat(JsonObject json, String name, float defval) {
        if (json.has(name)) {
            return getFloat(json, name);
        }
        return defval;
    }

    protected static void addFloat(JsonObject json, String name, float f) {
        json.addProperty(name, f);
    }

    protected static String getString(JsonObject json, String name) {
        return json.getAsJsonPrimitive(name).getAsString();
    }

    protected static String getString(JsonObject json, String name, String defval) {
        if (json.has(name)) {
            return getString(json, name);
        }
        return defval;
    }

    protected static void addString(JsonObject json, String name, String string) {
        json.addProperty(name, string);
    }

    protected static <U extends Parsable> U getObject(JsonObject json, Class<U> clazz) {
        return getObject(json, clazz.getSimpleName().toLowerCase(), clazz);
    }

    protected static <U extends Parsable> U getObject(JsonObject json, Class<U> clazz, U defval) {
        return getObject(json, clazz.getSimpleName().toLowerCase(), clazz, defval);
    }

    protected static <U extends Parsable> U getObject(JsonObject json, String name, Class<U> clazz) {
        return fromJson(json.get(name), clazz);
    }

    protected static <U extends Parsable> U getObject(JsonObject json, String name, Class<U> clazz, U defval) {
        if (json.has(name)) {
            return getObject(json, name, clazz);
        }
        return defval;
    }

    protected static <U extends Parsable> void addObject(JsonObject json, U object, Class<U> clazz) {
        addObject(json, clazz.getSimpleName().toLowerCase(), object, clazz);
    }

    protected static <U extends Parsable> void addObject(JsonObject json, String name, U object, Class<U> clazz) {
        json.add(name, toJson(object, clazz));
    }

    protected static <U extends Parsable> List<U> getList(JsonObject json, String name, Class<U> clazz) {
        JsonArray array = json.getAsJsonArray(name);
        List<U> contents = new ArrayList<U>();
        for (JsonElement e : array) {
            contents.add(fromJson(e, clazz));
        }
        return contents;
    }

    protected static <U extends Parsable> void addList(JsonObject json, String name, Collection<U> list, Class<U> clazz) {
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

    protected static Class<?> getClass(JsonObject json) {
        String className = json.getAsJsonPrimitive("class").getAsString();
        for (Class clazz : classes) {
            if (clazz.getSimpleName().equals(className)) {
                return clazz;
            }
        }
        return null;
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

    public static class EnumParser<T extends Enum & Parsable> extends SuperParser<T> {

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
