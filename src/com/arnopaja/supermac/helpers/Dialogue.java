package com.arnopaja.supermac.helpers;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Ari Weiland
 */
public class Dialogue {

    private final String rawDialogue;
    private final List<String> parsedDialogue;
    private int position;

    private final boolean hasOptions;
    private final DialogueOptions options;

    public Dialogue(String rawDialogue) {
        this(rawDialogue, (DialogueOptions) null);
    }

    public Dialogue(String rawDialogue, String rawOptions) {
        this(rawDialogue, new DialogueOptions(rawOptions));
    }

    public Dialogue(String rawDialogue, DialogueOptions options) {
        this.rawDialogue = rawDialogue;
        String[] lines = rawDialogue.split("[\n.]");
        parsedDialogue = new ArrayList<String>(lines.length);
        for (String line : lines) {
            line = line.trim();
            if (!line.isEmpty()) {
                parsedDialogue.add(line + ".");
            }
        }
        hasOptions = (options != null);
        this.options = options;
    }

    public void reset() {
        position = 0;
    }

    public String next() {
        if (position < parsedDialogue.size()) {
            String line = parsedDialogue.get(position);
            position++;
            return line;
        } else {
            return null;
        }
    }

    public String getRawDialogue() {
        return rawDialogue;
    }

    public boolean hasOptions() {
        return hasOptions;
    }

    public DialogueOptions getOptions() {
        return options;
    }

    @Override
    public String toString() {
        return rawDialogue;
    }
}
