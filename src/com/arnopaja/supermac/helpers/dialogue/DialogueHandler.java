package com.arnopaja.supermac.helpers.dialogue;

import com.arnopaja.supermac.helpers.Interaction;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

/**
 * Class used to render a dialogue box.
 *
 * A dialogue can conclude with a set of options (they must be at the end of the dialogue).
 * These options are defined by the DialogueOptions class.
 *
 * @author Ari Weiland
 */
public class DialogueHandler {

    private DialogueStyle style = DialogueStyle.WORLD;
    private boolean isDisplaying = false;
    private DialogueWindow window;

    public void render(ShapeRenderer shapeRenderer, SpriteBatch batch) {
        if (isDisplaying) {
            window.render(shapeRenderer, batch);
        }
    }

    public void display(Dialogue dialogue) {
        if (dialogue == null) {
            clear();
        } else {
            isDisplaying = true;
            window = new DialogueWindow(dialogue, style);
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
            return window.onClick(x, y);
        }
        return Interaction.NULL;
    }

    public void clear() {
        isDisplaying = false;
    }

    public DialogueStyle getStyle() {
        return style;
    }

    public void setStyle(DialogueStyle style) {
        this.style = style;
    }

    public boolean isDisplaying() {
        return isDisplaying;
    }

    public DialogueWindow getWindow() {
        return window;
    }
}
