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

    public static <T extends Savable> void save(T savable) {
        AssetLoader.prefs.putString(savable.getClass().getName(), gson.toJson(savable.toJson()));
        AssetLoader.prefs.flush();
    }

    public static <T extends Savable> void load(T savable) {
        JsonElement me = parser.parse(AssetLoader.prefs.getString(savable.getClass().getName()));
        savable.fromJson(me.getAsJsonObject());
    }
}
