package com.arnopaja.supermac.helpers.dialogue;

import com.arnopaja.supermac.GameScreen;
import com.arnopaja.supermac.world.grid.Grid;

/**
* @author Ari Weiland
*/
public enum DialogueStyle {

    WORLD(DialogueWindow.FRAME_GAP,
            GameScreen.GAME_HEIGHT - DialogueWindow.getHeight(3) - DialogueWindow.FRAME_GAP,
            GameScreen.GAME_WIDTH - 2 * DialogueWindow.FRAME_GAP,
            3),
    BATTLE_CONSOLE(Grid.GRID_PIXEL_DIMENSION / 2,
            GameScreen.GAME_HEIGHT - DialogueWindow.getHeight(4) - DialogueWindow.FRAME_GAP,
            GameScreen.GAME_WIDTH / 2 - DialogueWindow.FRAME_GAP,
            4),
    BATTLE_STATUS(GameScreen.GAME_WIDTH / 2,
            GameScreen.GAME_HEIGHT - DialogueWindow.getHeight(4) - DialogueWindow.FRAME_GAP,
            GameScreen.GAME_WIDTH / 2 - DialogueWindow.FRAME_GAP,
            4);

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
}