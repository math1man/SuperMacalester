package com.arnopaja.supermac.helpers;

import com.arnopaja.supermac.grid.Grid;
import com.arnopaja.supermac.objects.Dialogue;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

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

    private Vector2 dialogueSize;
    private Vector2 dialoguePosition;
    private Vector2 fontAreaSize;
    private Vector2 fontAreaPosition;

    private DisplayMode mode = DisplayMode.NONE;
    private Dialogue dialogue;
    private String dialogueLine;

    public DialogueHandler(float gameWidth, float gameHeight) {
        // The bottom left corner of the dialogue is the same as that of the game
        dialoguePosition = new Vector2(DIALOGUE_BOX_RENDER_GAP,
                gameHeight * (1 - DIALOGUE_TO_GAME_HEIGHT_RATIO));
        // DialogueHandler size is the width of the game and some fraction of the height
        dialogueSize = new Vector2(gameWidth, gameHeight * DIALOGUE_TO_GAME_HEIGHT_RATIO)
                .sub(2 * DIALOGUE_BOX_RENDER_GAP, DIALOGUE_BOX_RENDER_GAP);
        Vector2 fontOffset = new Vector2(DIALOGUE_BOX_RENDER_GAP, DIALOGUE_BOX_RENDER_GAP);
        fontAreaPosition = dialoguePosition.cpy().add(fontOffset);
        fontAreaSize = dialogueSize.cpy().sub(fontOffset.scl(2));
        AssetLoader.scaleFont(fontAreaSize.y / (AssetLoader.font.getLineHeight() * 3));
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
            shapeRenderer.rect(dialoguePosition.x, dialoguePosition.y, dialogueSize.x, dialogueSize.y);
            shapeRenderer.end();
            if (mode == DisplayMode.DIALOGUE) {
                AssetLoader.drawWrappedFont(batcher, dialogueLine,
                        fontAreaPosition.x, fontAreaPosition.y, fontAreaSize.x);
            } else {
                // render options
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
     * Returns true when no more dialogue exists
     *
     * @param x the x coordinate of the click
     * @param y the y coordinate of the click
     * @return true only when no more dialogue exists
     */
    public boolean onClick(int x, int y) {
        getNextLine();
        return (mode == DisplayMode.NONE);
    }

    public void getNextLine() {
        dialogueLine = dialogue.next();
        if (dialogueLine == null) {
            if (dialogue.hasOptions()) {
                mode = DisplayMode.OPTIONS;
                // TODO: set up options
            } else {
                mode = DisplayMode.NONE;
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
