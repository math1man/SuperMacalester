package com.arnopaja.supermac.battle;

import com.arnopaja.supermac.helpers.Dialogue;

import java.util.Random;

/**
 * Created by Envy on 2/28/14.
 */
public abstract class BattleAction {

    private static final Random battleRandomGen = new Random();

    public static enum ActionType { ATTACK, MAGIC, ITEM, DEFEND, FLEE }

    private final BattleCharacter source;
    private final BattleCharacter destination;
    private final short priority;
    private final Usable usable;
    private final ActionType type;

    private BattleAction(BattleCharacter source, BattleCharacter destination, ActionType type, short priority, Usable usable){
        this.source = source;
        this.destination = destination;
        this.type = type;
        this.priority = priority;
        this.usable = usable;
    }

    public static BattleAction create(BattleCharacter source, BattleCharacter destination, ActionType type, Usable usable) {
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

    public static BattleAction createAttack(BattleCharacter source, BattleCharacter destination) {
        return new BattleAction(source, destination, ActionType.ATTACK, source.getSpeed(), null) {
            @Override
            public Dialogue runAction() {
                int damage = (int) (getSource().getAttack() * (2 + Math.abs(battleRandomGen.nextGaussian())));
                damage /= getDestination().getDefense();
                getDestination().modifyHealth((short) -damage);
                String dialogue = getSource().getName() + " attacks " + getDestination().getName() + "!" +
                        "\n" + damage + " damage done!";
                if(getDestination().isFainted()) {
                    dialogue += "\n" + getDestination().getName() + " fell!";
                }
                System.out.println(dialogue);
                return new Dialogue(dialogue);
            }
        };
    }

    public static BattleAction createMagic(BattleCharacter source, BattleCharacter destination, Usable spell) {
        return new BattleAction(source, destination, ActionType.MAGIC, source.getSpeed(), spell) {
            @Override
            public Dialogue runAction() {
                return getUsable().use(getSource(), getDestination());
            }
        };
    }

    public static BattleAction createItem(BattleCharacter source, BattleCharacter destination, Usable item) {
        return new BattleAction(source, destination, ActionType.ITEM, source.getSpeed(), item) {
            @Override
            public Dialogue runAction() {
                return getUsable().use(getSource(), getDestination());
            }
        };
    }

    public static BattleAction createDefend(BattleCharacter source) {
        return new BattleAction(source, null, ActionType.DEFEND, Short.MAX_VALUE, null) {
            @Override
            public Dialogue runAction() {
                // TODO: code for defending
                String dialogue = getSource().getName() + " is defending!";
                return new Dialogue(dialogue);
            }
        };
    }

    public static BattleAction createFlee(BattleCharacter source) {
        // TODO: what is the priority for fleeing?
        return new BattleAction(source, null, ActionType.FLEE, source.getSpeed(), null) {
            @Override
            public Dialogue runAction() {
                // TODO: code for fleeing
                String dialogue = getSource().getName() + " flees!";
                return new Dialogue(dialogue);
            }
        };
    }

    public abstract Dialogue runAction();

    public BattleCharacter getSource() {
        return source;
    }

    public BattleCharacter getDestination() {
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
