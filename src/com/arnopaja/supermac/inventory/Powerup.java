package com.arnopaja.supermac.inventory;

import com.arnopaja.supermac.battle.characters.BattleCharacter;

import java.util.*;

/**
 * @author Ari Weiland
 */
public class Powerup {

    private final boolean self;
    private final EnumMap<Effect.Type, Integer> effects = new EnumMap<Effect.Type, Integer>(Effect.Type.class);
    private boolean isResurrect = false;

    public Powerup(boolean self) {
        this.self = self;
    }

    public Powerup(boolean self, Collection<Effect> effects) {
        this(self);
        addAll(effects);
    }

    /**
     * Returns the net value of the effect type
     * @param type
     * @return
     */
    public int getValue(Effect.Type type) {
        return has(type) ? effects.get(type) : 0;
    }

    public boolean has(Effect.Type type) {
        clean(type);
        return effects.containsKey(type);
    }

    public boolean isSelf() {
        return self;
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
        if (effect.self == self) {
            if (effect.isResurrect()) {
                isResurrect = true;
            } else {
                int value = getValue(effect.type) + effect.value;
                effects.put(effect.type, value);
            }
        }
    }

    public void addAll(Collection<Effect> c) {
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

    public String applyTo(BattleCharacter character) {
        StringBuilder dialogue = new StringBuilder();
        if (isResurrect() && character.isFainted()) {
            character.resurrect();
            dialogue.append("\n").append(character).append(" has been resurrected!");
        }
        if (!character.isFainted()) {
            if (has(Effect.Type.HEALTH)) {
                int health = getValue(Effect.Type.HEALTH);
                character.modifyHealth(health);
                if (health > 0) {
                    dialogue.append("\n").append(health).append(" health restored!");
                } else {
                    dialogue.append("\n").append(-health).append(" damage done!");
                }
            }
            if (has(Effect.Type.MANA)) {
                int mana = getValue(Effect.Type.MANA);
                character.modifyMana(mana);
                if (mana > 0) {
                    dialogue.append("\n").append(mana).append(" mana restored!");
                } else {
                    dialogue.append("\n").append(-mana).append(" mana burned!");
                }
            }
            if (isStatus()) {
                if (dialogue.length() != 0) {
                    dialogue.append("<d>");
                }
                dialogue.append(statusDialogue());
                character.powerup(this);
            }
        }
        return dialogue.toString();
    }

    private void clean(Effect.Type type) {
        if (effects.get(type) == 0) effects.remove(type); // remove types whose value is 0
    }
}
