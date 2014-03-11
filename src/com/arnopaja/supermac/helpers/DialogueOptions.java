package com.arnopaja.supermac.helpers;

import com.arnopaja.supermac.objects.Interaction;
import com.arnopaja.supermac.render.BattleInterface;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @author Ari Weiland
 */
public class DialogueOptions {

    private final String header;
    private final List<String> options;
    private final List<Interaction> interactions;
    private final int count;

    /**
     * Constructs a DialogueOptions with the specified header and options,
     * with no interactive effects on selection.
     *
     * @param header the option header
     * @param options the list of options to select from
     */
    public DialogueOptions(String header, List<String> options) {
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
    public DialogueOptions(String header, List<String> options, List<Interaction> interactions) {
        this.header = header;
        this.options = options;
        this.interactions = interactions;
        this.count = options.size();
    }

    public DialogueOptions(PreDialogueOptions preOptions, List<Interaction> interactions) {
        this(preOptions.getHeader(), preOptions.getOptions(), interactions);
    }

    /**
     * Constructs a DialogueOptions with the specified header and options Yes and No,
     * with the Yes option initiates a battle, while the No option does nothing.
     *
     * @param header the option header
     * @param battle the battle to initiate
     */
    public DialogueOptions(String header, BattleInterface battle) {
        this(header, getYesNoOptions(), getYesNoBattleInteractions(battle));
    }

    /**
     * Constructs a DialogueOptions with the specified header, options, and
     * dialogue interactions defined by the specified list of dialogues.
     * @param header the option header
     * @param options the list of options to select from
     * @param dialogues the list of dialogues to continue with for each option
     * @return the consturcted DialogueOptions
     */
    public static DialogueOptions getContinuedDialogueOptions(String header, List<String> options,
                                                              List<Dialogue> dialogues) {
        return new DialogueOptions(header, options, getContinuedDialogueInteractions(dialogues));
    }

    public String getHeader() {
        return header;
    }

    public String getOption(int option) {
        if (option < count && option >= 0) {
            return options.get(option);
        } else {
            return "";
        }
    }

    public Interaction getInteraction(int number) {
        if (number < count && number >= 0) {
            return interactions.get(number);
        } else {
            return Interaction.getNullInteraction();
        }
    }

    public int getCount() {
        return count;
    }

    public static PreDialogueOptions parseDialogueOptions(String rawOptions) {
        String[] lines = rawOptions.split("\n");
        int beginOptionIndex = 0, endOptionIndex = lines.length;
        String header = "";
        List<String> options = new ArrayList<String>(5);
        for (int i=0; i<lines.length; i++) {
            String line = lines[i].trim();
            if (Pattern.matches("<options:.*>", line)) {
                header = line.substring(9, line.length() - 1);
                beginOptionIndex = i;
            } else if (line.contains("</options>")) {
                endOptionIndex = i;
            } else if (i > beginOptionIndex && i < endOptionIndex && Pattern.matches("<option:.*>", line)) {
                String option = line.substring(8, line.length() - 1);
                options.add(option);
            }
        }
        return new PreDialogueOptions(header, options);
    }

    /**
     * Returns a List of Strings containing Yes and No in that order
     * @return
     */
    public static List<String> getYesNoOptions() {
        List<String> options = new ArrayList<String>(2);
        options.set(0, "Yes");
        options.set(1, "No");
        return options;
    }

    /**
     * Returns a list of null interactions
     * @return
     */
    public static List<Interaction> getNoInteractions() {
        List<Interaction> interactions = new ArrayList<Interaction>(4);
        Collections.fill(interactions, Interaction.getNullInteraction());
        return interactions;
    }

    /**
     * Returns a list of interactions with the first being a battle interaction,
     * and the rest being null interactions
     *
     * @param battle the battle to initiate
     * @return
     */
    public static List<Interaction> getYesNoBattleInteractions(BattleInterface battle) {
        List<Interaction> interactions = new ArrayList<Interaction>(4);
        Collections.fill(interactions, Interaction.getNullInteraction());
        interactions.set(0, Interaction.getBattleInteraction(battle));
        return interactions;
    }

    /**
     * Converts a list of dialogues into a list of dialogue interactions
     *
     * @param dialogues the list of dialogues to be converted
     * @return
     */
    public static List<Interaction> getContinuedDialogueInteractions(List<Dialogue> dialogues) {
        List<Interaction> interactions = new ArrayList<Interaction>(dialogues.size());
        for (int i=0; i<dialogues.size(); i++) {
            interactions.set(i, Interaction.getDialogueInteraction(dialogues.get(i)));
        }
        return interactions;
    }

    // Intermediary class used to hold both a header and a list of options
    public static class PreDialogueOptions {
        private final String header;
        private final List<String> options;

        public PreDialogueOptions(String header, List<String> options) {
            this.header = header;
            this.options = options;
        }

        public List<String> getOptions() {
            return options;
        }

        public String getHeader() {
            return header;
        }
    }
}
