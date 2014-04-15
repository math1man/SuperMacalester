package com.arnopaja.supermac.helpers;

import com.arnopaja.supermac.battle.Battle;
import com.arnopaja.supermac.battle.BattleAction;
import com.arnopaja.supermac.battle.Spell;
import com.arnopaja.supermac.battle.characters.BattleCharacter;
import com.arnopaja.supermac.helpers.dialogue.Dialogue;
import com.arnopaja.supermac.helpers.dialogue.DialogueOptions;
import com.arnopaja.supermac.inventory.Item;
import com.arnopaja.supermac.plot.Quest;
import com.arnopaja.supermac.world.grid.Location;
import com.arnopaja.supermac.world.objects.Chest;
import com.arnopaja.supermac.world.objects.Entity;

import java.util.Arrays;

/**
 * Contains a lot of useful methods for generating very
 * specific interactions and arrays of interactions.
 *
 * @author Ari Weiland
 */
public class InteractionUtils {

    /**
     * Creates a generic interaction based on the classes passed in
     *
     * For 1 parameter:
     * DialogueDisplayables will create a dialogue interaction
     * BattleControllers will create a battle interaction
     * BattleActions will create a battleAction interaction
     * Chests will create an openChest interaction
     * Quests will create a nextGoal interaction
     *
     * For 2 parameters:
     * Item followed by Chest will create a takeItem interaction
     * Entity followed by Location will create a changeGrid interaction
     *
     * Note that this method will never create a closeChest interaction
     *
     * Anything other parameter combinations will create a null interaction
     *
     * @param objects
     * @return
     */
    public static Interaction create(Object... objects) {
        if (objects.length > 1) {
            Object primary = objects[0];
            if (primary == null) {
                return Interaction.NULL;
            }
            if (objects.length == 2) {
                Object secondary = objects[1];
                if (secondary == null) {
                    return Interaction.NULL;
                } else if (primary instanceof Item && secondary instanceof Chest) {
                    return Interaction.takeItem((Item) primary, (Chest) secondary);
                } else if (primary instanceof Entity && secondary instanceof Location) {
                    return Interaction.changeGrid((Entity) primary, (Location) secondary);
                }
            } else {
                if (primary instanceof Dialogue) {
                    return Interaction.dialogue((Dialogue) primary);
                } else if (primary instanceof Battle) {
                    return Interaction.battle((Battle) primary);
                } else if (primary instanceof BattleAction) {
                    return Interaction.battleAction((BattleAction) primary);
                } else if (primary instanceof Chest) {
                    return Interaction.openChest((Chest) primary);
                } else if (primary instanceof Quest) {
                    return Interaction.nextGoal((Quest) primary);
                }
            }
        }
        return Interaction.NULL;
    }

    //---------------------------------
    //  Selecting BattleAction methods
    //----------------------------------

    public static Interaction selectAttack(BattleCharacter hero, BattleCharacter[] enemies) {
        return Interaction.dialogue(new DialogueOptions("Who do you want to attack?",
                enemies, convertActions(attacks(hero, enemies))));
    }

    public static Interaction selectDefend(BattleCharacter hero) {
        return Interaction.battleAction(BattleAction.defend(hero));
    }

    public static Interaction selectSpell(BattleCharacter hero, Spell[] spells, BattleCharacter[] enemies) {
        Interaction[] spellInteractions = new Interaction[spells.length];
        for (int i=0; i<spells.length; i++) {
            spellInteractions[i] = Interaction.dialogue(
                    new DialogueOptions("Who do you want to use " + spells[i] + " on?", enemies,
                            convertActions(spells(hero, spells[i], enemies))));
        }
        return Interaction.dialogue(new DialogueOptions("What spell do you want to use?",
                spells, spellInteractions));
    }

    // TODO: can items be used on friends? On self?
    public static Interaction selectItem(BattleCharacter hero, Item[] items, BattleCharacter[] enemies) {
        Interaction[] spellInteractions = new Interaction[items.length];
        for (int i=0; i<items.length; i++) {
            spellInteractions[i] = Interaction.dialogue(
                    new DialogueOptions("Who do you want to use " + items[i] + " on?", enemies,
                            convertActions(items(hero, items[i], enemies))));
        }
        return Interaction.dialogue(new DialogueOptions("What item do you want to use?",
                items, spellInteractions));
    }

