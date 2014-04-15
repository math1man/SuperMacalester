package com.arnopaja.supermac.plot;

import com.arnopaja.supermac.helpers.SaverLoader;
import com.arnopaja.supermac.helpers.SuperParser;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * This will be a static class that handles game settings
 * It maybe also should handle game resolution
 *
 * @author Ari Weiland
 */
public class Settings {

    private static Settings settings = new Settings();

    public static void save() {
        save(false);
    }

    public static void save(boolean flush) {
        SaverLoader.save(settings, Settings.class);
        if (flush) {
            SaverLoader.flush();
        }
    }

    public static void load() {
        settings = SaverLoader.load(Settings.class, new Settings());
    }

    public static class Parser extends SuperParser<Settings> {
        @Override
        public Settings fromJson(JsonElement element) {
            return new Settings();
        }

        @Override
        public JsonElement toJson(Settings object) {
            return new JsonObject();
        }
    }
}
