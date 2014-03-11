package com.arnopaja.supermac.helpers;

import com.arnopaja.supermac.grid.Grid;
import com.arnopaja.supermac.objects.Interaction;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;

/**
 * Static class used to render a dialogue box on the WorldScreen
 *
 * A dialogue can conclude with a set of options (they must be at the end of the dialogue).
 * A dialogue can also or alternatively conclude by going to a battle.
 *
 * @author Ari Weiland
 */
public class DialogueHandler {

    public static final float DIALOGUE_TO_GAME_HEIGHT_RATIO = 0.3f;
    public static final float DIALOGUE_BOX_RENDER_GAP = 0.5f * Grid.GRID_PIXEL_DIMENSION;

    public enum DisplayMode { NONE, DIALOGUE, OPTIONS }

    private final Rectangle dialogueSpace;
    private final Rectangle fontSpace;
    private final Rectangle option1;
    private final Rectangle option2;
    private final Rectangle option3;
    private final Rectangle option4;

    private DisplayMode mode = DisplayMode.NONE;
    private Dialogue dialogue;
    private String dialogueLine;
    private DialogueOptions options;

    public DialogueHandler(float gameWidth, float gameHeight) {
        dialogueSpace = new Rectangle(
                DIALOGUE_BOX_RENDER_GAP,
                gameHeight * (1 - DIALOGUE_TO_GAME_HEIGHT_RATIO), 
                gameWidth - 2 * DIALOGUE_BOX_RENDER_GAP, 
                gameHeight * DIALOGUE_TO_GAME_HEIGHT_RATIO - DIALOGUE_BOX_RENDER_GAP);
        fontSpace = new Rectangle(
                dialogueSpace.getX() + DIALOGUE_BOX_RENDER_GAP,
                dialogueSpace.getY() + DIALOGUE_BOX_RENDER_GAP,
                dialogueSpace.getWidth() - 2 * DIALOGUE_BOX_RENDER_GAP,
                dialogueSpace.getHeight() - 2 * DIALOGUE_BOX_RENDER_GAP);
        AssetLoader.scaleFont(fontSpace.getHeight() / (AssetLoader.font.getLineHeight() * 3));

        option1 = new Rectangle(
                fontSpace.getX(),
                fontSpace.getY() + fontSpace.getHeight() / 3,
                fontSpace.getX() + fontSpace.getWidth() / 2,
                fontSpace.getY() + fontSpace.getHeight() * 2/3);
        option2 = new Rectangle(
                fontSpace.getX() + fontSpace.getWidth() / 2,
                fontSpace.getY() + fontSpace.getHeight() / 3,
                fontSpace.getX() + fontSpace.getWidth(),
                fontSpace.getY() + fontSpace.getHeight() * 2/3);
        option3 = new Rectangle(
                fontSpace.getX(),
                fontSpace.getY() + fontSpace.getHeight() * 2/3,
                fontSpace.getX() + fontSpace.getWidth() / 2,
                fontSpace.getY() + fontSpace.getHeight());
        option4 = new Rectangle(
                fontSpace.getX() + fontSpace.getWidth() / 2,
                fontSpace.getY() + fontSpace.getHeight() * 2/3,
                fontSpace.getX() + fontSpace.getWidth(),
                fontSpace.getY() + fontSpace.getHeight());
    }

    /**
     * Renders the dialogue at the specified position in a rectangle specified by size.
     *
     * @param batcher the SpriteBatch to render with
     */
    public void render(ShapeRenderer shapeRenderer, SpriteBatch batcher) {
        if (mode != DisplayMode.NONE) {
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(1, 1, 1, 0.8f);
            shapeRenderer.rect(dialogueSpace.getX(), dialogueSpace.getY(),
                    dialogueSpace.getWidth(), dialogueSpace.getHeight());
            shapeRenderer.end();
            if (mode == DisplayMode.DIALOGUE) {
                AssetLoader.drawWrappedFont(batcher, dialogueLine,
                        fontSpace.getX(), fontSpace.getY(), fontSpace.getWidth());
            } else {
                AssetLoader.drawFont(batcher, options.getHeader(), fontSpace.getX(), fontSpace.getY());
                AssetLoader.drawFont(batcher, options.getOption(1), option1.x, option2.y);
                AssetLoader.drawFont(batcher, options.getOption(2), option2.x, option2.y);
                AssetLoader.drawFont(batcher, options.getOption(3), option3.x, option3.y);
                AssetLoader.drawFont(batcher, options.getOption(4), option4.x, option4.y);
            }
        }
    }

    public void displayDialogue(Dialogue dialogue) {
        setDialogue(dialogue);
        mode = DisplayMode.DIALOGUE;
        dialogue.reset();
        getNextLine();
    }

    /**
     * Moves on to the next bit of dialogue.
     * Returns null if the dialogue is to continue, or the resulting interaction.
     *
     * @param x the x coordinate of the click
     * @param y the y coordinate of the click
     * @return null if the dialogue is to continue, or the resulting interaction
     */
    public Interaction onClick(int x, int y) {
        if (mode == DisplayMode.OPTIONS) {
            if (option1.contains(x, y)) {
                mode = DisplayMode.NONE;
                return options.getInteraction(0);
            } else if (option2.contains(x, y)) {
                mode = DisplayMode.NONE;
                return options.getInteraction(1);
            } else if (option3.contains(x, y) && options.getCount() > 2) {
                mode = DisplayMode.NONE;
                return options.getInteraction(2);
            } else if (option4.contains(x, y) && options.getCount() > 3) {
                mode = DisplayMode.NONE;
                return options.getInteraction(3);
            }
        } else {
            getNextLine();
        }
        return (mode == DisplayMode.NONE ? Interaction.getNullInteraction() : null);
    }

    public void getNextLine() {
        dialogueLine = dialogue.next();
        if (dialogueLine == null) {
            if (dialogue.hasOptions() && (mode != DisplayMode.OPTIONS)) {
                mode = DisplayMode.OPTIONS;
                options = dialogue.getOptions();
            } else {
                clear();
            }
        }
    }

    public void clear() {
        mode = DisplayMode.NONE;
    }

    public DisplayMode getMode() {
        return mode;
    }

    public Dialogue getDialogue() {
        return dialogue;
    }

    public void setDialogue(Dialogue dialogue) {
        this.dialogue = dialogue;
    }
}
