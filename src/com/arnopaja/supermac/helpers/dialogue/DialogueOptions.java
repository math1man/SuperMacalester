package com.arnopaja.supermac.helpers.dialogue;

import com.arnopaja.supermac.helpers.Interaction;
import com.arnopaja.supermac.helpers.InteractionUtils;

import java.util.Arrays;

/**
 * @author Ari Weiland
 */
public class DialogueOptions extends Dialogue {

    public static final String[] YES_NO_OPTIONS = {"Yes", "No"};
    public static final String[] BATTLE_OPTIONS = {"Attack", "Defend", "Spell", "Item", "Flee"};

    private final String header;
    private final Object[] options;
    private final Interaction[] interactions;
    private final int count;

    public DialogueOptions(String rawOptions) {
        this(rawOptions.split("\n"));
    }

    public DialogueOptions(String[] lines) {
        this(lines, new Interaction[0]);
    }

    public DialogueOptions(String rawOptions, Interaction... interactions) {
        this(rawOptions.split("\n"), interactions);
    }

    public DialogueOptions(String[] lines, Interaction... interactions) {
        this(lines[0], Arrays.copyOfRange(lines, 1, lines.length), interactions);
    }

    /**
     * Constructs a DialogueOptions with the specified header and options,
     * with no interactive effects on selection. If options are not strings,
     * the displayed text will call the toString method on the object.
     *
     * @param header the option header
     * @param options the list of options to select from
     */
    public <T> DialogueOptions(String header, T... options) {
        this(header, options, new Interaction[0]);
    }

    /**
     * Constructs a DialogueOptions with the specified header, options, and
     * interactions. The first option with cause the first interaction, etc.
     * There must be as many interactions as options, or the interactions
     * will be set to null. If options are not strings, the displayed text
     * will call the toString method on the object.
     *
     * @param header the option header
     * @param options the list of options to select from
     * @param interactions the list of interactions resulting from each option
     */
    public <T> DialogueOptions(String header, T[] options, Interaction... interactions) {
        this.header = header;
        this.options = Arrays.copyOf(options, options.length, Object[].class);
        if (interactions.length != options.length) {
            this.interactions = InteractionUtils.getNulls(options.length);
        } else {
            this.interactions = interactions;
        }
        this.count = options.length;
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
            return Interaction.NULL;
        }
    }

    public int getCount() {
        return count;
    }
}
