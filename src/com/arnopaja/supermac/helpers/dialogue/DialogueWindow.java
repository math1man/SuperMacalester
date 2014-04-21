package com.arnopaja.supermac.helpers.dialogue;

import com.arnopaja.supermac.helpers.AssetLoader;
import com.arnopaja.supermac.world.grid.Grid;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;

/**
 * @author Ari Weiland
 */
public class DialogueWindow {
    public static final float FRAME_GAP = 0.5f * Grid.GRID_PIXEL_DIMENSION;

    private final String[][] dialogue;

    private final int rows;
    private final int columns;

    private final Rectangle frame;
    private final Rectangle textSpace;
    private final Rectangle[][] sectors;

    public DialogueWindow(DialogueText text, int rows, float x, float y, float width) {
        this(text.getCurrentDialogue(), rows, x, y, width);
    }

    public DialogueWindow(String dialogue, int rows, float x, float y, float width) {
        this(toArray(dialogue), x, y, width, getHeight(rows));
    }

    public DialogueWindow(DialogueOptions options, int rows, float x, float y, float width) {
        this(toArray(options, rows), x, y, width, getHeight(rows));
    }

    public DialogueWindow(String[][] dialogue, float x, float y, float width, float height) {
        this.dialogue = dialogue;
        this.rows = dialogue[0].length;
        this.columns = dialogue.length;
        frame = new Rectangle(x, y, width, height);
        textSpace = new Rectangle(x + FRAME_GAP, y + FRAME_GAP, width - 2 * FRAME_GAP, height - 2 * FRAME_GAP);
        sectors = new Rectangle[columns][rows];
        float rectWidth = textSpace.getWidth() / columns;
        float rectHeight = textSpace.getHeight() / rows;
        for (int i=0; i<columns; i++) {
            for (int j=0; j<rows; j++) {
                sectors[i][j] = new Rectangle(textSpace.getX() + i * rectWidth,
                        textSpace.getY() + j * rectHeight, rectWidth, rectHeight);
            }
        }
    }

    private static String[][] toArray(String dialogue) {
        String[][] array = new String[1][1];
        array[0][0] = dialogue;
        return array;
    }

    private static String[][] toArray(DialogueOptions options, int rows) {
        int count = options.getCount();
        int columns = (int) Math.ceil(count / (rows - 1.0f));
        String[][] array = new String[columns][rows];
        array[0][0] = options.getHeader();
        for (int i=0; i<count; i++) {
            int x = i / (rows - 1);
            int y = i % (rows - 1) + 1;
            array[x][y] = options.getOption(i);
        }
        return array;
    }

    public void render(ShapeRenderer shapeRenderer, SpriteBatch batch) {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(1, 1, 1, 0.8f);
        shapeRenderer.rect(frame.getX(), frame.getY(), frame.getWidth(), frame.getHeight());
        shapeRenderer.end();

        batch.begin();
        batch.enableBlending();

        float sectorWidth = textSpace.getWidth() / columns;
        float sectorHeight = textSpace.getHeight() / rows;

        for (int i=0; i<columns; i++) {
            for (int j=0; j<rows; j++) {
                AssetLoader.drawWrappedFont(batch, dialogue[i][j], sectors[i][j].getX(),
                        sectors[i][j].getY(), textSpace.getWidth());
            }
        }

        batch.end();
    }

    public float getWidth() {
        return frame.getWidth();
    }

    public float getHeight() {
        return frame.getHeight();
    }

    public Rectangle[][] getSectors() {
        return sectors;
    }

    public static float getHeight(int rows) {
        return 2 * FRAME_GAP - rows * AssetLoader.font.getLineHeight();
    }
}
