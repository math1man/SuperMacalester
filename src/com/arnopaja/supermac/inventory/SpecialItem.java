package com.arnopaja.supermac.inventory;

import com.arnopaja.supermac.helpers.SuperParser;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * @author Nolan Varani
 */
public class SpecialItem extends GenericItem {

    // TODO: image definition

    protected SpecialItem(int id, String name) {
        super(id, name, 0);
    }

    public static class Parser extends SuperParser<SpecialItem> {
        @Override
        public SpecialItem fromJson(JsonElement element) {
            JsonObject object = element.getAsJsonObject();
            int id = getInt(object, "id");
            String name = getString(object, "name");
            return new SpecialItem(id, name);
        }

        @Override
        public JsonElement toJson(SpecialItem object) {
            JsonObject json = new JsonObject();
            addInt(json, "id", object.getId());
            addString(json, "name", object.getName());
            return json;
        }
    }
}
