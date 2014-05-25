package com.arnopaja.supermac.inventory;

/**
 * @author Nolan Varani
 */
public class Effect {
    public final Type type;
    public final boolean self;
    public final int value;
    // positive values ALWAY increase, negative values decrease
    // ie. damage would be of type HEALTH and value < 0

    public Effect(Type type, boolean self, int value) {
        this.type = type;
        this.self = self;
        this.value = value;
    }

    public boolean isHealing() {
        return type == Type.HEALTH && value > 0;
    }

    public boolean isResurrect()
    {
        return type == Type.HEALTH && value == 0;
    }

    public boolean isDamage() {
        return type == Type.HEALTH && value < 0;
    }

    public boolean isManaRestore()
    {
        return type == Type.MANA && value >= 0;
    }

    public boolean isManaBurn()
    {
        return type == Type.MANA && value < 0;
    }

    public boolean isStatus() {
        return type != Type.HEALTH && type != Type.MANA;
    }

    public boolean isSpecial()
    {
        return type == Type.SPECIAL;
    }

    public boolean isAttack()
    {
        return type == Type.ATTACK;
    }

    public boolean isDefense()
    {
        return type == Type.DEFENSE;
    }

    public boolean isSpeed()
    {
        return type == Type.SPEED;
    }

    @Override
    public String toString() {
        String prefix = type.prefix;
        StringBuilder sb = new StringBuilder();
        if (self) {
            sb.append(prefix.toUpperCase());
        } else {
            sb.append(prefix.toLowerCase());
        }
        sb.append(value);
        return sb.toString();
    }

    public static enum Type {
        HEALTH("h"),
        MANA("m"),
        ATTACK("a"),
        DEFENSE("d"),
        SPEED("s"),
        SPECIAL("z");

        private final String prefix;

        Type(String prefix) {
            this.prefix = prefix;
        }

        public static Type get(String prefix) {
            for (Type t : Type.values()) {
                if (t.prefix.equals(prefix.toLowerCase())) {
                    return t;
                }
            }
            return null;
        }
    }
}