    public static Interaction selectFlee(BattleCharacter hero) {
        return Interaction.battleAction(BattleAction.flee(hero));
    }

    //-----------------------------
    //  Interaction Array Methods
    //-----------------------------

    /**
     * Returns a 4-array of null interactions
     * @return
     * @param size
     */
    public static Interaction[] getNulls(int size) {
        Interaction[] interactions = new Interaction[size];
        Arrays.fill(interactions, Interaction.NULL);
        return interactions;
    }

    /**
     * Converts a set of objects into an array of interactions,
     * as per the Interaction.create(Object) method.
     * Note that an element of objects can itself be an array,
     * if for example you wanted to create a changeGrid interaction.
     * @param objects
     * @return
     */
    public static Interaction[] convert(Object... objects) {
        Interaction[] interactions = new Interaction[objects.length];
        for (int i=0; i<objects.length; i++) {
            interactions[i] = create(objects[i]);
        }
        return interactions;
    }

    /**
     * Converts an array of dialogues into an array of dialogue interactions
     *
     * @param dialogues the list of dialogues to be converted
     * @return
     */
    public static Interaction[] convertDialogues(Dialogue... dialogues) {
        Interaction[] interactions = new Interaction[dialogues.length];
        for (int i=0; i<dialogues.length; i++) {
            interactions[i] = Interaction.dialogue(dialogues[i]);
        }
        return interactions;
    }

    /**
     * Returns an array of interactions of the specified size
     * where each one initiates the specified dialogue
     *
     * @param dialogue the dialogue to initiate
     * @return
     */
    public static Interaction[] convertDialogues(Dialogue dialogue, int size) {
        Dialogue[] dialogues = new Dialogue[size];
        Arrays.fill(dialogues, dialogue);
        return convertDialogues(dialogues);
    }

    /**
     * Returns a list of interactions with the first being a go to battle interaction,
     * and the rest being null interactions
     *
     *
     * @param battle the battle to initiate
     * @param size
     * @return
     */
    public static Interaction[] yesNoGoToBattle(Battle battle, int size) {
        Interaction[] interactions = getNulls(size);
        interactions[0] = Interaction.battle(battle);
        return interactions;
    }

    /**
     * Converts an array of battle actions into an array of battle interactions
     *
     * @param actions the battle actions to be converted
     * @return
     */
    public static Interaction[] convertActions(BattleAction... actions) {
        Interaction[] interactions = new Interaction[actions.length];
        for (int i=0; i<actions.length; i++) {
            interactions[i] = Interaction.battleAction(actions[i]);
        }
        return interactions;
    }

    public static Interaction[] makeBattleActions(BattleCharacter hero, BattleCharacter[] enemies,
                                                  Spell[] spells, Item[] items) {
        Interaction[] interactions = new Interaction[5];
        interactions[0] = InteractionUtils.selectAttack(hero, enemies);
        interactions[1] = InteractionUtils.selectDefend(hero);
        interactions[2] = InteractionUtils.selectSpell(hero, spells, enemies);
        interactions[3] = InteractionUtils.selectItem(hero, items, enemies);
        interactions[4] = InteractionUtils.selectFlee(hero);
        return interactions;
    }

    //----------------------------------------
    //  BattleAction Array Methods (private)
    //----------------------------------------

    private static BattleAction[] attacks(BattleCharacter source, BattleCharacter[] destinations) {
        int count = destinations.length;
        BattleAction[] attacks = new BattleAction[count];
        for (int i=0; i<count; i++) {
            attacks[i] = BattleAction.attack(source, destinations[i]);
        }
        return attacks;
    }

    private static BattleAction[] spells(BattleCharacter source, Spell spell, BattleCharacter[] destinations) {
        int count = destinations.length;
        BattleAction[] spells = new BattleAction[count];
        for (int i=0; i<count; i++) {
            spells[i] = BattleAction.spell(source, spell, destinations[i]);
        }
        return spells;
    }

    private static BattleAction[] items(BattleCharacter source, Item item, BattleCharacter[] destinations) {
        int count = destinations.length;
        BattleAction[] items = new BattleAction[count];
        for (int i=0; i<count; i++) {
            items[i] = BattleAction.item(source, item, destinations[i]);
        }
        return items;
    }
}
