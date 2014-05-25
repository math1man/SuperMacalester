package com.arnopaja.supermac.inventory;

import java.util.*;

/**
 * TODO: handle self vs other
 * @author Ari Weiland
 */
public class Powerup {

    private final EnumMap<Effect.Type, Integer> effects = new EnumMap<Effect.Type, Integer>(Effect.Type.class);
    private boolean isResurrect = false;

    /**
     * Returns a 2-array of Powerups. The first powerup contains effects applied to self,
     * while the second powerup contains effects applied to other.
     * @param effects
     * @return
     */
    public static Powerup[] getPowerups(Collection<Effect> effects) {
        Powerup[] powerups = { new Powerup(), new Powerup() };
        for (Effect e : effects) {
            if (e.self) {
                powerups[0].add(e);
            } else {
                powerups[1].add(e);
            }
        }
        return powerups;
    }

    public Powerup() {}

    public Powerup(Collection<Effect> effects) {
        addAll(effects);
    }

    /**
     * Returns the net value of the effect type
     * @param type
     * @return
     */
    public int getValue(Effect.Type type) {
        return effects.containsKey(type) ? effects.get(type) : 0;
    }

    public boolean has(Effect.Type type) {
        clean(type);
        return effects.containsKey(type);
    }

    public boolean isResurrect() {
        return isResurrect;
    }

    public boolean isStatus() {
        return has(Effect.Type.ATTACK) || has(Effect.Type.DEFENSE) || has(Effect.Type.SPECIAL) || has(Effect.Type.SPEED);
    }

    public String statusDialogue() {
        String dialogue = "";
        if (has(Effect.Type.ATTACK)) {
            dialogue += "Attack modified by " + getValue(Effect.Type.ATTACK) + "!\n";
        }
        if (has(Effect.Type.DEFENSE)) {
            dialogue += "Defense modified by " + getValue(Effect.Type.DEFENSE) + "!\n";
        }
        if (has(Effect.Type.SPECIAL)) {
            dialogue += "Special modified by " + getValue(Effect.Type.SPECIAL) + "!\n";
        }
        if (has(Effect.Type.SPEED)) {
            dialogue += "Speed modified by " + getValue(Effect.Type.SPEED) + "!\n";
        }
        return dialogue;
    }

    public boolean isEmpty() {
        for (Effect.Type type : effects.keySet()) {
            clean(type);
        }
        return effects.isEmpty();
    }

    public void add(Effect effect) {
        if (effect.isResurrect()) {
            isResurrect = true;
        } else {
            int value = getValue(effect.type) + effect.value;
            effects.put(effect.type, value);
        }
    }

    public void addAll(Collection<? extends Effect> c) {
        for (Effect e : c) {
            add(e);
        }
    }

    public void combine(Powerup other) {
        this.isResurrect = this.isResurrect || other.isResurrect;
        for (Effect.Type type : other.effects.keySet()) {
            int value = this.getValue(type) + other.getValue(type);
            this.effects.put(type, value);
        }
    }

    public void clear() {
        effects.clear();
    }

    private void clean(Effect.Type type) {
        if (effects.get(type) == 0) effects.remove(type); // remove types whose value is 0
    }
}
