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

    public static enum DisplayMode { NONE, DIALOGUE, OPTIONS }

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

    public void render(ShapeRenderer shapeRenderer, SpriteBatch batch) {
        if (!isNone()) {
            batch.flush();
            renderRectangle(shapeRenderer);
            if (isDialogue()) {
                renderDialogue(batch);
            } else  {
                renderOptions(batch);
            }
        }
    }

    private void renderRectangle(ShapeRenderer shapeRenderer) {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(1, 1, 1, 0.8f);
        shapeRenderer.rect(dialogueSpace.getX(), dialogueSpace.getY(),
                dialogueSpace.getWidth(), dialogueSpace.getHeight());
        shapeRenderer.end();
    }

    private void renderDialogue(SpriteBatch batch) {
        AssetLoader.drawWrappedFont(batch, dialogue.getCurrentDialogue(),
                fontSpace.getX(), fontSpace.getY(), fontSpace.getWidth());
    }

    private void renderOptions(SpriteBatch batch) {
        AssetLoader.drawFont(batch, options.getHeader(), fontSpace.getX(), fontSpace.getY());
        AssetLoader.drawFont(batch, options.getOption(0), option0.getX(), option0.getY());
        AssetLoader.drawFont(batch, options.getOption(1), option1.getX(), option1.getY());
        AssetLoader.drawFont(batch, options.getOption(2), option2.getX(), option2.getY());
        AssetLoader.drawFont(batch, options.getOption(3), option3.getX(), option3.getY());
    }

    public void displayDialogue(DialogueDisplayable displayable) {
        dialogueQueue.add(displayable);
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
                display(dialogue.getOptions());
            } else {
                pollQueue();
            }
        } else if (isOptions()) {
            if (option0.contains(x, y)) {
                pollQueue();
                return Interaction.combine(Interaction.clearDialogue(), options.getInteraction(0));
            } else if (option1.contains(x, y)) {
                pollQueue();
                return Interaction.combine(Interaction.clearDialogue(), options.getInteraction(1));
            } else if (option2.contains(x, y) && options.getCount() > 2) {
                pollQueue();
                return Interaction.combine(Interaction.clearDialogue(), options.getInteraction(2));
            } else if (option3.contains(x, y) && options.getCount() > 3) {
                pollQueue();
                return Interaction.combine(Interaction.clearDialogue(), options.getInteraction(3));
            }
        }
        return isNone() ? Interaction.clearDialogue() : Interaction.getNull();
    }

    private void pollQueue() {
        if (dialogueQueue.isEmpty()) {
            clear();
        } else {
            DialogueDisplayable displayable = dialogueQueue.poll();
            if (displayable instanceof Dialogue) {
               display((Dialogue) displayable);
            } else if (displayable instanceof DialogueOptions) {
                display((DialogueOptions) displayable);
            } else {
                pollQueue();
            }
        }
    }

    private void display(Dialogue dialogue) {
        this.dialogue = dialogue;
        this.dialogue.reset();
        mode = DisplayMode.DIALOGUE;
    }

    private void display(DialogueOptions options) {
        this.options = options;
        mode = DisplayMode.OPTIONS;
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
