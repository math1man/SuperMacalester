package com.arnopaja.supermac.helpers;

import com.arnopaja.supermac.screen.WorldScreen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Static class used to render a dialogue box on the WorldScreen
 *
 * A dialogue can conclude with a set of options (they must be at the end of the dialogue).
 * A dialogue can also or alternatively conclude by going to a battle.
 *
 * @author Ari Weiland
 */
public class Dialogue {

    public static final float DIALOGUE_TO_GAME_HEIGHT_RATIO = 0.4f;

    private static Vector2 dialogueSize;
    private static Vector2 dialoguePosition;

    private static WorldScreen worldScreen;

    private static boolean displayDialogue = false;
    private static String dialogue;
    private static List<String> parsedDialogue;
    private static int position;
    private static boolean goToBattleAfterDialogue = false;

    private static boolean hasOptions = false;
    private static boolean displayOptions = false;
    private static List<String> options;

    public static void init(WorldScreen screen, float gameWidth, float gameHeight) {
        worldScreen = screen;
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
            if (displayOptions) {
                // render options
            } else {
                // render narration
            }
        }
    }

    public static void displayDialogue(String dialogue) {
        setDialogue(dialogue);
        displayDialogue();
    }

    public static void displayDialogue() {
        parseDialogue();
        position = 0;
        worldScreen.setRunning(false);
        displayDialogue = true;
    }

    public static void onClick(int x, int y) {
        if (displayOptions) {
            // deal with selecting options
        } else {
            position++;
            if (position >= parsedDialogue.size()) {
                if (hasOptions && !displayOptions) {
                    displayOptions = true;
                } else {
                    displayDialogue = false;
                    displayOptions = false;
                    worldScreen.setRunning(true);
                }
            }
        }
        if (goToBattleAfterDialogue) {
            worldScreen.goToBattle();
        }
    }

    public static void parseDialogue() {
        // converts the basic dialogue string into a
        // sequence of strings that are displayable
        String[] lines = dialogue.split("\n");
        parsedDialogue = new ArrayList<String>(lines.length);
        int beginOptionIndex = 0, endOptionIndex = lines.length;
        int optionIndex = 0;
        options = new ArrayList<String>(4);
        goToBattleAfterDialogue = false;
        for (int i=0; i<lines.length; i++) {
            String line = lines[i];
            if (line.contains("<options>")) {
                beginOptionIndex = i;
            } else if (line.contains("</options>")) {
                endOptionIndex = i;
            } else if (i > beginOptionIndex && i < endOptionIndex
                    && Pattern.matches("<option:*>", line) && optionIndex < 4) {
                String option = line.substring(8, line.length() - 1);
                options.add(option);
                optionIndex++;
            } else if (line.contains("<battle>")) {
                goToBattleAfterDialogue = true;
            } else {
                // TODO: update this else to properly set up the line
                parsedDialogue.add(line);
            }
        }
    }

    public static WorldScreen getWorldScreen() {
        return worldScreen;
    }

    public static boolean shouldDisplayDialogue() {
        return displayDialogue;
    }

    public static String getDialogue() {
        return dialogue;
    }

    public static void setDialogue(String dialogue) {
        Dialogue.dialogue = dialogue;
    }

    public static boolean hasOptions() {
        return hasOptions;
    }

    public static void setHasOptions(boolean hasOptions) {
        Dialogue.hasOptions = hasOptions;
    }

    public static List<String> getOptions() {
        return options;
    }
}
