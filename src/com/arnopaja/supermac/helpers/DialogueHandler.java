package com.arnopaja.supermac.helpers;

import com.arnopaja.supermac.world.grid.Grid;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Class used to render a dialogue box.
 *
 * A dialogue can conclude with a set of options (they must be at the end of the dialogue).
 * These options are defined by the DialogueOptions class.
 *
 * @author Ari Weiland
 */
public class DialogueHandler {

    public static final float DIALOGUE_TO_GAME_HEIGHT_RATIO = 0.3f;
    public static final float DIALOGUE_BOX_RENDER_GAP = 0.5f * Grid.GRID_PIXEL_DIMENSION;

    public enum DisplayMode { NONE, DIALOGUE, OPTIONS }

    private final Rectangle dialogueSpace;
    private final Rectangle fontSpace;
    private final Rectangle option0;
    private final Rectangle option1;
    private final Rectangle option2;
    private final Rectangle option3;

    private final Queue<DialogueDisplayable> dialogueQueue = new ConcurrentLinkedQueue<DialogueDisplayable>();

    private DisplayMode mode = DisplayMode.NONE;
    private Dialogue dialogue;
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

        option0 = new Rectangle(
                fontSpace.getX(),
                fontSpace.getY() + fontSpace.getHeight() / 3,
                fontSpace.getWidth() / 2,
                fontSpace.getHeight() / 3);
        option1 = new Rectangle(
                fontSpace.getX() + fontSpace.getWidth() / 2,
                fontSpace.getY() + fontSpace.getHeight() / 3,
                fontSpace.getWidth() / 2,
                fontSpace.getHeight() / 3);
        option2 = new Rectangle(
                fontSpace.getX(),
                fontSpace.getY() + fontSpace.getHeight() * 2/3,
                fontSpace.getWidth() / 2,
                fontSpace.getHeight() / 3);
        option3 = new Rectangle(
                fontSpace.getX() + fontSpace.getWidth() / 2,
                fontSpace.getY() + fontSpace.getHeight() * 2/3,
                fontSpace.getWidth() / 2,
                fontSpace.getHeight() / 3);
    }

    /**
     * Renders the dialogue at the specified position in a rectangle specified by size.
     *
     * @param batcher the SpriteBatch to render with
     */
    public void render(ShapeRenderer shapeRenderer, SpriteBatch batcher) {
        if (!isNone()) {
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(1, 1, 1, 0.8f);
            shapeRenderer.rect(dialogueSpace.getX(), dialogueSpace.getY(),
                    dialogueSpace.getWidth(), dialogueSpace.getHeight());
            shapeRenderer.end();
            if (isDialogue()) {
                AssetLoader.drawWrappedFont(batcher, dialogue.getCurrentDialogue(),
                        fontSpace.getX(), fontSpace.getY(), fontSpace.getWidth());
            } else {
                AssetLoader.drawFont(batcher, options.getHeader(), fontSpace.getX(), fontSpace.getY());
                AssetLoader.drawFont(batcher, options.getOption(0), option0.x, option0.y);
                AssetLoader.drawFont(batcher, options.getOption(1), option1.x, option1.y);
                AssetLoader.drawFont(batcher, options.getOption(2), option2.x, option2.y);
                AssetLoader.drawFont(batcher, options.getOption(3), option3.x, option3.y);
            }
        }
    }

    public void displayDialogue(DialogueDisplayable displayableDialogue) {
        dialogueQueue.add(displayableDialogue);
        if (isNone()) {
            pollQueue();
        }
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
        if (isDialogue()) {
            if (dialogue.hasNext()) {
                dialogue.next();
            } else if (dialogue.hasOptions()) {
                displayDialogue(dialogue.getOptions());
            } else {
                pollQueue();
            }
        } else if (isOptions()) {
            if (option0.contains(x, y)) {
                pollQueue();
                return options.getInteraction(0);
            } else if (option1.contains(x, y)) {
                pollQueue();
                return options.getInteraction(1);
            } else if (option2.contains(x, y) && options.getCount() > 2) {
                pollQueue();
                return options.getInteraction(2);
            } else if (option3.contains(x, y) && options.getCount() > 3) {
                pollQueue();
                return options.getInteraction(3);
            }
        }
        return isNone() ? Interaction.clearDialogue() : Interaction.getNull();
    }

    private void pollQueue() {
        if (dialogueQueue.isEmpty()) {
            clear();
        } else {
            DialogueDisplayable displayableDialogue = dialogueQueue.poll();
            if (displayableDialogue instanceof Dialogue) {
                this.dialogue = ((Dialogue) displayableDialogue);
                mode = DisplayMode.DIALOGUE;
            } else {
                dialogue = null;
                mode = DisplayMode.OPTIONS;
                this.options = ((DialogueOptions) displayableDialogue);
            }
        }
    }

    public void clear() {
        mode = DisplayMode.NONE;
        dialogueQueue.clear();
    }

    public boolean isNone() {
        return mode == DisplayMode.NONE;
    }

    public boolean isDialogue() {
        return mode == DisplayMode.DIALOGUE;
    }

    public boolean isOptions() {
        return mode == DisplayMode.OPTIONS;
    }

    public Dialogue getDialogue() {
        return dialogue;
    }
}
