package com.arnopaja.supermac.plot;

import com.arnopaja.supermac.helpers.load.AssetLoader;
import com.arnopaja.supermac.helpers.load.SaverLoader;
import com.arnopaja.supermac.helpers.load.SuperParser;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * This will be a static class that handles game settings
 * It maybe also should handle game resolution
 *
 * @author Ari Weiland
 */
public class Settings {

    private static Settings INSTANCE = new Settings();

    private float volume = 1.0f;
    private boolean isClean = true;

    private Settings() {}

    public static float getVolume() {
        return INSTANCE.volume;
    }

    public static void setVolume(float volume) {
        INSTANCE.volume = volume;
        AssetLoader.worldMusic.setVolume(volume);
        AssetLoader.battleMusic.setVolume(volume);
    }

    public static boolean isClean() {
        return INSTANCE.isClean;
    }

    public static void setClean(boolean clean) {
        INSTANCE.isClean = clean;
    }

    public static void save() {
        save(false);
    }

    public static void save(boolean flush) {
        SaverLoader.save(INSTANCE, Settings.class);
        if (flush) {
            SaverLoader.flush();
        }
    }

    public static void load() {
        INSTANCE = SaverLoader.load(Settings.class, INSTANCE);
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
