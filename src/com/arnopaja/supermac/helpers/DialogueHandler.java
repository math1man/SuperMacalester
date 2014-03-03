package com.arnopaja.supermac.helpers;

import com.arnopaja.supermac.objects.Dialogue;
import com.arnopaja.supermac.render.WorldRenderer;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
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

    public static final float DIALOGUE_TO_GAME_HEIGHT_RATIO = 0.4f;

    private Vector2 dialogueSize;
    private Vector2 dialoguePosition;

    private WorldRenderer renderer;

    private boolean displayDialogue = false;
    private Dialogue dialogue;

    public DialogueHandler(WorldRenderer renderer, float gameWidth, float gameHeight) {
        this.renderer = renderer;
        // DialogueHandler size is the width of the game and some fraction of the height
        this.dialogueSize = new Vector2(gameWidth, gameHeight * DIALOGUE_TO_GAME_HEIGHT_RATIO);
        // The bottom left corner of the dialogue is the same as that of the game
        this.dialoguePosition = new Vector2(0, gameHeight * (1 - DIALOGUE_TO_GAME_HEIGHT_RATIO));
    }

    /**
     * Renders the dialogue at the specified position in a rectangle specified by size.
     * TODO: do we instead/additionally need a ShapeRenderer?
     *
     * @param batcher the SpriteBatch to render with
     */
    public void render(SpriteBatch batcher) {
        if (displayDialogue) {
            // render Dialogue lines
        }
    }

    public void displayDialogue(Dialogue dialogue) {
        setDialogue(dialogue);
        // TODO: pause the game somehow?
        displayDialogue = true;
    }

    public void onClick(int x, int y) {
        // TODO: this needs to handle options
        dialogue.next();
    }

    public boolean shouldDisplayDialogue() {
        return displayDialogue;
    }

    public Dialogue getDialogue() {
        return dialogue;
    }

    public void setDialogue(Dialogue dialogue) {
        this.dialogue = dialogue;
    }
}
