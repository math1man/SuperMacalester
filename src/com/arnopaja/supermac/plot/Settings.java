package com.arnopaja.supermac.plot;

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
