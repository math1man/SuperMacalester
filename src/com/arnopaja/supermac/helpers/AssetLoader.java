package com.arnopaja.supermac.helpers;

import com.arnopaja.supermac.world.grid.Direction;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.EnumMap;

/**
 * @author Ari Weiland
 */
public class AssetLoader {

    public static final float FONT_SHADOW_OFFSET = 0.06f;

    public static Preferences prefs;

    private static Texture texture;

    public static TextureRegion grass0, grass1, grass2, cobble;
    public static TextureRegion asphalt, asphaltLineH, asphaltLineV;
    public static TextureRegion asphaltEdgeN, asphaltEdgeE, asphaltEdgeS, asphaltEdgeW;
    public static TextureRegion campusCenter, chapel;
    public static EnumMap<Direction, TextureRegion> steven;
    public static EnumMap<Direction, Animation> stevenStepping;

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

        MapLoader.initTileMap(); // Must be called after all tiles are loaded

        loadCharacters(0, 0);

        font = new BitmapFont(Gdx.files.internal("data/text.fnt"));
        shadow = new BitmapFont(Gdx.files.internal("data/shadow.fnt"));

        mapHandle = Gdx.files.internal("data/maps.txt");

        prefs = Gdx.app.getPreferences("com_arnopaja_supermac");
    }

    public static void scaleFont(float scale) {
        font.setScale(scale, -scale);
        shadow.setScale(scale, -scale);
    }

    public static void drawFont(SpriteBatch batch, String string, float x, float y) {
        float shadowOffset = font.getLineHeight() * FONT_SHADOW_OFFSET * -1;
        AssetLoader.shadow.drawMultiLine(batch, string, x + shadowOffset, y + shadowOffset);
        AssetLoader.font.drawMultiLine(batch, string, x, y);
    }

    public static void drawWrappedFont(SpriteBatch batch, String string, float x, float y, float width) {
        float shadowOffset = font.getLineHeight() * FONT_SHADOW_OFFSET * -1;
        AssetLoader.shadow.drawWrapped(batch, string, x + shadowOffset, y + shadowOffset, width);
        AssetLoader.font.drawWrapped(batch, string, x, y, width);
    }

    public static void loadCharacters(int x, int y) {
        steven = new EnumMap<Direction, TextureRegion>(Direction.class);
        texture = new Texture(Gdx.files.internal("data/steven/steven_back.png"));
        TextureRegion temp = new TextureRegion(texture, x, y, 32, 32);
        steven.put(Direction.EAST, temp);

        texture = new Texture(Gdx.files.internal("data/steven/steven_right.png"));
        temp = new TextureRegion(texture, x, y, 32, 32);
        steven.put(Direction.SOUTH, temp);

        // North is just South flipped horizontally
        temp = new TextureRegion(temp);
        temp.flip(true, false);
        steven.put(Direction.NORTH, temp);

        texture = new Texture(Gdx.files.internal("data/steven/steven_front.png"));
        temp = new TextureRegion(texture, x, y, 32, 32);
        steven.put(Direction.WEST, temp);

        EnumMap<Direction, TextureRegion> stevenStepRight = new EnumMap<Direction, TextureRegion>(Direction.class);
        EnumMap<Direction, TextureRegion> stevenStepLeft = new EnumMap<Direction, TextureRegion>(Direction.class);
        texture = new Texture(Gdx.files.internal("data/steven/steven_back_step_right.png"));
        temp = new TextureRegion(texture, x, y, 32, 32);
        stevenStepRight.put(Direction.EAST, temp);
        texture = new Texture(Gdx.files.internal("data/steven/steven_back_step_left.png"));
        temp = new TextureRegion(texture, x, y, 32, 32);
        stevenStepLeft.put(Direction.EAST, temp);

        texture = new Texture(Gdx.files.internal("data/steven/steven_right_step.png"));
        temp = new TextureRegion(texture, x, y, 32, 32);
        stevenStepRight.put(Direction.SOUTH, temp);
        stevenStepLeft.put(Direction.SOUTH, new TextureRegion(temp));

        // North is just South flipped horizontally
        temp = new TextureRegion(temp);
        temp.flip(true, false);
        stevenStepRight.put(Direction.NORTH, temp);
        stevenStepLeft.put(Direction.NORTH, new TextureRegion(temp));

        texture = new Texture(Gdx.files.internal("data/steven/steven_front_step_right.png"));
        temp = new TextureRegion(texture, x, y, 32, 32);
        stevenStepRight.put(Direction.WEST, temp);
        texture = new Texture(Gdx.files.internal("data/steven/steven_front_step_left.png"));
        temp = new TextureRegion(texture, x, y, 32, 32);
        stevenStepLeft.put(Direction.WEST, temp);

        stevenStepping = new EnumMap<Direction, Animation>(Direction.class);
        for (Direction dir : Direction.values()) {
            steven.get(dir).flip(false, true);
            stevenStepRight.get(dir).flip(false, true);
            stevenStepLeft.get(dir).flip(false, true);
            TextureRegion[] array = {steven.get(dir), stevenStepRight.get(dir),
                    steven.get(dir), stevenStepLeft.get(dir)};
            Animation animation = new Animation(0.1f, array);
            animation.setPlayMode(Animation.LOOP);
            stevenStepping.put(dir, animation);
        }
    }

    public static void dispose() {
        texture.dispose();
        font.dispose();
        shadow.dispose();
    }
}
