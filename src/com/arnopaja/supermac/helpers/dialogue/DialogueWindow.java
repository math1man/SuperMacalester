package com.arnopaja.supermac.helpers.dialogue;

import com.arnopaja.supermac.helpers.AssetLoader;
import com.arnopaja.supermac.helpers.Interaction;
import com.arnopaja.supermac.world.grid.Grid;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;

/**
 * @author Ari Weiland
 */
public class DialogueWindow {

    public static final float FRAME_GAP = Grid.GRID_PIXEL_DIMENSION / 2f;

    private final DialogueMember[][] dialogue;

    private final int rows;
    private final int columns;

    private final Rectangle frame;
    private final Rectangle textSpace;
    private final Rectangle[][] sectors;

    public DialogueWindow(Dialogue dialogue, DialogueStyle position) {
        this(toArray(dialogue, position.getRows()), position);
    }

    public DialogueWindow(DialogueMember[][] dialogue, DialogueStyle position) {
        this(dialogue, position.getX(), position.getY(), position.getWidth(), getHeight(position.getRows()));
    }

    public DialogueWindow(DialogueMember[][] dialogue, float x, float y, float width, float height) {
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

    private static DialogueMember[][] toArray(Dialogue dialogue, int rows) {
        if (dialogue instanceof DialogueText) {
            return toArray((DialogueText) dialogue);
        } else {
            return toArray((DialogueOptions) dialogue, rows);
        }
    }

    private static DialogueMember[][] toArray(DialogueText dialogue) {
        DialogueMember[][] array = new DialogueMember[1][1];
        array[0][0] = dialogue.getMember();
        return array;
    }

    private static DialogueMember[][] toArray(DialogueOptions options, int rows) {
        int count = options.getCount();
        int columns = (int) Math.ceil(count / (rows - 1.0f));
        DialogueMember[][] array = new DialogueMember[columns][rows];
        array[0][0] = new DialogueMember(options.getHeader());
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
        for (int i=0; i<columns; i++) {
            for (int j=0; j<rows; j++) {
                AssetLoader.drawWrappedFont(batch, dialogue[i][j].getText(), sectors[i][j].getX(),
                        sectors[i][j].getY(), textSpace.getWidth());
            }
        }
        batch.end();
    }

    public Interaction onClick(float x, float y) {
        if (rows == 1 && columns == 1) {
            return dialogue[0][0].getInteraction();
        }
        for (int i=0; i<columns; i++) {
            for (int j=0; j<rows; j++) {
                if (sectors[i][j].contains(x, y)) {
                    if (dialogue[i][j].hasInteraction()) {
                        return dialogue[i][j].getInteraction();
                    }
                }
            }
        }
        return Interaction.NULL;
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
