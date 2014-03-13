package com.arnopaja.supermac.helpers;

import com.arnopaja.supermac.battle.BattleAction;
import com.arnopaja.supermac.battle.BattleController;

import java.util.Arrays;

/**
 * @author Ari Weiland
 */
public class DialogueOptions implements DialogueDisplayable {

    public static final String[] YES_NO_OPTIONS = {"Yes", "No"};
    public static final String[] BATTLE_OPTIONS = {"Attack", "Defend", "Spell", "Item"};

    private final String header;
    private final String[] options;
    private final Interaction[] interactions;
    private final int count;

    public DialogueOptions(String rawOptions) {
        this(parseDialogueOptions(rawOptions));
    }

    /**
     * Constructs a DialogueOptions with the specified header and options,
     * with no interactive effects on selection.
     *
     * @param header the option header
     * @param options the list of options to select from
     */
    public DialogueOptions(String header, String... options) {
        this(header, options, getNullInteractions());
    }

    public DialogueOptions(PreDialogueOptions preOptions) {
        this(preOptions.getHeader(), preOptions.getOptions());
    }

    /**
     * Constructs a DialogueOptions with the specified header, options, and
     * interactions.  The first option with cause the first interaction, etc.
     * There must be as many interactions as options.
     *
     * @param header the option header
     * @param options the list of options to select from
     * @param interactions the list of interactions resulting from each option
     */
    public DialogueOptions(String header, String[] options, Interaction[] interactions) {
        this.header = header;
        this.options = options;
        this.interactions = interactions;
        this.count = options.length;
    }

    public DialogueOptions(PreDialogueOptions preOptions, Interaction[] interactions) {
        this(preOptions.getHeader(), preOptions.getOptions(), interactions);
    }

    /**
     * Constructs a DialogueOptions with the specified header and options Yes and No,
     * with the Yes option initiates a battle, while the No option does nothing.
     *
     * @param header the option header
     * @param battle the battle to initiate
     */
    public DialogueOptions(String header, BattleController battle) {
        this(header, YES_NO_OPTIONS, yesNoGoToBattle(battle));
    }

    public String getHeader() {
        return header;
    }

    public String getOption(int option) {
        if (option < count && option >= 0) {
            return options[option];
        } else {
            return "";
        }
    }

    public Interaction getInteraction(int interaction) {
        if (interaction < count) {
            return interactions[interaction];
        } else {
            return Interaction.getNull();
        }
    }

    public int getCount() {
        return count;
    }

    /**
     * Returns a 4-array of null interactions
     * @return
     */
    public static Interaction[] getNullInteractions() {
        Interaction[] interactions = new Interaction[4];
        Arrays.fill(interactions, Interaction.getNull());
        return interactions;
    }

    /**
     * Converts an array of dialogues into an array of dialogue interactions
     *
     * @param dialogues the list of dialogues to be converted
     * @return
     */
    public static Interaction[] convertDialogues(DialogueDisplayable[] dialogues) {
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
    public static Interaction[] convertDialogues(DialogueDisplayable dialogue) {
        DialogueDisplayable[] dialogues = new DialogueDisplayable[4];
        Arrays.fill(dialogues, dialogue);
        return convertDialogues(dialogues);
    }

    /**
     * Returns a list of interactions with the first being a go to battle interaction,
     * and the rest being null interactions
     *
     * @param battle the battle to initiate
     * @return
     */
    public static Interaction[] yesNoGoToBattle(BattleController battle) {
        Interaction[] interactions = new Interaction[4];
        Arrays.fill(interactions, Interaction.getNull());
        interactions[0] = Interaction.goToBattle(battle);
        return interactions;
    }

    /**
     * Converts an array of battle actions into an array of battle interactions
     *
     * @param actions the battle actions to be converted
     * @return
     */
    public static Interaction[] convertActions(BattleAction[] actions) {
        Interaction[] interactions = new Interaction[actions.length];
        for (int i=0; i<actions.length; i++) {
            interactions[i] = Interaction.battle(actions[i]);
        }
        return interactions;
    }

    public static Interaction[] convert(Object... objects) {
        Interaction[] interactions = new Interaction[objects.length];
        for (int i=0; i<objects.length; i++) {
            Object object = objects[i];
            if (object == null) {
                interactions[i] = Interaction.getNull();
            } else if (object instanceof DialogueDisplayable) {
                interactions[i] = Interaction.dialogue((DialogueDisplayable) object);
            } else if (object instanceof BattleController) {
                interactions[i] = Interaction.goToBattle((BattleController) object);
            } else if (object instanceof BattleAction) {
                interactions[i] = Interaction.battle((BattleAction) object);
            } else {
                interactions[i] = Interaction.getNull();
            }
        }
        return interactions;
    }

    public static PreDialogueOptions parseDialogueOptions(String rawOptions) {
        String[] lines = rawOptions.split("\n");
        String header = lines[0];
        String[] options = Arrays.copyOfRange(lines, 1, lines.length);
        return new PreDialogueOptions(header, options);
    }


    // Intermediary class used to hold both a header and a list of options
    public static class PreDialogueOptions {
        private final String header;
        private final String[] options;

        public PreDialogueOptions(String header, String[] options) {
            this.header = header;
            this.options = options;
        }

        public String[] getOptions() {
            return options;
        }

        public String getHeader() {
            return header;
        }
    }
}
