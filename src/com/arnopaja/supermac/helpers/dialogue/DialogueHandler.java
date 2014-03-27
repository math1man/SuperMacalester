package com.arnopaja.supermac.helpers.dialogue;

import com.arnopaja.supermac.helpers.AssetLoader;
import com.arnopaja.supermac.helpers.Interaction;
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
    public static final int TEXT_ROWS = 3;
    public static final int OPTION_ROWS = TEXT_ROWS - 1;
    public static final int MAX_OPTION_COLUMNS = 5;

    public static enum DisplayMode { NONE, DIALOGUE, OPTIONS }

    private final Rectangle dialogueSpace;
    private final Rectangle fontSpace;
    private final Rectangle[][] optionSpaces;

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
        AssetLoader.scaleFont(fontSpace.getHeight() / (AssetLoader.font.getLineHeight() * TEXT_ROWS));

        optionSpaces = initOptionRects(fontSpace.getX(), fontSpace.getY(),
                    fontSpace.getWidth(), fontSpace.getHeight());
    }

    private static Rectangle[][] initOptionRects(float x, float y, float width, float height) {
        Rectangle[][] rects = new Rectangle[MAX_OPTION_COLUMNS][];
        for (int i=0; i<MAX_OPTION_COLUMNS; i++) {
            rects[i] = new Rectangle[OPTION_ROWS * (i + 1)];
            float rectWidth = width / (i + 1);
            float rectHeight = height / TEXT_ROWS;
            for (int j=0; j<=i; j++) {
                for (int k=0; k<OPTION_ROWS; k++) {
                    rects[i][OPTION_ROWS * j + k] = new Rectangle(x + j*rectWidth,
                            y + (k+1)*rectHeight, rectWidth, rectHeight);
                }
            }
        }
        return rects;
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
        int count = options.getCount();
        Rectangle[] spaces = getOptionSpaces(count);
        for (int i=0; i<count; i++) {
            AssetLoader.drawFont(batch, options.getOption(i), spaces[i].getX(), spaces[i].getY());
        }
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
            int count = options.getCount();
            for (int i=0; i<count; i++) {
                if (getOptionSpaces(count)[i].contains(x, y)) {
                    pollQueue();
                    return Interaction.combine(Interaction.clearDialogue(), options.getInteraction(i));
                }
            }
        }
        return isNone() ? Interaction.clearDialogue() : Interaction.NULL;
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

    private Rectangle[] getOptionSpaces(int count) {
        int index = (count - 1) / OPTION_ROWS;
        return optionSpaces[index];
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
