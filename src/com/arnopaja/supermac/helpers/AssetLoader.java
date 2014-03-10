package com.arnopaja.supermac.helpers;

import com.arnopaja.supermac.objects.Tile;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * @author Ari Weiland
 */
public class AssetLoader {

    public static final float FONT_SHADOW_OFFSET = 0.06f;

    private static final String MAP_NAME_LEADER = "map: ";
    private static final String MAP_WIDTH_LEADER = "width: ";
    private static final String MAP_HEIGHT_LEADER = "height: ";

    public static Preferences prefs;

    public static Texture texture;
    public static TextureRegion darkgrass1, cobblestone1, buildingTile;
    public static TextureRegion[] steven, stevenSteps;
    public static Animation[] stevenStepping;

    public static BitmapFont font, shadow;

    public static FileHandle mapHandle;

    public static void load() {

//        texture = new Texture(Gdx.files.internal("data/texture.png"));
//        texture.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);

        // TODO: build a basic texture file and set up tiles
        Texture temp = new Texture(Gdx.files.internal("data/landscapetiles/darkgrasstile1.png"));
        darkgrass1 = new TextureRegion(temp, 0, 0, 32, 32);
        darkgrass1.flip(false, true);
        temp = new Texture(Gdx.files.internal("data/landscapetiles/cobblestone1.png"));
        cobblestone1 = new TextureRegion(temp, 0, 0, 32, 32);
        cobblestone1.flip(false, true);
        temp = new Texture(Gdx.files.internal("data/landscapetiles/darkgrasstile3.png"));
        buildingTile = new TextureRegion(temp, 0, 0, 32, 32);
        buildingTile.flip(false, true);

        Tile.initSpriteMap(); // Must be called after all tiles are loaded

        steven = new TextureRegion[4];
        temp = new Texture(Gdx.files.internal("data/steven/steven_back.png"));
        steven[0] = new TextureRegion(temp, 0, 0, 32, 32);
        temp = new Texture(Gdx.files.internal("data/steven/steven_right.png"));
        steven[1] = new TextureRegion(temp, 0, 0, 32, 32);
        temp = new Texture(Gdx.files.internal("data/steven/steven_front.png"));
        steven[2] = new TextureRegion(temp, 0, 0, 32, 32);
        temp = new Texture(Gdx.files.internal("data/steven/steven_left.png"));
        steven[3] = new TextureRegion(temp, 0, 0, 32, 32);

        stevenSteps = new TextureRegion[8];
        temp = new Texture(Gdx.files.internal("data/steven/steven_back_step_right.png"));
        stevenSteps[0] = new TextureRegion(temp, 0, 0, 32, 32);
        temp = new Texture(Gdx.files.internal("data/steven/steven_back_step_left.png"));
        stevenSteps[1] = new TextureRegion(temp, 0, 0, 32, 32);
        temp = new Texture(Gdx.files.internal("data/steven/steven_right_step.png"));
        stevenSteps[2] = new TextureRegion(temp, 0, 0, 32, 32);
        stevenSteps[3] = new TextureRegion(temp, 0, 0, 32, 32);
        temp = new Texture(Gdx.files.internal("data/steven/steven_front_step_right.png"));
        stevenSteps[4] = new TextureRegion(temp, 0, 0, 32, 32);
        temp = new Texture(Gdx.files.internal("data/steven/steven_front_step_left.png"));
        stevenSteps[5] = new TextureRegion(temp, 0, 0, 32, 32);
        temp = new Texture(Gdx.files.internal("data/steven/steven_left_step.png"));
        stevenSteps[6] = new TextureRegion(temp, 0, 0, 32, 32);
        stevenSteps[7] = new TextureRegion(temp, 0, 0, 32, 32);

        stevenStepping = new Animation[4];
        for (int i=0; i<4; i++) {
            steven[i].flip(false, true);
            stevenSteps[2*i].flip(false, true);
            stevenSteps[2*i + 1].flip(false, true);
            TextureRegion[] tempSteps = {steven[i], stevenSteps[2*i], steven[i], stevenSteps[2*i+1]};
            stevenStepping[i] = new Animation(0.1f, tempSteps);
            stevenStepping[i].setPlayMode(Animation.LOOP);
        }

        font = new BitmapFont(Gdx.files.internal("data/text.fnt"));
        shadow = new BitmapFont(Gdx.files.internal("data/shadow.fnt"));

        mapHandle = Gdx.files.internal("data/maps.txt");

        prefs = Gdx.app.getPreferences("SuperMacalester");
    }

    public static void scaleFont(float scale) {
        font.setScale(scale, -scale);
        shadow.setScale(scale, -scale);
    }

    public static void drawFont(SpriteBatch batcher, String string, float x, float y) {
        float shadowOffset = font.getLineHeight() * FONT_SHADOW_OFFSET * -1;
        AssetLoader.shadow.drawMultiLine(batcher, string, x + shadowOffset, y + shadowOffset);
        AssetLoader.font.drawMultiLine(batcher, string, x, y);
    }

    public static void drawWrappedFont(SpriteBatch batcher, String string, float x, float y, float width) {
        float shadowOffset = font.getLineHeight() * FONT_SHADOW_OFFSET * -1;
        AssetLoader.shadow.drawWrapped(batcher, string, x + shadowOffset, y + shadowOffset, width);
        AssetLoader.font.drawWrapped(batcher, string, x, y, width);
    }

    public static Tile[][] parseTileArray(String name) {
        String raw = mapHandle.readString();
        String[] lines = raw.split("\n");
        int lineIndex = 0;
        while (lineIndex < lines.length && !lines[lineIndex].contains(MAP_NAME_LEADER + name)) {
            lineIndex++;
        }
        String w = lines[lineIndex + 1];
        String h = lines[lineIndex + 2];
        if (lineIndex < lines.length && w.contains(MAP_WIDTH_LEADER) && h.contains(MAP_HEIGHT_LEADER)) {
            int width = Integer.parseInt(w.substring(MAP_WIDTH_LEADER.length()).trim());
            int height = Integer.parseInt(h.substring(MAP_HEIGHT_LEADER.length()).trim());
            if (width > 0 && height > 0) {
                Tile[][] tileArray = new Tile[width][height];
                for (int i=0; i<width; i++) {
                    String line = lines[lineIndex + 3 + i];
                    String[] tileCodes = line.split("\t");
                    for (int j=0; j<height; j++) {
                        tileArray[i][j] = Tile.createTile(tileCodes[j].trim());
                    }
                }
                return tileArray;
            }
        }
        return null;
    }

    public static void dispose() {
//        texture.dispose();
        font.dispose();
        shadow.dispose();
    }
}
