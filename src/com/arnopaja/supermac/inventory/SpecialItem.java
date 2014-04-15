package com.arnopaja.supermac.inventory;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * Created by Nolan on 2/16/14.
 */
public class SpecialItem extends AbstractItem {

    // TODO: image definition

    public SpecialItem(int id, String name) {
        super(id, name, 0);
    }

    public static class Parser extends AbstractItem.Parser<SpecialItem> {
        @Override
        public SpecialItem convert(JsonElement element) {
            JsonObject object = element.getAsJsonObject();
            int id = object.getAsJsonPrimitive("id").getAsInt();
            String name = object.getAsJsonPrimitive("name").getAsString();
            return new SpecialItem(id, name);
        }
    }
}
