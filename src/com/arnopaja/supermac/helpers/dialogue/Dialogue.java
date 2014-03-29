package com.arnopaja.supermac.helpers.dialogue;

import com.arnopaja.supermac.helpers.Interaction;

import java.util.Arrays;

/**
 * @author Ari Weiland
 */
public class Dialogue implements DialogueDisplayable {

    private final String[] dialogue;
    private final boolean hasOptions;
    private final DialogueOptions options;
    private final boolean hasPostInteraction;
    private final Interaction postInteraction;

    private int position;
    private String currentDialogue;
    private boolean hasNext;

    public Dialogue(String rawDialogue) {
        this(rawDialogue.split("<d>"));
    }

    public Dialogue(String[] dialogue) {
        this(dialogue, null, null);
    }

    public Dialogue(String rawDialogue, Interaction postInteraction) {
        this(rawDialogue.split("<d>"), postInteraction);
    }

    public Dialogue(String[] dialogue, Interaction postInteraction) {
        this(dialogue, null, postInteraction);
    }

    public Dialogue(String rawDialogue, DialogueOptions options) {
        this(rawDialogue.split("<d>"), options);
    }

    public Dialogue(String[] dialogue, DialogueOptions options) {
        this(dialogue, options, null);
    }

    private Dialogue(String[] dialogue, DialogueOptions options, Interaction postInteraction) {
        this.dialogue = dialogue;
        this.hasOptions = (options != null);
        this.options = options;
        this.hasPostInteraction = (postInteraction != null);
        this.postInteraction = postInteraction;
        reset();
    }

    public void next() {
        position++;
        currentDialogue = dialogue[position];
        hasNext = position + 1 < dialogue.length ;
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


    public boolean hasPostInteraction() {
        return hasPostInteraction;
    }

    public Interaction getPostInteraction() {
        return postInteraction;
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
        return Arrays.toString(dialogue);
    }
}
