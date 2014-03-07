package com.arnopaja.supermac.battle;

import java.util.Random;

/**
 * Created by Envy on 2/28/14.
 */
public abstract class BattleAction {

    private static final Random battleRandomGen = new Random();

    public enum ActionType { ATTACK, MAGIC, ITEM, DEFEND, FLEE }

    private final Character source;
    private final Character destination;
    private final short priority;
    private final Usable usable;
    private final ActionType type;

    private BattleAction(Character source, Character destination, ActionType type, short priority, Usable usable){
        this.source = source;
        this.destination = destination;
        this.type = type;
        this.priority = priority;
        this.usable = usable;
    }

    public static BattleAction create(Character source, Character destination, ActionType type, Usable usable) {
        switch(type) {
            case ATTACK:
                return createAttack(source, destination);
            case MAGIC:
                return createMagic(source, destination, usable);
            case ITEM:
                return createItem(source, destination, usable);
            case DEFEND:
                return createDefend(source);
            case FLEE:
                return createFlee(source);
            default:
                return null;
        }
    }

    public static BattleAction createAttack(Character source, Character destination) {
        return new BattleAction(source, destination, ActionType.ATTACK, source.getSpeed(), null) {
            @Override
            public int runAction() {
                System.out.println(getSource().getName() + " attacks " + getDestination().getName());
                int damage = (int) (getSource().getAttack() * (2 + Math.abs(battleRandomGen.nextGaussian())));
                damage /= getDestination().getDefense();
                System.out.println(damage + " damage done!");
                getDestination().modifyHealth((short) -damage);
                if(getDestination().isFainted()) System.out.println(getDestination().getName() + " fell!");
                return damage;
            }
        };
    }

    public static BattleAction createMagic(Character source, Character destination, Usable spell) {
        return new BattleAction(source, destination, ActionType.MAGIC, source.getSpeed(), spell) {
            @Override
            public int runAction() {
                return getUsable().use(getSource(), getDestination());
            }
        };
    }

    public static BattleAction createItem(Character source, Character destination, Usable item) {
        return new BattleAction(source, destination, ActionType.ITEM, source.getSpeed(), item) {
            @Override
            public int runAction() {
                return getUsable().use(getSource(), getDestination());
            }
        };
    }

    public static BattleAction createDefend(Character source) {
        return new BattleAction(source, null, ActionType.DEFEND, Short.MAX_VALUE, null) {
            @Override
            public int runAction() {
                // TODO: code for defending
                return 0;
            }
        };
    }

    public static BattleAction createFlee(Character source) {
        // TODO: what is the priority for fleeing?
        return new BattleAction(source, null, ActionType.FLEE, source.getSpeed(), null) {
            @Override
            public int runAction() {
                // TODO: code for fleeing
                return 0;
            }
        };
    }

    public abstract int runAction();

    public Character getSource() {
        return source;
    }

    public Character getDestination() {
        return destination;
    }

    public ActionType getType() {
        return type;
    }

    public short getPriority() {
        return priority;
    }

    public Usable getUsable() {
        return usable;
    }
}
