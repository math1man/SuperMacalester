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

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * @author Ari Weiland
 */
public class AssetLoader {

    public static final float FONT_SHADOW_OFFSET = 0.06f;

    private static final String MAP_NAME_LEADER = "map: ";
    private static final String MAP_WIDTH_LEADER = "width: ";
    private static final String MAP_HEIGHT_LEADER = "height: ";
    private static final Random random = new Random();

    public static Preferences prefs;

    private static Texture texture;
    private static final Map<String, TextureRegion> tileMap = new HashMap<String, TextureRegion>();
    private static final Map<String, Integer> randomTileMap = new HashMap<String, Integer>();

    public static TextureRegion grass0, grass1, grass2, cobble;
    public static TextureRegion asphalt, asphaltLineH, asphaltLineV;
    public static TextureRegion asphaltEdgeN, asphaltEdgeE, asphaltEdgeS, asphaltEdgeW;
    public static TextureRegion campusCenter, chapel;
    public static TextureRegion[] steven, stevenSteps;
    public static Animation[] stevenStepping;

    public static FileHandle mapHandle;

    // Used for Battle
    public static TextureRegion battleBackground;

    public static BitmapFont font, shadow;

    public static void load() {

//        texture = new Texture(Gdx.files.internal("data/texture.png"));
//        texture.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);

        // TODO: build a basic texture file and set up tiles
        texture = new Texture(Gdx.files.internal("data/landscapetiles/darkgrasstile1.png"));
        grass0 = new TextureRegion(texture, 0, 0, 32, 32);
        texture = new Texture(Gdx.files.internal("data/landscapetiles/darkgrasstile2.png"));
        grass1 = new TextureRegion(texture, 0, 0, 32, 32);
        texture = new Texture(Gdx.files.internal("data/landscapetiles/darkgrasstile3.png"));
        grass2 = new TextureRegion(texture, 0, 0, 32, 32);
        texture = new Texture(Gdx.files.internal("data/landscapetiles/cobblestone1.png"));
        cobble = new TextureRegion(texture, 0, 0, 32, 32);

        texture = new Texture(Gdx.files.internal("data/landscapetiles/asphalt_tile.png"));
        asphalt = new TextureRegion(texture, 0, 0, 32, 32);
        texture = new Texture(Gdx.files.internal("data/landscapetiles/roadline_tile_horizontal.png"));
        asphaltLineH = new TextureRegion(texture, 0, 0, 32, 32);
        texture = new Texture(Gdx.files.internal("data/landscapetiles/roadline_tile_vertical.png"));
        asphaltLineV = new TextureRegion(texture, 0, 0, 32, 32);
        texture = new Texture(Gdx.files.internal("data/landscapetiles/grasstoroad_north.png"));
        asphaltEdgeN = new TextureRegion(texture, 0, 0, 32, 32);
        asphaltEdgeS = new TextureRegion(asphaltEdgeN);
        asphaltEdgeS.flip(true, false);
        texture = new Texture(Gdx.files.internal("data/landscapetiles/grasstoroad_east.png"));
        asphaltEdgeE = new TextureRegion(texture, 0, 0, 32, 32);
        asphaltEdgeW = new TextureRegion(asphaltEdgeE);
        asphaltEdgeE.flip(false, true);

        // The buildings don't work at the moment because their canvas dimensions are bad
//        texture = new Texture(Gdx.files.internal("data/landscapetiles/campuscenter.png"));
//        campusCenter = new TextureRegion(texture, 0, 0, 480, 128);
//        campusCenter.flip(false, true);
//        texture = new Texture(Gdx.files.internal("data/landscapetiles/chapel.png"));
//        chapel = new TextureRegion(texture, 0, 0, 192, 192);
//        chapel.flip(false, true);

        initTileMap(); // Must be called after all tiles are loaded

        steven = new TextureRegion[4];
        texture = new Texture(Gdx.files.internal("data/steven/steven_back.png"));
        steven[0] = new TextureRegion(texture, 0, 0, 32, 32);
        texture = new Texture(Gdx.files.internal("data/steven/steven_right.png"));
        steven[1] = new TextureRegion(texture, 0, 0, 32, 32);
        texture = new Texture(Gdx.files.internal("data/steven/steven_front.png"));
        steven[2] = new TextureRegion(texture, 0, 0, 32, 32);
        texture = new Texture(Gdx.files.internal("data/steven/steven_left.png"));
        steven[3] = new TextureRegion(texture, 0, 0, 32, 32);

        stevenSteps = new TextureRegion[8];
        texture = new Texture(Gdx.files.internal("data/steven/steven_back_step_right.png"));
        stevenSteps[0] = new TextureRegion(texture, 0, 0, 32, 32);
        texture = new Texture(Gdx.files.internal("data/steven/steven_back_step_left.png"));
        stevenSteps[1] = new TextureRegion(texture, 0, 0, 32, 32);
        texture = new Texture(Gdx.files.internal("data/steven/steven_right_step.png"));
        stevenSteps[2] = new TextureRegion(texture, 0, 0, 32, 32);
        stevenSteps[3] = new TextureRegion(stevenSteps[2]);
        texture = new Texture(Gdx.files.internal("data/steven/steven_front_step_right.png"));
        stevenSteps[4] = new TextureRegion(texture, 0, 0, 32, 32);
        texture = new Texture(Gdx.files.internal("data/steven/steven_front_step_left.png"));
        stevenSteps[5] = new TextureRegion(texture, 0, 0, 32, 32);
        texture = new Texture(Gdx.files.internal("data/steven/steven_left_step.png"));
        stevenSteps[6] = new TextureRegion(texture, 0, 0, 32, 32);
        stevenSteps[7] = new TextureRegion(stevenSteps[6]);

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
                for (int i=0; i<height; i++) {
                    String line = lines[lineIndex + 3 + i];
                    String[] tileCodes = line.split("\t");
                    for (int j=0; j<width; j++) {
                        tileArray[j][i] = Tile.createTile(tileCodes[j].trim());
                    }
                }
                return tileArray;
            }
        }
        return null;
    }

    /* notes:
    n is a special character for the null tile
    b is a prefix for buildings
    numerals should be used only for arbitrary tiles (ie. different grasses)
    asterisks are used to represent an arbitrary numeral in the data file
    */
    private static void initTileMap() {
        tileMap.put("g0", AssetLoader.grass0);
        tileMap.put("g1", AssetLoader.grass1);
        tileMap.put("g2", AssetLoader.grass2);
        tileMap.put("c", AssetLoader.cobble);
        tileMap.put("a", AssetLoader.asphalt);
        tileMap.put("ah", AssetLoader.asphaltLineH);
        tileMap.put("av", AssetLoader.asphaltLineV);
        tileMap.put("an", AssetLoader.asphaltEdgeN);
        tileMap.put("ae", AssetLoader.asphaltEdgeE);
        tileMap.put("as", AssetLoader.asphaltEdgeS);
        tileMap.put("aw", AssetLoader.asphaltEdgeW);
        tileMap.put("bcc", AssetLoader.campusCenter);
        tileMap.put("bch", AssetLoader.chapel);

        randomTileMap.put("g", 3);
    }

    public static TextureRegion getTileSprite(String tileKey) {
        if (tileKey.contains("*")) {
            tileKey = tileKey.replace("*", "");
            int temp = randomTileMap.get(tileKey);
            tileKey += random.nextInt(temp);
        }
        return tileMap.get(tileKey);
    }

    public static void dispose() {
        texture.dispose();
        font.dispose();
        shadow.dispose();
    }
}
