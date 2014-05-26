package com.arnopaja.supermac.battle.characters;

import com.arnopaja.supermac.helpers.SuperParser;
import com.arnopaja.supermac.helpers.load.AssetLoader;
import com.arnopaja.supermac.helpers.Parsable;
import com.badlogic.gdx.audio.Sound;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Nolan Varani
 */
public class BattleClass implements Parsable {
                                                                                 // hp  m   a   d   s   speed
    public static final BattleClass COMP_SCI   = new BattleClass("Comp Sci", SpecialType.BLACK, 30, 10, 5,  10, 25, 15, "comp sci magic");
    public static final BattleClass ECON       = new BattleClass("Econ", SpecialType.WHITE, 40, 5,  20, 15, 10, 5,  "healing");
    public static final BattleClass NAT_SCI    = new BattleClass("Nat Sci", SpecialType.RED,   35, 5,  10, 10, 15, 10, "nat sci magic");
    public static final BattleClass HUMANITIES = new BattleClass("Humanities", SpecialType.NONE,  30, 0,  20, 10, 5,  20, null);

    public static enum SpecialType implements Parsable { BLACK, WHITE, RED, NONE }

    private final String name;
    private final SpecialType specialType;
    private final int baseMaxHealth, baseMaxMana;
    private final int baseAttack, baseDefense, baseSpecial, baseSpeed;
    private final String magicSound;

    protected BattleClass(String name, SpecialType specialType, int baseMaxHealth, int baseMaxMana,
                          int baseAttack, int baseDefense, int baseSpecial, int baseSpeed, String magicSound) {
        this.name = name;
        this.specialType = specialType;
        this.baseMaxHealth = baseMaxHealth;
        this.baseMaxMana = baseMaxMana;
        this.baseAttack = baseAttack;
        this.baseDefense = baseDefense;
        this.baseSpecial = baseSpecial;
        this.baseSpeed = baseSpeed;
        this.magicSound = magicSound;
    }

    public String getName() {
        return name;
    }

    public SpecialType getSpecialType() {
        return specialType;
    }

    public int getBaseMaxHealth() {
        return baseMaxHealth;
    }

    public int getBaseMaxMana() {
        return baseMaxMana;
    }

    public int getBaseAttack() {
        return baseAttack;
    }

    public int getBaseDefense() {
        return baseDefense;
    }

    public int getBaseSpecial() {
        return baseSpecial;
    }

    public int getBaseSpeed() {
        return baseSpeed;
    }

    public int getVitality() {
        return (int) Math.round(baseMaxHealth / 8.0);
    }

    public int getIntelligence() {
        return (int) Math.round(baseMaxMana / 8.0);
    }

    public Sound getMagicSound() {
        return AssetLoader.getSound(magicSound);
    }

    private static final Map<String, BattleClass> cache = new HashMap<String, BattleClass>();

    /**
     * Returns whether the specified name is in the cache
     * @param name
     * @return
     */
    public static boolean isCached(String name) {
        return cache.containsKey(name);
    }

    /**
     * Retrieves the AbstractItem of the specified name from the cache
     * @param name
     * @return
     */
    public static BattleClass getCached(String name) {
        return cache.get(name);
    }

    protected static void cache(BattleClass battleClass) {
        cache.put(battleClass.name, battleClass);
    }

    public static class Parser extends SuperParser<BattleClass> {
        @Override
        public BattleClass fromJson(JsonElement element) {
            JsonObject object = element.getAsJsonObject();
            String name = getString(object, "name");
            if (isCached(name)) {
                return getCached(name);
            } else {
                SpecialType type = getObject(object, "special type", SpecialType.class);
                int hp = getInt(object, "hp");
                int mana = getInt(object, "mana");
                int attack = getInt(object, "attack");
                int defense = getInt(object, "defense");
                int special = getInt(object, "special");
                int speed = getInt(object, "speed");
                String sound = getString(object, "sound", null);
                BattleClass battleClass = new BattleClass(name, type, hp, mana, attack, defense, special, speed, sound);
                cache(battleClass);
                return battleClass;
            }
        }

        @Override
        public JsonElement toJson(BattleClass object) {
            JsonObject json = new JsonObject();
            addString(json, "name", object.name);
            addObject(json, "special type", object.specialType, SpecialType.class);
            addInt(json, "hp", object.baseMaxHealth);
            addInt(json, "mana", object.baseMaxMana);
            addInt(json, "attack", object.baseAttack);
            addInt(json, "defense", object.baseDefense);
            addInt(json, "special", object.baseSpecial);
            addInt(json, "speed", object.baseSpeed);
            addString(json, "sound", object.magicSound);
            return json;
        }
    }
}
