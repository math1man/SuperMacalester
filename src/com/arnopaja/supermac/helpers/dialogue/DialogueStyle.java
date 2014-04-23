package com.arnopaja.supermac.helpers.dialogue;

import com.arnopaja.supermac.GameScreen;
import com.arnopaja.supermac.world.grid.Grid;

/**
* @author Ari Weiland
*/
public enum DialogueStyle {

    BOTTOM(DialogueWindow.FRAME_GAP,
            GameScreen.GAME_HEIGHT - DialogueWindow.getHeight(3) - DialogueWindow.FRAME_GAP,
            GameScreen.GAME_WIDTH - 2 * DialogueWindow.FRAME_GAP,
            3),
    BOTTOM_LEFT(Grid.GRID_PIXEL_DIMENSION / 2,
            GameScreen.GAME_HEIGHT - DialogueWindow.getHeight(3) - DialogueWindow.FRAME_GAP,
            GameScreen.GAME_WIDTH / 2 - DialogueWindow.FRAME_GAP,
            3),
    BOTTOM_RIGHT(GameScreen.GAME_WIDTH / 2,
            GameScreen.GAME_HEIGHT - DialogueWindow.getHeight(3) - DialogueWindow.FRAME_GAP,
            GameScreen.GAME_WIDTH / 2 - DialogueWindow.FRAME_GAP,
            3);

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
