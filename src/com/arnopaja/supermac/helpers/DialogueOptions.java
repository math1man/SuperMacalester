package com.arnopaja.supermac.helpers;

import java.util.Arrays;

/**
 * @author Ari Weiland
 */
public class DialogueOptions implements DialogueDisplayable {

    public static final String[] YES_NO_OPTIONS = {"Yes", "No"};
    public static final String[] BATTLE_OPTIONS = {"Attack", "Defend", "Spell", "Item", "Flee"};

    private final String header;
    private final Object[] options;
    private final Interaction[] interactions;
    private final int count;

    public DialogueOptions(String rawOptions) {
        this(parseDialogueOptions(rawOptions));
    }

    public DialogueOptions(String rawOptions, Interaction... interactions) {
        this(parseDialogueOptions(rawOptions), interactions);
    }

    /**
     * Constructs a DialogueOptions with the specified header and options,
     * with no interactive effects on selection. If options are not strings,
     * the displayed text will call the toString method on the object.
     *
     * @param header the option header
     * @param options the list of options to select from
     */
    public DialogueOptions(String header, Object... options) {
        this(header, options, InteractionUtils.getNulls(options.length));
    }

    public DialogueOptions(PreDialogueOptions preOptions) {
        this(preOptions.getHeader(), preOptions.getOptions());
    }

    /**
     * Constructs a DialogueOptions with the specified header, options, and
     * interactions.  The first option with cause the first interaction, etc.
     * There must be as many interactions as options. If options are not strings,
     * the displayed text will call the toString method on the object.
     *
     * @param header the option header
     * @param options the list of options to select from
     * @param interactions the list of interactions resulting from each option
     */
    public DialogueOptions(String header, Object[] options, Interaction... interactions) {
        this.header = header;
        this.options = options;
        this.interactions = interactions;
        this.count = options.length;
    }

    public DialogueOptions(PreDialogueOptions preOptions, Interaction[] interactions) {
        this(preOptions.getHeader(), preOptions.getOptions(), interactions);
    }

    public String getHeader() {
        return header;
    }

    public String getOption(int option) {
        if (option < count && option >= 0) {
            return options[option].toString();
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
