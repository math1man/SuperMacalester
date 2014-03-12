package com.arnopaja.supermac.helpers;

import com.arnopaja.supermac.objects.Interaction;
import com.arnopaja.supermac.render.BattleController;

import java.util.Arrays;
import java.util.List;

/**
 * @author Ari Weiland
 */
public class DialogueOptions {

    public static final String[] YES_NO_OPTIONS = {"Yes", "No"};

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
    public DialogueOptions(String header, String[] options) {
        this(header, options, getNoInteractions());
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
        this(header, YES_NO_OPTIONS, getYesNoBattleInteractions(battle));
    }

    /**
     * Constructs a DialogueOptions with the specified header, options, and
     * dialogue interactions defined by the specified list of dialogues.
     * @param header the option header
     * @param options the list of options to select from
     * @param dialogues the list of dialogues to continue with for each option
     * @return the consturcted DialogueOptions
     */
    public static DialogueOptions getContinuedDialogueOptions(String header, String[] options,
                                                              List<Dialogue> dialogues) {
        return new DialogueOptions(header, options, getContinuedDialogueInteractions(dialogues));
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
            return Interaction.getNullInteraction();
        }
    }

    public int getCount() {
        return count;
    }

    public static PreDialogueOptions parseDialogueOptions(String rawOptions) {
        String[] lines = rawOptions.split("\n");
        String header = lines[0];
        String[] options = Arrays.copyOfRange(lines, 1, lines.length);
        return new PreDialogueOptions(header, options);
    }

    /**
     * Returns a list of null interactions
     * @return
     */
    public static Interaction[] getNoInteractions() {
        Interaction[] interactions = new Interaction[4];
        Arrays.fill(interactions, Interaction.getNullInteraction());
        return interactions;
    }

    /**
     * Returns a list of interactions with the first being a battle interaction,
     * and the rest being null interactions
     *
     * @param battle the battle to initiate
     * @return
     */
    public static Interaction[] getYesNoBattleInteractions(BattleController battle) {
        Interaction[] interactions = new Interaction[4];
        Arrays.fill(interactions, Interaction.getNullInteraction());
        interactions[0] = Interaction.getBattleInteraction(battle);
        return interactions;
    }

    /**
     * Converts a list of dialogues into an array of dialogue interactions
     *
     * @param dialogues the list of dialogues to be converted
     * @return
     */
    public static Interaction[] getContinuedDialogueInteractions(List<Dialogue> dialogues) {
        Interaction[] interactions = new Interaction[dialogues.size()];
        for (int i=0; i<dialogues.size(); i++) {
            interactions[i] = Interaction.getDialogueInteraction(dialogues.get(i));
        }
        return interactions;
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
