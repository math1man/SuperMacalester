package com.arnopaja.supermac.plot;

import com.arnopaja.supermac.helpers.Savable;
import com.google.gson.JsonElement;

/**
 * This will be a static class that handles game settings
 * It maybe also should handle game resolution
 *
 * @author Ari Weiland
 */
public class Settings implements Savable {

    @Override
    public JsonElement toJson() {
        return null;
    }

    @Override
    public void fromJson(JsonElement element) {

    }
}
