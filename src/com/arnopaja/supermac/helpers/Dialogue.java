package com.arnopaja.supermac.helpers;

import com.arnopaja.supermac.MacGame;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

/**
 * Static class used to render dialogue boxes on the screen
 *
 * @author Ari Weiland
 */
public class Dialogue {

    private static MacGame game;

    private static Vector2 dialogueSize;

    private static boolean displayDialogue = false;
    private static String dialogue;
    private static String[] parsedDialogue;
    private static int position;

    private static boolean giveOptions = false;
    private static String[] options;

    public static void init(MacGame newGame) {
        game = newGame;
    }

    /**
     * Renders the dialogue at the specified position in a rectangle specified by size.
     * TODO: do we instead/additionally need a ShapeRenderer?
     *
     * @param batcher the SpriteBatch to render with
     * @param position the upper lefthand corner of teh dialogue box
     * @param size the size of the rectangle containing the dialogue box
     */
    public static void render(SpriteBatch batcher, Vector2 position, Vector2 size) {
        if (displayDialogue) {
            dialogueSize = size;
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
