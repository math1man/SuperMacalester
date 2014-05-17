package com.arnopaja.supermac.inventory;

import com.arnopaja.supermac.GameScreen;
import com.arnopaja.supermac.helpers.dialogue.DialogueStyle;
import com.arnopaja.supermac.helpers.dialogue.DialogueText;
import com.arnopaja.supermac.helpers.interaction.Interaction;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Ari Weiland
 */
public abstract class GenericItem implements Interaction {

    private final int id;
    private final String name;
    private final int value;

    protected GenericItem(int id, String name, int value) {
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

    @Override
    public void run(GameScreen screen) {
        Inventory.getMain().store(this);
        new DialogueText(this + " has been added to your inventory!", DialogueStyle.WORLD).run(screen);
    }

    //-----------------------------
    //     Cache and Methods
    //-----------------------------

    private static final Map<Integer, GenericItem> cache = new HashMap<Integer, GenericItem>();

    /**
     * Returns whether the specified ID is in the cache
     * @param id
     * @return
     */
    public static boolean isCached(int id) {
        return cache.containsKey(id);
    }

    /**
     * Returns true only if the specified ID is in the cache and is of class clazz
     * @param id
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T extends GenericItem> boolean isCached(int id, Class<T> clazz) {
        return isCached(id) && getCached(id).getClass() == clazz;
    }

    /**
     * Retrieves the AbstractItem of the specified ID from the cache
     * @param id
     * @return
     */
    public static GenericItem getCached(int id) {
        return cache.get(id);
    }

    /**
     * Retrives the item of the specified ID from the cache as an
     * instance of clazz, but only if it is of class clazz.
     * @param id
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T extends GenericItem> T getCached(int id, Class<T> clazz) {
        GenericItem item = getCached(id);
        if (item.getClass() == clazz) {
            return clazz.cast(item);
        }
        return null;
    }

    protected static void cache(GenericItem item) {
        cache.put(item.getId(), item);
    }
}
