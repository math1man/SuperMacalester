package com.arnopaja.supermac.helpers.dialogue;

import com.arnopaja.supermac.helpers.interaction.Interaction;
import com.arnopaja.supermac.helpers.interaction.Interactions;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.util.EnumMap;

/**
 * Class used to render a dialogue box.
 *
 * A dialogue can conclude with a set of options (they must be at the end of the dialogue).
 * These options are defined by the DialogueOptions class.
 *
 * @author Ari Weiland
 */
public class DialogueHandler {

    private final EnumMap<DialogueStyle, DialogueWindow> windows = new EnumMap<DialogueStyle, DialogueWindow>(DialogueStyle.class);
    private boolean isDisplaying = false;

    public void render(ShapeRenderer shapeRenderer, SpriteBatch batch) {
        if (isDisplaying) {
            for (DialogueWindow window : windows.values()) {
                window.render(shapeRenderer, batch);
            }
        }
    }

    public void display(Dialogue dialogue) {
        if (dialogue != null) {
            isDisplaying = true;
            windows.put(dialogue.getStyle(), new DialogueWindow(dialogue));
        }
    }

    /**
     * Moves on to the next bit of dialogue.
     * Returns NULL if the dialogue is to continue, or the resulting interaction.
     *
     * @param x the x coordinate of the click
     * @param y the y coordinate of the click
     * @return NULL if the dialogue is to continue, or the resulting interaction
     */
    public Interaction onClick(float x, float y) {
        if (isDisplaying) {
            for (DialogueWindow window : windows.values()) {
                Interaction interaction = window.onClick(x, y);
                if (interaction != null && interaction != Interactions.NULL) {
                    System.out.println(window);
                    System.out.println(interaction);
                    return interaction;
                }
            }
        }
        return Interactions.NULL;
    }

    public void clear() {
        isDisplaying = false;
        windows.clear();
    }

    public void clear(DialogueStyle style) {
        windows.remove(style);
        if (windows.isEmpty()) {
            isDisplaying = false;
        }
    }

    public boolean isDisplaying() {
        return isDisplaying;
    }

    public DialogueWindow getWindow(DialogueStyle style) {
        return windows.get(style);
    }
}
