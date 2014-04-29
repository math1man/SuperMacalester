package com.arnopaja.supermac.battle;

import com.arnopaja.supermac.GameScreen;
import com.arnopaja.supermac.battle.characters.BattleCharacter;
import com.arnopaja.supermac.helpers.Interaction;
import com.arnopaja.supermac.helpers.InteractionBuilder;
import com.arnopaja.supermac.helpers.dialogue.Dialogue;
import com.arnopaja.supermac.helpers.dialogue.DialogueText;
import com.arnopaja.supermac.inventory.Inventory;
import com.arnopaja.supermac.inventory.Item;
import com.arnopaja.supermac.inventory.Spell;

import java.util.Random;

/**
 * @author Nolan Varani
 */
public abstract class BattleAction implements InteractionBuilder {

    private static final Random random = new Random();
    public static final int DEFEND_PRIORITY = 255; // TODO: should this be higher?
    public static final int FLEE_PRIORITY = Integer.MAX_VALUE;

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

    public abstract Dialogue run(float delta); // delta needed for eventual action animations

    public static BattleAction attack(BattleCharacter source, BattleCharacter destination) {
        return new BattleAction(source, destination, ActionType.ATTACK, source.getSpeed(), null, null) {
            @Override
            public Dialogue run(float delta) {
                float damage = (float) (getSource().getAttack() / getDestination().getDefense()
                        * (2 + Math.abs(random.nextGaussian())));
                int damageDone = -(int) getDestination().modifyHealth(-damage);
                String dialogue = getSource() + " attacks " + getDestination() + "!\n" +
                        damageDone + " damage done.";
                if(getDestination().isFainted()) {
                    dialogue += "\n" + getDestination() + " fell!";
                }
                return new DialogueText(dialogue);
            }
        };
    }

    public static BattleAction spell(BattleCharacter source, Spell spell, BattleCharacter destination) {
        return new BattleAction(source, destination, ActionType.SPELL, source.getSpeed(), spell, null) {
            @Override
            public Dialogue run(float delta) {
                return getSpell().use(getSource(), getDestination());
            }
        };
    }

    public static BattleAction item(BattleCharacter source, Item item, BattleCharacter destination) {
        return new BattleAction(source, destination, ActionType.ITEM, source.getSpeed(), null, item) {
            @Override
            public Dialogue run(float delta) {
                return getItem().use(getSource(), getDestination());
            }
        };
    }

    public static BattleAction defend(BattleCharacter source) {
        return new BattleAction(source, null, ActionType.DEFEND, DEFEND_PRIORITY, null, null) {
            @Override
            public Dialogue run(float delta) {
                // TODO: code for defending
                String dialogue = getSource() + " is defending!";
                return new DialogueText(dialogue);
            }
        };
    }

    public static BattleAction flee(BattleCharacter source) {
        return new BattleAction(source, null, ActionType.FLEE, FLEE_PRIORITY, null, null) {
            @Override
            public Dialogue run(float delta) {
                // TODO: code for fleeing
                String dialogue = getSource() + " flees!";
                return new DialogueText(dialogue);
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

    @Override
    public Interaction toInteraction() {
        final BattleAction action = this;
        return new Interaction(action) {
            @Override
            public void run(GameScreen screen) {
                screen.getBattle().addAction(action);
                if (action.getType() == BattleAction.ActionType.ITEM) {
                    // Removes the item from Inventory when the battle
                    // action is put into the action queue
                    Inventory.getMain().take(action.getItem());
                }
            }
        };

    }

    @Override
    public String toString() {
        return "BattleAction{" +
                "source=" + source +
                ", destination=" + destination +
                ", priority=" + priority +
                ", spell=" + spell +
                ", item=" + item +
                ", type=" + type +
                '}';
    }
}
