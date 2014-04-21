package com.arnopaja.supermac.inventory;

import com.arnopaja.supermac.helpers.SaverLoader;
import com.arnopaja.supermac.helpers.SuperParser;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.*;

/**
 * I compromised on the static nature of this class:
 * The class is inherently non static.  However, the class also
 * maintains a static instance of itself, and provides appropriate
 * static methods for accessing that instance. Thus, we can use
 * inventories wherever, while still maintaining a main static
 * instance for the main party usable throughout the game.
 *
 * For saving and loading the static instance, the specific
 * Inventory.save() and Inventory.load() methods must be used.
 *
 * @author Nolan Varani
 */
public class Inventory {

    private Map<Integer, Integer> idMap = new LinkedHashMap<Integer, Integer>();

    public void store(int id) {
        store(id, 1);
    }

    public void store(GenericItem item) {
        store(item.getId());
    }

    public void store(int id, int amount) {
        int count = amount;
        if (idMap.containsKey(id)) {
            count += idMap.get(id);
        }
        idMap.put(id, count);
    }

    public void store(GenericItem item, int amount) {
        store(item.getId(), amount);
    }

    /**
     * Removes one of the item with the given ID
     * @param id
     * @return true only if an item was removed, else false
     */
    public boolean take(int id) {
        return take(id, 1) == 1;
    }

    /**
     * Removes one of the specified item
     * @param item
     * @return true only if an item was removed, else false
     */
    public boolean take(GenericItem item) {
        return take(item.getId());
    }

    /**
     * Removes the specified amount of the item with the given ID.
     * If there are fewer of the item in the inventory, it removes all of them.
     * @param id
     * @param amount
     * @return the amount removed
     */
    public int take(int id, int amount) {
        for (int i : idMap.keySet()) {
            if (i == id) {
                int count = idMap.get(id);
                if (count < amount) {
                    idMap.remove(id);
                    return count;
                } else {
                    idMap.remove(id);
                    idMap.put(id, count - amount);
                    return amount;
                }
            }
        }
        return 0;
    }

    /**
     * Removes the specified amount of the specified item.
     * If there are fewer of the item in the inventory, it removes all of them.
     * @param item
     * @param amount
     * @return the amount removed
     */
    public int take(GenericItem item, int amount) {
        return take(item.getId(), amount);
    }

    public int takeAll(int id) {
        for (int i : idMap.keySet()) {
            if (i == id) {
                int count = idMap.get(id);
                idMap.remove(id);
                return count;
            }
        }
        return 0;
    }

    public int takeAll(GenericItem item) {
        return takeAll(item.getId());
    }

    /**
     * Removes an item of the specified ID and class.
     * Returns an instance of the item specified, or null if the item
     * either was not in the inventory or the ID and class do not match
     * @param id
     * @param clazz
     * @return an instance of the item specified, or null
     */
    public <T extends GenericItem> T take(int id, Class<T> clazz) {
        if (GenericItem.isCached(id, clazz) && take(id)) { // make sure the ID matches the class and
            return GenericItem.getCached(id, clazz);         // make sure the ID is in the inventory
        }
        return null;
    }

    /**
     * Removes the specified item. Returns an instance of the item
     * specified, or null if the item either was not in the inventory.
     * @param item
     * @param clazz
     * @return an instance of the item specified, or null
     */
    public <T extends GenericItem> T take(T item, Class<T> clazz) {
        return take(item.getId(), clazz);
    }

    /**
     * Returns a list of all items in the inventory that match the class.
     * Note that this method does NOT remove items from the inventory.
     * @param clazz
     * @return
     */
    public <T extends GenericItem> List<T> getAll(Class<T> clazz) {
        List<T> items = new ArrayList<T>();
        for (int id : idMap.keySet()) {
            if (GenericItem.isCached(id, clazz)) {
                items.add(GenericItem.getCached(id, clazz));
            }
        }
        return Collections.unmodifiableList(items);
    }

    public List<GenericItem> getAll() {
        List<GenericItem> items = new ArrayList<GenericItem>();
        for (int id : idMap.keySet()) {
            if (GenericItem.isCached(id)) {
                items.add(GenericItem.getCached(id));
            }
        }
        return Collections.unmodifiableList(items);
    }

    public boolean contains(int id) {
        return amount(id) != 0;
    }

    public boolean contains(GenericItem item) {
        return contains(item.getId());
    }

    public int amount(int id) {
        if (idMap.containsKey(id)) {
            return idMap.get(id);
        } else {
            return 0;
        }
    }

    public int amount(GenericItem item) {
        return amount(item.getId());
    }

    public int amount() {
        int count = 0;
        for (int i : idMap.values()) {
            count += i;
        }
        return count;
    }

    public int size() {
        return idMap.size();
    }

    public boolean isEmpty() {
        return idMap.isEmpty();
    }

    //-------------------------
    //    Static members
    //-------------------------

    private static Inventory main = new Inventory();

    public static Inventory getMain() {
        return main;
    }

    public static void save() {
        save(false);
    }

    public static void save(boolean flush) {
        SaverLoader.save(main, Inventory.class);
        if (flush) {
            SaverLoader.flush();
        }
    }

    public static void load() {
        main = SaverLoader.load(Inventory.class, new Inventory());
    }

    public static class Parser extends SuperParser<Inventory> {
        @Override
        public Inventory fromJson(JsonElement element) {
            JsonArray array = element.getAsJsonArray();
            Inventory inventory = new Inventory();
            for (JsonElement e : array) {
                JsonObject o = e.getAsJsonObject();
                inventory.store(getInt(o, "id"), getInt(o, "amount"));
            }
            return inventory;
        }

        @Override
        public JsonElement toJson(Inventory object) {
            JsonArray json = new JsonArray();
            for (Map.Entry<Integer, Integer> entry : object.idMap.entrySet()) {
                JsonObject o = new JsonObject();
                addInt(o, "id", entry.getKey());
                addInt(o, "amount", entry.getValue());
                json.add(o);
            }
            return json;
        }
    }
}
