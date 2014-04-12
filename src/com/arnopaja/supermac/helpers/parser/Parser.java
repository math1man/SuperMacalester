package com.arnopaja.supermac.helpers.parser;

import com.badlogic.gdx.files.FileHandle;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * @author Ari Weiland
 */
public abstract class Parser<T> {

    public static JsonParser parser = new JsonParser();

    public abstract T parse(JsonObject object);

    public T parse(String name, FileHandle handle) {
        return parse(getJson(name, handle));
    }

    public static JsonObject getJson(String name, FileHandle handle) {
        String json = handle.readString();
        JsonElement element = parser.parse(json);
        if (element.isJsonObject()) {
            return element.getAsJsonObject().getAsJsonObject(name);
        }
        return null;
    }
}
