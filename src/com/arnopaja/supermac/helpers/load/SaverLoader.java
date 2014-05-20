package com.arnopaja.supermac.helpers.load;

import com.arnopaja.supermac.helpers.Parsable;
import com.arnopaja.supermac.helpers.SuperParser;

/**
 * @author Ari Weiland
 */
public class SaverLoader {

    public static <T extends Parsable> void save(T savable, Class<T> clazz) {
        save(savable, clazz.getName(), clazz);
    }

    public static <T extends Parsable> void save(T savable, String name, Class<T> clazz) {
        AssetLoader.getPrefs().putString(name, SuperParser.toJsonString(savable, clazz));
    }

    public static <T extends Parsable> T load(Class<T> clazz) {
        return load(clazz, null);
    }

    public static <T extends Parsable> T load(Class<T> clazz, T defVal) {
        return load(clazz.getName(), clazz, defVal);
    }

    public static <T extends Parsable> T load(String name, Class<T> clazz) {
        return load(name, clazz, null);
    }

    public static <T extends Parsable> T load(String name, Class<T> clazz, T defVal) {
        if (isSaved(name)) {
            return SuperParser.parse(AssetLoader.getPrefs().getString(name), clazz);
        } else if (defVal != null) {
            save(defVal, name, clazz);
        }
        return defVal;
    }

    public static boolean isSaved(Class clazz) {
        return isSaved(clazz.getName());
    }

    public static boolean isSaved(String name) {
        return AssetLoader.getPrefs().contains(name);
    }

    public static void flush() {
        AssetLoader.getPrefs().flush();
    }
}
