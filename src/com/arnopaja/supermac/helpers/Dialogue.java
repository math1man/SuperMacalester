package com.arnopaja.supermac.helpers;

/**
 * @author Ari Weiland
 */
public class Dialogue implements DialogueDisplayable {

    private final String rawDialogue;
    private final String[] dialogue;
    private final boolean hasOptions;
    private final DialogueOptions options;

    private int position;
    private String currentDialogue;
    private boolean hasNext;

    public Dialogue(String rawDialogue) {
        this(rawDialogue, null);
    }

    public Dialogue(String rawDialogue, DialogueOptions options) {
        this.rawDialogue = rawDialogue;
        this.dialogue = rawDialogue.split("<d>");
        this.hasOptions = (options != null);
        this.options = options;
        reset();
    }

    public void next() {
        position++;
        currentDialogue = dialogue[position];
        hasNext = dialogue.length > 1 + position;
    }

    public String getRawDialogue() {
        return rawDialogue;
    }

    public String[] getDialogue() {
        return dialogue;
    }

    public boolean hasOptions() {
        return hasOptions;
    }

    public DialogueOptions getOptions() {
        return options;
    }

    public String getCurrentDialogue() {
        return currentDialogue.trim();
    }

    public boolean hasNext() {
        return hasNext;
    }

    public void reset() {
        position = 0;
        currentDialogue = dialogue[0];
        hasNext = dialogue.length > 1;
    }

    @Override
    public String toString() {
        return rawDialogue;
    }
}
