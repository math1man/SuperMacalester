package com.arnopaja.supermac.helpers;

import com.arnopaja.supermac.battle.*;

import java.util.Arrays;

/**
 * Contains a lot of useful methods for generating very
 * specific interactions and arrays of interactions.
 *
 * @author Ari Weiland
 */
public class InteractionUtils {

    /**
     * Creates a generic interaction based on the class of the object.
     * DialogueDisplayables will create a dialogue interaction.
     * BattleControllers will create a goToBattle interaction.
     * BattleActions will create a battle interaction.
     * Anything else will create a null interaction.
     * @param object
     * @return
     */
    public static Interaction generic(Object object) {
        if (object == null) {
            return Interaction.NULL;
        } else if (object instanceof DialogueDisplayable) {
            return Interaction.dialogue((DialogueDisplayable) object);
        } else if (object instanceof BattleController) {
            return Interaction.goToBattle((BattleController) object);
        } else if (object instanceof BattleAction) {
            return Interaction.battle((BattleAction) object);
        } else {
            return Interaction.NULL;
        }
    }

    //---------------------------------
    //  Selecting BattleAction methods
    //----------------------------------

    public static Interaction selectAttack(BattleCharacter hero, BattleCharacter[] enemies) {
        return Interaction.dialogue(new DialogueOptions("Who do you want to attack?",
                enemies, convertActions(attacks(hero, enemies))));
    }

    public static Interaction selectDefend(BattleCharacter hero) {
        return Interaction.battle(BattleAction.defend(hero));
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

    // TODO: can items be used on friends?
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
        return Interaction.battle(BattleAction.flee(hero));
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
     * as per the Interaction.generic(Object) method
     * @param objects
     * @return
     */
    public static Interaction[] convert(Object... objects) {
        Interaction[] interactions = new Interaction[objects.length];
        for (int i=0; i<objects.length; i++) {
            interactions[i] = generic(objects[i]);
        }
        return interactions;
    }

    /**
     * Converts an array of dialogues into an array of dialogue interactions
     *
     * @param dialogues the list of dialogues to be converted
     * @return
     */
    public static Interaction[] convertDialogues(DialogueDisplayable... dialogues) {
        Interaction[] interactions = new Interaction[dialogues.length];
        for (int i=0; i<dialogues.length; i++) {
            interactions[i] = Interaction.dialogue(dialogues[i]);
        }
        return interactions;
    }

    /**
     * Returns an 4-array of interactions where each one initiates the specified dialogue
     *
     * @param dialogue the dialogue to initiate
     * @return
     */
    public static Interaction[] convertDialogues(DialogueDisplayable dialogue, int size) {
        DialogueDisplayable[] dialogues = new DialogueDisplayable[size];
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
    public static Interaction[] yesNoGoToBattle(BattleController battle, int size) {
        Interaction[] interactions = getNulls(size);
        interactions[0] = Interaction.goToBattle(battle);
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
            interactions[i] = Interaction.battle(actions[i]);
        }
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

    private static BattleAction[] spells(BattleCharacter source, Usable spell, BattleCharacter[] destinations) {
        int count = destinations.length;
        BattleAction[] spells = new BattleAction[count];
        for (int i=0; i<count; i++) {
            spells[i] = BattleAction.spell(source, spell, destinations[i]);
        }
        return spells;
    }

    private static BattleAction[] items(BattleCharacter source, Usable item, BattleCharacter[] destinations) {
        int count = destinations.length;
        BattleAction[] items = new BattleAction[count];
        for (int i=0; i<count; i++) {
            items[i] = BattleAction.item(source, item, destinations[i]);
        }
        return items;
    }
}
