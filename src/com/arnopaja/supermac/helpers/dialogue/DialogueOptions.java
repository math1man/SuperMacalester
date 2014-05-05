package com.arnopaja.supermac.helpers.dialogue;

import com.arnopaja.supermac.helpers.interaction.Interaction;
import com.arnopaja.supermac.helpers.interaction.Interactions;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Ari Weiland
 */
public class DialogueOptions extends Dialogue {

    private final List<DialogueMember> members;
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
    public DialogueOptions(String header, List<?> options, List<Interaction> interactions, DialogueStyle style) {
        this("", header, options, interactions, style);
    }

    public DialogueOptions(String name, String header, List<?> options, List<Interaction> interactions, DialogueStyle style) {
        this(name, encapsulate(header, options, interactions), style);
    }

    protected DialogueOptions(String name, List<DialogueMember> members, DialogueStyle style) {
        super(name, style);
        this.members = members;
        count = members.size() - 1;
    }

    private static List<DialogueMember> encapsulate(String header, List<?> options, List<Interaction> interactions) {
        List<DialogueMember> members = new ArrayList<DialogueMember>(options.size() + 1);
        members.add(new DialogueMember(header, Interactions.NULL));
        for (int i=0; i<options.size(); i++) {
            members.add(new DialogueMember(options.get(i).toString(),
                    interactions.size() > i ? interactions.get(i) : Interactions.NULL));
        }
        return members;
    }

    public String getHeader() {
        return members.get(0).getText();
    }

    public DialogueMember getOption(int i) {
        if (i < count && i >= 0) {
            return members.get(i + 1);
        } else {
            return null;
        }
    }

    public Interaction getInteraction(int i) {
        if (i < count) {
            return members.get(i + 1).getInteraction();
        } else {
            return Interactions.NULL;
        }
    }

    public int getCount() {
        return count;
    }

    @Override
    public String getText() {
        return members.toString();
    }

    @Override
    public String getRaw() {
        StringBuilder sb = new StringBuilder();
        for (DialogueMember member : members) {
            sb.append(member.getText());
            sb.append("\n");
        }
        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        return getRaw();
    }
}
