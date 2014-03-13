package com.arnopaja.supermac.battle;

import com.arnopaja.supermac.helpers.Dialogue;

import java.util.Random;

/**
 * Created by Envy on 2/28/14.
 */
public abstract class BattleAction {

    private static final Random battleRandomGen = new Random();

    public static enum ActionType { ATTACK, SPELL, ITEM, DEFEND, FLEE }

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
                return attack(source, destination);
            case SPELL:
                return spell(source, usable, destination);
            case ITEM:
                return item(source, usable, destination);
            case DEFEND:
                return defend(source);
            case FLEE:
                return flee(source);
            default:
                return null;
        }
    }

    public static BattleAction attack(BattleCharacter source, BattleCharacter destination) {
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

    public static BattleAction spell(BattleCharacter source, Usable spell, BattleCharacter destination) {
        return usable(source, spell, ActionType.SPELL, destination);
    }

    public static BattleAction item(BattleCharacter source, Usable item, BattleCharacter destination) {
        return usable(source, item, ActionType.ITEM, destination);
    }

    private static BattleAction usable(BattleCharacter source, Usable usable, ActionType type, BattleCharacter destination) {
        if (type != ActionType.SPELL && type != ActionType.ITEM) {
            return null;
        }
        return new BattleAction(source, destination, type, source.getSpeed(), usable) {
            @Override
            public Dialogue runAction() {
                return getUsable().use(getSource(), getDestination());
            }
        };
    }

    public static BattleAction defend(BattleCharacter source) {
        return new BattleAction(source, null, ActionType.DEFEND, Short.MAX_VALUE, null) {
            @Override
            public Dialogue runAction() {
                // TODO: code for defending
                String dialogue = getSource().getName() + " is defending!";
                return new Dialogue(dialogue);
            }
        };
    }

    public static BattleAction flee(BattleCharacter source) {
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
