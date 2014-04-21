package com.arnopaja.supermac.helpers.dialogue;

import com.arnopaja.supermac.helpers.Interaction;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;

/**
 * Class used to render a dialogue box.
 *
 * A dialogue can conclude with a set of options (they must be at the end of the dialogue).
 * These options are defined by the DialogueOptions class.
 *
 * @author Ari Weiland
 */
public class DialogueHandler {

    public static final int TEXT_ROWS = 3;

    public static enum DisplayMode { NONE, DIALOGUE, OPTIONS }

    private final float gameWidth, gameHeight;

    private DisplayMode mode = DisplayMode.NONE;
    private DialogueText text;
    private DialogueOptions options;
    private DialogueWindow window;

    public DialogueHandler(float gameWidth, float gameHeight) {
        this.gameWidth = gameWidth;
        this.gameHeight = gameHeight;
    }

    public void render(ShapeRenderer shapeRenderer, SpriteBatch batch) {
        if (!isNone()) {
            if (window == null) {
                if (isText()) {
                    window = getTextWindow(TEXT_ROWS);
                } else {
                    window = getOptionsWindow(TEXT_ROWS);
                }
            }
            window.render(shapeRenderer, batch);
        }
    }

    private DialogueWindow getTextWindow(int rows) {
        return new DialogueWindow(text, rows, DialogueWindow.FRAME_GAP,
                gameHeight - DialogueWindow.getHeight(rows) - DialogueWindow.FRAME_GAP,
                gameWidth - 2 * DialogueWindow.FRAME_GAP);
    }

    private DialogueWindow getOptionsWindow(int rows) {
        return new DialogueWindow(options, rows, DialogueWindow.FRAME_GAP,
                gameHeight - DialogueWindow.getHeight(rows) - DialogueWindow.FRAME_GAP,
                gameWidth - 2 * DialogueWindow.FRAME_GAP);
    }

    public void display(Dialogue dialogue) {
        if (dialogue == null) {
            clear();
        } else if (dialogue instanceof DialogueText) {
            this.text = (DialogueText) dialogue;
            this.text.reset();
            window = getTextWindow(TEXT_ROWS);
            mode = DisplayMode.DIALOGUE;
        } else {
            this.options = (DialogueOptions) dialogue;
            window = getOptionsWindow(TEXT_ROWS);
            mode = DisplayMode.OPTIONS;
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
    public Interaction onClick(int x, int y) {
        if (isText()) {
            if (text.hasNext()) {
                text.next();
                window = getTextWindow(TEXT_ROWS);
            } else if (text.hasOptions()) {
                display(text.getOptions());
            } else {
                if (text.hasPostInteraction()) {
                    return Interaction.combine(Dialogue.CLEAR_DIALOGUE, text.getPostInteraction());
                }
                return Dialogue.CLEAR_DIALOGUE;
            }
        } else if (isOptions()) {
            for (int i=0; i<options.getCount(); i++) {
                if (getOptionSpaces()[i].contains(x, y)) {
                    return Interaction.combine(Dialogue.CLEAR_DIALOGUE, options.getInteraction(i));
                }
            }
        } else {
            return Dialogue.CLEAR_DIALOGUE;
        }
        return Interaction.NULL;
    }

    private Rectangle[] getOptionSpaces() {
        Rectangle[][] sectors = window.getSectors();
        int rows = sectors[0].length - 1;
        int count = rows * sectors.length;
        Rectangle[] optionSpaces = new Rectangle[count];
        for (int i=0; i<count; i++) {
            int x = i / rows;
            int y = i % rows + 1;
            optionSpaces[i] = sectors[x][y];
        }
        return optionSpaces;
    }

    public void clear() {
        mode = DisplayMode.NONE;
        window = null;
    }

    public boolean isNone() {
        return mode == DisplayMode.NONE;
    }

    public boolean isText() {
        return mode == DisplayMode.DIALOGUE;
    }

    public boolean isOptions() {
        return mode == DisplayMode.OPTIONS;
    }
}
