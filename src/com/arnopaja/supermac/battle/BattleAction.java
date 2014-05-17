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
public abstract class BattleAction implements Interaction, Comparable<BattleAction> {

    private static final Random random = new Random();

    public static final int DEFEND_PRIORITY = Integer.MAX_VALUE;
    public static final double AVG_MISS_CHANCE = 1.0 / 3.0;
    public static final double FLEE_CHANCE = 1.0 / 5.0;

    private final BattleCharacter source;
    private final BattleCharacter destination;
    private final int priority;
    private final Spell spell;
    private final Item item;

    private BattleAction(BattleCharacter source, BattleCharacter destination, int priority, Spell spell, Item item){
        this.source = source;
        this.destination = destination;
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
            return new DialogueText(source + " has fainted!", DialogueStyle.BATTLE_CONSOLE);
        } else {
            if (canRun()) {
                return subrun(delta);
            } else {
                return new DialogueText(source + "'s target " + destination + " has fainted!", DialogueStyle.BATTLE_CONSOLE);
            }
        }
    }

    protected abstract Dialogue subrun(float delta); // delta needed for eventual action animations
    protected abstract boolean canRun();

    public static BattleAction attack(BattleCharacter source, BattleCharacter destination) {
        return new BattleAction(source, destination, source.getSpeed(), null, null) {
            @Override
            public Dialogue subrun(float delta) {
                String dialogue;
                if (random.nextDouble() / getDestination().getLevel() < random.nextDouble() / getSource().getLevel() * AVG_MISS_CHANCE) {
                    // MISS
                    dialogue = getSource() + " tried to attack " + getDestination() + " and missed!";
                } else {
                    int damage = (int) ((2 + Math.abs(random.nextGaussian())) * getSource().getAttack() / getDestination().getDefense());
                    getDestination().modifyHealth(-damage);
                    dialogue = getSource() + " attacks " + getDestination() + "!\n" +
                            damage + " damage done.";
                    if(getDestination().isFainted()) {
                        dialogue += "\n" + getDestination() + " fell!";
                    }
                }
                return new DialogueText(dialogue, DialogueStyle.BATTLE_CONSOLE);
            }

            @Override
            protected boolean canRun() {
                return !getDestination().isFainted();
            }
        };
    }

    public static BattleAction spell(BattleCharacter source, Spell spell, BattleCharacter destination) {
        return new BattleAction(source, destination, source.getSpeed(), spell, null) {
            @Override
            public Dialogue subrun(float delta) {
                getSource().getBattleClass().getMagicSound().play(Settings.getVolume());
                return getSpell().use(getSource(), getDestination());
            }

            @Override
            protected boolean canRun() {
                return !getDestination().isFainted() || getSpell().isResurrect();
            }
        };
    }

    public static BattleAction item(BattleCharacter source, Item item, BattleCharacter destination) {
        return new BattleAction(source, destination, source.getSpeed(), null, item) {
            @Override
            public Dialogue subrun(float delta) {
                if (Inventory.getMain().take(getItem())) {
                    if(getItem().isHealing()) AssetLoader.healingSound.play(Settings.getVolume());
                    else AssetLoader.powerupSound.play(Settings.getVolume());
                    return getItem().use(getSource(), getDestination());
                } else {
                    return new DialogueText("You do not have any " + getItem() + "s left.", DialogueStyle.BATTLE_CONSOLE);
                }
            }

            @Override
            protected boolean canRun() {
                return !getDestination().isFainted() || getItem().isHealing();
            }
        };
    }

    public static BattleAction defend(BattleCharacter source) {
        return new BattleAction(source, null, DEFEND_PRIORITY, null, null) {
            @Override
            public Dialogue subrun(float delta) {
                //Sets defending to true, which will cause the character to return twice its normal defense value
                //for the rest of the turn
                getSource().defend();
                String dialogue = getSource() + " is defending!";
                return new DialogueText(dialogue, DialogueStyle.BATTLE_CONSOLE);
            }

            @Override
            protected boolean canRun() {
                return true;
            }
        };
    }

    public static BattleAction flee(BattleCharacter source) {
        return new BattleAction(source, null, source.getSpeed(), null, null) {
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

            @Override
            protected boolean canRun() {
                return true;
            }
        };
    }

    public BattleCharacter getSource() {
        return source;
    }

    public BattleCharacter getDestination() {
        return destination;
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
    }

    @Override
    public int compareTo(BattleAction o) {
        return o.getPriority() - getPriority();
    }

    @Override
    public String toString() {
        return "BattleAction{" +
                "source=" + source +
                ", destination=" + destination +
                ", priority=" + priority +
                ", spell=" + spell +
                ", item=" + item +
                '}';
    }
}
