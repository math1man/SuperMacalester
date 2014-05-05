package com.arnopaja.supermac.helpers.interaction;

import com.arnopaja.supermac.GameScreen;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Ari Weiland
 */
public class MultiInteraction implements Interaction {

    private final List<Interaction> interactions;

    public MultiInteraction() {
        this(new ArrayList<Interaction>());
    }

    public MultiInteraction(Interaction... interactions) {
        this(Arrays.asList(interactions));
    }

    public MultiInteraction(List<Interaction> interactions) {
        this.interactions = new ArrayList<Interaction>(interactions);
        purgeNulls();
    }

    public void attach(Interaction interaction) {
        if (interaction != null && interaction != Interactions.NULL) {
            interactions.add(interaction);
        }
    }

    public void attach(MultiInteraction multi) {
        attach(multi.interactions);
    }

    public void attach(Interaction... interactions) {
        attach(Arrays.asList(interactions));
    }

    public void attach(List<Interaction> interactions) {
        this.interactions.addAll(interactions);
        purgeNulls();
    }

    public List<Interaction> getInteractions() {
        return interactions;
    }

    private void purgeNulls() {
        while (interactions.contains(null)) {
            interactions.remove(null);
        }
        while(interactions.contains(Interactions.NULL)) {
            interactions.remove(Interactions.NULL);
        }
    }

    @Override
    public void run(GameScreen screen) {
        if (!interactions.isEmpty()) {
            for (Interaction interaction : interactions) {
                interaction.run(screen);
            }
        }
    }

    @Override
    public String toString() {
        return interactions.toString();
    }
}
