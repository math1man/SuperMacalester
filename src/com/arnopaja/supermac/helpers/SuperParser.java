package com.arnopaja.supermac.helpers;

import com.badlogic.gdx.files.FileHandle;
import com.google.gson.*;
import org.reflections.Reflections;

import java.util.*;

/**
 * Superclass for all parsers.
 * Subclass parsers should never be accessed individually though.
 * Instead, parsing should be run through the static methods in this class.
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
    //  Static Generic-Class methods
    //--------------------------------

    /**
     * Creates an object of the specified class from the element.
     * The returned object may actually be a subclass of clazz.
     * @param element
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T extends Parsable> T fromJson(JsonElement element, Class<T> clazz) {
        SuperParser parser = getParser(element, clazz);
        return clazz.cast(parser.fromJson(element));
    }

    /**
     * Creates a JSON of the element. The element must be of the specified
     * class, but the class does not otherwise affect the code. It is only
     * used to differentiate this method from the non-static equivalent.
     * @param object
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T extends Parsable> JsonElement toJson(T object, Class<T> clazz) {
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

    /**
     * Converts the object to a JSON string, based on the toJson method
     * @param object
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T extends Parsable> String toJsonString(T object, Class<T> clazz) {
        return gson.toJson(toJson(object, clazz));
    }

    /**
     * Finds and parses a member of name name from the specified JSON string.
     * Will return null if the name is not found, or fail catastrophically
     * if the name's element is incorrectly formatted.
     * @param name
     * @param json
     * @return
     */
    public static <T extends Parsable> T parse(String name, String json, Class<T> clazz) {
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
    public static <T extends Parsable> T parse(String name, FileHandle handle, Class<T> clazz) {
        return parse(name, handle.readString(), clazz);
    }

    /**
     * Parses the JSON string as a whole as an object of clazz.
     * @param json
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T extends Parsable> T parse(String json, Class<T> clazz) {
        return fromJson(getJsonHead(json), clazz);
    }

    /**
     * Parses the FileHandle as a whole as an object of clazz.
     * @param handle
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T extends Parsable> T parse(FileHandle handle, Class<T> clazz) {
        return parse(handle.readString(), clazz);
    }

    /**
     * Parses all members in the specified JSON string as if they were of type T.
     * Will fail catastrophically if any member is incorrectly formatted.
     * @param json
     * @return
     */
    public static <T extends Parsable> Map<String, T> parseAll(String json, Class<T> clazz) {
        JsonElement element = getJsonHead(json);
        Map<String, T> parsables = new HashMap<String, T>();
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
    public static <T extends Parsable> Map<String, T> parseAll(FileHandle handle, Class<T> clazz) {
        return parseAll(handle.readString(), clazz);
    }

    //--------------------------------
    //     Convenience methods
    //--------------------------------

    public static boolean getBoolean(JsonObject json, String name) {
        return json.getAsJsonPrimitive(name).getAsBoolean();
    }

    public static boolean getBoolean(JsonObject json, String name, boolean defval) {
        if (json.has(name)) {
            return getBoolean(json, name);
        }
        return defval;
    }

    public static void addBoolean(JsonObject json, String name, boolean b) {
        json.addProperty(name, b);
    }

    public static int getInt(JsonObject json, String name) {
        return json.getAsJsonPrimitive(name).getAsInt();
    }

    public static int getInt(JsonObject json, String name, int defval) {
        if (json.has(name)) {
            return getInt(json, name);
        }
        return defval;
    }

    public static void addInt(JsonObject json, String name, int i) {
        json.addProperty(name, i);
    }

    public static float getFloat(JsonObject json, String name) {
        return json.getAsJsonPrimitive(name).getAsFloat();
    }

    public static float getFloat(JsonObject json, String name, float defval) {
        if (json.has(name)) {
            return getFloat(json, name);
        }
        return defval;
    }

    public static void addFloat(JsonObject json, String name, float f) {
        json.addProperty(name, f);
    }

    public static String getString(JsonObject json, String name) {
        return json.getAsJsonPrimitive(name).getAsString();
    }

    public static String getString(JsonObject json, String name, String defval) {
        if (json.has(name)) {
            return getString(json, name);
        }
        return defval;
    }

    public static void addString(JsonObject json, String name, String string) {
        json.addProperty(name, string);
    }

    public static <T extends Parsable> T getObject(JsonObject json, Class<T> clazz) {
        return getObject(json, clazz.getSimpleName().toLowerCase(), clazz);
    }

    public static <T extends Parsable> T getObject(JsonObject json, Class<T> clazz, T defval) {
        return getObject(json, clazz.getSimpleName().toLowerCase(), clazz, defval);
    }

    public static <T extends Parsable> T getObject(JsonObject json, String name, Class<T> clazz) {
        return fromJson(json.get(name), clazz);
    }

    public static <T extends Parsable> T getObject(JsonObject json, String name, Class<T> clazz, T defval) {
        if (json.has(name)) {
            return getObject(json, name, clazz);
        }
        return defval;
    }

    public static <T extends Parsable> void addObject(JsonObject json, T object, Class<T> clazz) {
        addObject(json, clazz.getSimpleName().toLowerCase(), object, clazz);
    }

    public static <T extends Parsable> void addObject(JsonObject json, String name, T object, Class<T> clazz) {
        json.add(name, toJson(object, clazz));
    }

    public static <T extends Parsable> List<T> getList(JsonObject json, String name, Class<T> clazz) {
        JsonArray array = json.getAsJsonArray(name);
        List<T> contents = new ArrayList<T>();
        for (JsonElement e : array) {
            contents.add(fromJson(e, clazz));
        }
        return contents;
    }

    public static <T extends Parsable> void addList(JsonObject json, String name, Collection<T> list, Class<T> clazz) {
        JsonArray array = new JsonArray();
        for (T element : list) {
            array.add(toJson(element, clazz));
        }
        json.add(name, array);
    }

    public static void addNull(JsonObject json, String name) {
        json.add(name, JsonNull.INSTANCE);
    }

    public static boolean has(JsonObject json, Class clazz) {
        return json.has(clazz.getSimpleName().toLowerCase());
    }

    public static Class<?> getClass(JsonObject json) {
        String className = json.getAsJsonPrimitive("class").getAsString();
        for (Class clazz : classes) {
            if (clazz.getSimpleName().equals(className)) {
                return clazz;
            }
        }
        return null;
    }

    public static void addClass(JsonObject json, Class clazz) {
        json.addProperty("class", clazz.getSimpleName());
    }

    public static JsonElement getJsonHead(String json) {
        return parser.parse(json);
    }

    /**
     * A parser specifically designed to parser enums
     * @param <T> a parsable enum class
     */
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
