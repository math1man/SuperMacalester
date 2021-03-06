package com.arnopaja.supermac.battle;

import com.arnopaja.supermac.GameScreen;
import com.arnopaja.supermac.battle.characters.BattleCharacter;
import com.arnopaja.supermac.battle.characters.Hero;
import com.arnopaja.supermac.helpers.dialogue.Dialogue;
import com.arnopaja.supermac.helpers.dialogue.DialogueStyle;
import com.arnopaja.supermac.helpers.dialogue.DialogueText;
import com.arnopaja.supermac.helpers.interaction.Interaction;
import com.arnopaja.supermac.helpers.load.AssetLoader;
import com.arnopaja.supermac.inventory.Inventory;
import com.arnopaja.supermac.inventory.Item;
import com.arnopaja.supermac.inventory.Spell;
import com.arnopaja.supermac.plot.Settings;

import java.util.Random;

/**
 * @author Nolan Varani
 */
public abstract class BattleAction implements Interaction {

    private static final Random random = new Random();

    public static final int DEFEND_PRIORITY = Integer.MAX_VALUE;
    public static final double AVG_MISS_CHANCE = 1.0 / 3.0;
    public static final double FLEE_CHANCE = 1.0 / 5.0;

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

    /**
     * Runs this battle action. If the source is not fainted and any of
     * the following conditions are met, it will properly run the battle
     * action. If not, it will return a message detailing the conditions.
     *
     * Either:
     *  - there is no destination (defend or flee actions)
     *  - the destination is not fainted
     *  - the action is a healing spell
     *  - the action is a healing item
     *
     * @param delta
     * @return
     */
    public Dialogue run(float delta) {
        if (source.isFainted()) {
            return new DialogueText(source + " is fainted!", DialogueStyle.BATTLE_CONSOLE);
        } else {
            if (getDestination() == null                                           // no destination
                    || !getDestination().isFainted()                               // destination is not fainted
                    || ((getType() == ActionType.SPELL && !getSpell().isBlack())   // healing spell
                    || (getType() == ActionType.ITEM && getItem().isHealing()))) { // healing item
                return subrun(delta);
            } else {
                return new DialogueText(source + "'s target " + destination + " is fainted!", DialogueStyle.BATTLE_CONSOLE);
            }
        }
    }

    protected abstract Dialogue subrun(float delta); // delta needed for eventual action animations

    public static BattleAction attack(BattleCharacter source, BattleCharacter destination) {
        return new BattleAction(source, destination, ActionType.ATTACK, source.getSpeed(), null, null) {
            @Override
            public Dialogue subrun(float delta) {
                if(random.nextDouble() / getDestination().getLevel() < random.nextDouble() / getSource().getLevel() * AVG_MISS_CHANCE)
                {
                    //MISS
                    String dialogue = getSource() + " tried to attack " + getDestination() + " and missed!";
                    return new DialogueText(dialogue, DialogueStyle.BATTLE_CONSOLE);
                }
                int damage = (int) ((2 + Math.abs(random.nextGaussian())) * getSource().getAttack() / getDestination().getDefense());
                getDestination().modifyHealth(-damage);
                String dialogue = getSource() + " attacks " + getDestination() + "!\n" +
                        damage + " damage done.";
                if(getDestination().isFainted()) {
                    dialogue += "\n" + getDestination() + " fell!";
                }
                return new DialogueText(dialogue, DialogueStyle.BATTLE_CONSOLE);
            }
        };
    }

    public static BattleAction spell(BattleCharacter source, Spell spell, BattleCharacter destination) {
        return new BattleAction(source, destination, ActionType.SPELL, source.getSpeed(), spell, null) {
            @Override
            public Dialogue subrun(float delta) {
                getSource().getBattleClass().getMagicSound().play(Settings.getVolume());
                return getSpell().use(getSource(), getDestination());
            }
        };
    }

    public static BattleAction item(BattleCharacter source, Item item, BattleCharacter destination) {
        return new BattleAction(source, destination, ActionType.ITEM, source.getSpeed(), null, item) {
            @Override
            public Dialogue subrun(float delta) {
                if(getItem().isHealing()) AssetLoader.healingSound.play(Settings.getVolume());
                else AssetLoader.powerupSound.play(Settings.getVolume());
                return getItem().use(getSource(), getDestination());
            }
        };
    }

    public static BattleAction defend(BattleCharacter source) {
        return new BattleAction(source, null, ActionType.DEFEND, DEFEND_PRIORITY, null, null) {
            @Override
            public Dialogue subrun(float delta) {
                //Sets defending to true, which will cause the character to return twice its normal defense value
                //for the rest of the turn
                getSource().defend();
                String dialogue = getSource() + " is defending!";
                return new DialogueText(dialogue, DialogueStyle.BATTLE_CONSOLE);
            }
        };
    }

    public static BattleAction flee(BattleCharacter source) {
        return new BattleAction(source, null, ActionType.FLEE, source.getSpeed(), null, null) {
            @Override
            public Dialogue subrun(float delta) {
                String dialogue;
                // Only heroes can flee
                if(random.nextDouble() < FLEE_CHANCE && getSource() instanceof Hero) {
                    Hero h = (Hero) getSource();
                    h.setHasFled(true);
                    dialogue = h + " flees!";
                } else {
                    dialogue = getSource() + " could not flee!";
                }
                return new DialogueText(dialogue, DialogueStyle.BATTLE_CONSOLE);
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
    public void run(GameScreen screen) {
        screen.getBattle().addAction(this);
        if (getType() == BattleAction.ActionType.ITEM) {
            // Removes the item from Inventory when the battle
            // action is put into the action queue
            Inventory.getMain().take(getItem());
        }
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
