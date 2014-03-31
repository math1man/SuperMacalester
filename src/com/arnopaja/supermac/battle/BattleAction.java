package com.arnopaja.supermac.battle;

import com.arnopaja.supermac.battle.characters.BattleCharacter;
import com.arnopaja.supermac.helpers.dialogue.Dialogue;
import com.arnopaja.supermac.inventory.Item;

import java.util.Random;

/**
 * Created by Envy on 2/28/14.
 */
public abstract class BattleAction {

    private static final Random RANDOM = new Random();

    public static enum ActionType { ATTACK, SPELL, ITEM, DEFEND, FLEE }

    private final BattleCharacter source;
    private final BattleCharacter destination;
    private final int priority;
    private final Spell spell;
    private final Item item;
    private final ActionType type;

    private BattleAction(BattleCharacter source, BattleCharacter destination, ActionType type, int priority, Spell spell, Item item){
        this.source = source;
        this.destination = destination;
        this.type = type;
        this.priority = priority;
        this.spell = spell;
        this.item = item;
    }

    public abstract Dialogue run();

    public static BattleAction attack(BattleCharacter source, BattleCharacter destination) {
        return new BattleAction(source, destination, ActionType.ATTACK, source.getSpeed(), null, null) {
            @Override
            public Dialogue run() {
                float damage = (float) (getSource().getAttack() / getDestination().getDefense()
                        * (2 + Math.abs(RANDOM.nextGaussian())));
                int damageDone = (int) getDestination().modifyHealth(-damage);
                String dialogue = getSource() + " attacks " + getDestination() + "!\n" +
                        damageDone + " damage done.";
                if(getDestination().isFainted()) {
                    dialogue += "\n" + getDestination() + " fell!";
                }
                System.out.println(dialogue);
                return new Dialogue(dialogue);
            }
        };
    }

    public static BattleAction spell(BattleCharacter source, Spell spell, BattleCharacter destination) {
        return new BattleAction(source, destination, ActionType.SPELL, source.getSpeed(), spell, null) {
            @Override
            public Dialogue run() {
                return getSpell().use(getSource(), getDestination());
            }
        };
    }

    public static BattleAction item(BattleCharacter source, Item item, BattleCharacter destination) {
        return new BattleAction(source, destination, ActionType.ITEM, source.getSpeed(), null, item) {
            @Override
            public Dialogue run() {
                return getItem().use(getSource(), getDestination());
            }
        };
    }

    public static BattleAction defend(BattleCharacter source) {
        return new BattleAction(source, null, ActionType.DEFEND, Integer.MAX_VALUE, null, null) {
            @Override
            public Dialogue run() {
                // TODO: code for defending
                String dialogue = getSource() + " is defending!";
                return new Dialogue(dialogue);
            }
        };
    }

    public static BattleAction flee(BattleCharacter source) {
        // TODO: what is the priority for fleeing?
        return new BattleAction(source, null, ActionType.FLEE, source.getSpeed(), null, null) {
            @Override
            public Dialogue run() {
                // TODO: code for fleeing
                String dialogue = getSource() + " flees!";
                return new Dialogue(dialogue);
            }
        };
    }

    public BattleCharacter getSource() {
        return source;
    }

    public BattleCharacter getDestination() {
        return destination;
    }

    public ActionType getType() {
        return type;
    }

    public int getPriority() {
        return priority;
    }

    public Spell getSpell() {
        return spell;
    }

    public Item getItem() {
        return item;
    }
}
