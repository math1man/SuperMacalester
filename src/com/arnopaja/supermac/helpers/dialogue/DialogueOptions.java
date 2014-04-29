package com.arnopaja.supermac.helpers.dialogue;

import com.arnopaja.supermac.helpers.Interaction;

import java.util.Arrays;

/**
 * @author Ari Weiland
 */
public class DialogueOptions extends Dialogue {

    public static final String[] YES_NO_OPTIONS = {"Yes", "No"};
    public static final String[] BATTLE_OPTIONS = {"Attack", "Defend", "Spell", "Item", "Flee"};

    private final DialogueMember[] members;
    private final int count;

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
    public <T> DialogueOptions(String header, T[] options, DialogueStyle style, Interaction... interactions) {
        this(encapsulate(header, options, interactions), style);
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
    public <T> DialogueOptions(String header, T[] options, Interaction[] interactions, DialogueStyle style) {
        this(encapsulate(header, options, interactions), style);
    }

    protected DialogueOptions(DialogueMember[] members, DialogueStyle style) {
        super(style);
        this.members = members;
        count = members.length - 1;
    }

    private static <T> DialogueMember[] encapsulate(String header, T[] options, Interaction[] interactions) {
        DialogueMember[] members = new DialogueMember[options.length + 1];
        members[0] = new DialogueMember(header);
        for (int i=0; i<options.length; i++) {
            members[i+1] = new DialogueMember(options[i].toString(), interactions.length > i ? interactions[i] : Interaction.NULL);
        }
        return members;
    }

    public String getHeader() {
        return members[0].getText();
    }

    public DialogueMember getOption(int i) {
        if (i < count && i >= 0) {
            return members[i+1];
        } else {
            return null;
        }
    }

    public Interaction getInteraction(int i) {
        if (i < count) {
            return members[i+1].getInteraction();
        } else {
            return Interaction.NULL;
        }
    }

    public int getCount() {
        return count;
    }

    @Override
    public String getText() {
        return Arrays.toString(members);
    }

    @Override
    public String getRaw() {
        StringBuilder sb = new StringBuilder();
        for (DialogueMember member : members) {
            sb.append(member.getText());
            sb.append("\n");
        }
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }
}
