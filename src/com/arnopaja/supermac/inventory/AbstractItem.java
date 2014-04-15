package com.arnopaja.supermac.inventory;

import com.arnopaja.supermac.helpers.SuperParser;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Ari Weiland
 */
public abstract class AbstractItem {

    private final int id;
    private final String name;
    private final int value;

    protected AbstractItem(int id, String name, int value) {
        this.id = id;
        this.name = name;
        this.value = value;
        cache.put(id, this);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getValue() {
        return value;
    }

    @Override
    public String toString() {
        return getName();
    }

    // Cache of all AbstractItems
    private static final Map<Integer, AbstractItem> cache = new HashMap<Integer, AbstractItem>();

    public static boolean isCached(int id) {
        return cache.containsKey(id);
    }

    public static AbstractItem getCached(int id) {
        return cache.get(id);
    }

    public static <T extends AbstractItem> T getCached(int id, Class<T> clazz) {
        AbstractItem item = getCached(id);
        if (item.getClass() == clazz) {
            return clazz.cast(item);
        }
        return null;
    }

    public static class Parser<T extends AbstractItem> extends SuperParser<T> {

        private static final Map<String, Parser> parsers = new HashMap<String, Parser>();

        static {
            parsers.put(Armor.class.getSimpleName(), new Armor.Parser());
            parsers.put(Item.class.getSimpleName(), new Item.Parser());
            parsers.put(SpecialItem.class.getSimpleName(), new SpecialItem.Parser());
            parsers.put(Weapon.class.getSimpleName(), new Weapon.Parser());
        }


        @Override
        public T fromJson(JsonElement element) {
            JsonObject object = element.getAsJsonObject();
            int id = object.getAsJsonPrimitive("id").getAsInt();
            if (isCached(id)) {
                return (T) getCached(id);
            } else {
                String className = getClass(object);
                Parser<T> parser = parsers.get(className);
                return parser.fromJson(element);
            }
        }

        @Override
        public JsonElement toJson(T object) {
            Parser<T> parser = parsers.get(object.getClass().getSimpleName());
            return parser.toJson(object);
        }

        protected JsonObject toBaseJson(T object) {
            JsonObject json = new JsonObject();
            addInt(json, "id", object.getId());
            addString(json, "name", object.getName());
            addInt(json, "value", object.getValue());
            addClass(json, object);
            return json;
        }
    }
}
