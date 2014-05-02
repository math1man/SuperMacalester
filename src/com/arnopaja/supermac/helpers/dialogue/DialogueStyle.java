package com.arnopaja.supermac.helpers.dialogue;

import com.arnopaja.supermac.GameScreen;
import com.arnopaja.supermac.helpers.Interaction;

/**
* @author Ari Weiland
*/
public enum DialogueStyle {

    WORLD(DialogueWindow.FRAME_GAP,
            GameScreen.GAME_HEIGHT - DialogueWindow.getHeight(3) - DialogueWindow.FRAME_GAP,
            GameScreen.GAME_WIDTH - 2 * DialogueWindow.FRAME_GAP,
            3),
    BATTLE_CONSOLE(0, GameScreen.GAME_HEIGHT - DialogueWindow.getHeight(4),
            GameScreen.GAME_WIDTH / 2,
            4),
    BATTLE_STATUS(GameScreen.GAME_WIDTH / 2, GameScreen.GAME_HEIGHT - DialogueWindow.getHeight(4),
            GameScreen.GAME_WIDTH / 2,
            4),
    BATTLE_STATUS_LEFT(0, 0,
            GameScreen.GAME_WIDTH / 3,
            8),
    BATTLE_STATUS_RIGHT(GameScreen.GAME_WIDTH * 2 / 3, 0,
            GameScreen.GAME_WIDTH / 3,
            8),
    FULL_SCEEN(DialogueWindow.FRAME_GAP,
            DialogueWindow.FRAME_GAP,
            GameScreen.GAME_WIDTH - 2 * DialogueWindow.FRAME_GAP,
            12);

    private final float x;
    private final float y;
    private final float width;
    private final int rows;

    DialogueStyle(float x, float y, float width, int rows) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.rows = rows;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getWidth() {
        return width;
    }

    public int getRows() {
        return rows;
    }

    public Interaction clear() {
        final DialogueStyle style = this;
        return new Interaction(style) {
            @Override
            public void run(GameScreen screen) {
                screen.getDialogueHandler().clear(style);
            }
        };
    }

    public static Interaction clearAll() {
        return new Interaction() {
            @Override
            public void run(GameScreen screen) {
                screen.getDialogueHandler().clear();
            }
        };
    }
}
