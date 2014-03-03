package com.arnopaja.supermac.helpers;

import com.arnopaja.supermac.MacGame;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

/**
 * Static class used to render a dialogue box on the WorldScreen
 *
 * @author Ari Weiland
 */
public class Dialogue {

    public static final float DIALOGUE_TO_GAME_HEIGHT_RATIO = 0.4f;

    private static MacGame game;

    private static Vector2 dialogueSize;
    private static Vector2 dialoguePosition;

    private static boolean displayDialogue = false;
    private static String dialogue;
    private static String[] parsedDialogue;
    private static int position;

    private static boolean giveOptions = false;
    private static String[] options;

    public static void init(MacGame newGame, float gameWidth, float gameHeight) {
        game = newGame;
        // Dialogue size is the width of the game and some fraction of the height
        dialogueSize = new Vector2(gameWidth, gameHeight * DIALOGUE_TO_GAME_HEIGHT_RATIO);
        // The bottom left corner of the dialogue is the same as that of the game
        dialoguePosition = new Vector2(0, gameHeight * (1 - DIALOGUE_TO_GAME_HEIGHT_RATIO));
    }

    /**
     * Renders the dialogue at the specified position in a rectangle specified by size.
     * TODO: do we instead/additionally need a ShapeRenderer?
     *
     * @param batcher the SpriteBatch to render with
     */
    public static void render(SpriteBatch batcher) {
        if (displayDialogue) {
            // render code goes here
        }
    }

    public static void displayDialogue() {
        parseDialogue();
        position = 0;
        displayDialogue = true;
        // TODO: pause game, etc?
    }

    public static void onClick(int x, int y) {
        if (giveOptions) {
            // deal with options
        } else {
            position++;
        }
    }

    public static void parseDialogue() {
        // converts the basic dialogue string into a
        // sequence of strings that are displayable
    }

    public static MacGame getGame() {
        return game;
    }

    public static boolean isDisplayDialogue() {
        return displayDialogue;
    }

    public static void setDisplayDialogue(boolean displayDialogue) {
        Dialogue.displayDialogue = displayDialogue;
    }

    public static String getDialogue() {
        return dialogue;
    }

    public static void setDialogue(String dialogue) {
        Dialogue.dialogue = dialogue;
    }

    public static boolean isGiveOptions() {
        return giveOptions;
    }

    public static void setGiveOptions(boolean giveOptions) {
        Dialogue.giveOptions = giveOptions;
    }

    public static String[] getOptions() {
        return options;
    }

    public static void setOptions(String[] options) {
        Dialogue.options = options;
    }
}
