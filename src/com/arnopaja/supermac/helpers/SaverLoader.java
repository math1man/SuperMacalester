package com.arnopaja.supermac.helpers;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

/**
 * @author Ari Weiland
 */
public class SaverLoader {

    public static final Gson gson = new Gson();
    public static final JsonParser parser = new JsonParser();

    public static <T> void save(T savable, Class<T> clazz) {
        save(savable, clazz.getName(), clazz);
    }

    public static <T> void save(T savable, String name, Class<T> clazz) {
        AssetLoader.prefs.putString(name, gson.toJson(SuperParser.toJson(savable, clazz)));
    }

    public static <T> T load(Class<T> clazz) {
        return load(clazz.getName(), clazz);
    }

    public static <T> T load(String name, Class<T> clazz) {
        if (AssetLoader.prefs.contains(name)) {
            JsonElement me = parser.parse(AssetLoader.prefs.getString(name));
            return SuperParser.fromJson(me, clazz);
        }
        return null;
    }

    public static void flush() {
        AssetLoader.prefs.flush();
    }
}
