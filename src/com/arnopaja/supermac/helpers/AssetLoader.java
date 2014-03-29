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

    // Tiles
    public static TextureRegion grass0, grass1, grass2;
    public static TextureRegion bushH, bushV, bushFlowersH, bushFlowersV;
    public static TextureRegion treeSmall, treeBig;
    public static TextureRegion cobble, cobbleRed;
    public static TextureRegion asphalt, asphaltLineH, asphaltLineV;
    public static TextureRegion asphaltEdgeN, asphaltEdgeE, asphaltEdgeS, asphaltEdgeW;
    public static TextureRegion asphaltCornerNE, asphaltCornerSE, asphaltCornerSW, asphaltCornerNW;
    public static TextureRegion campusCenter, chapel, dupre, weyerhauser;

    // Characters
    public static EnumMap<Direction, TextureRegion> steven;
    public static EnumMap<Direction, Animation> stevenStepping;

    // Non-character entities
    public static TextureRegion chestOpen, chestClosed;

    public static FileHandle mapHandle;

    // Used for Battle
    public static TextureRegion battleBackground;

    public static BitmapFont font, shadow;

    public static void load() {

//        texture = new Texture(Gdx.files.internal("data/texture.png"));
//        texture.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);

        //--------------------------
        //          Tiles
        //--------------------------

        // TODO: build a basic texture file and set up tiles
        texture = new Texture(Gdx.files.internal("data/landscapetiles/darkgrasstile1.png"));
        grass0 = new TextureRegion(texture, 0, 0, 32, 32);
        texture = new Texture(Gdx.files.internal("data/landscapetiles/darkgrasstile2.png"));
        grass1 = new TextureRegion(texture, 0, 0, 32, 32);
        texture = new Texture(Gdx.files.internal("data/landscapetiles/darkgrasstile3.png"));
        grass2 = new TextureRegion(texture, 0, 0, 32, 32);

        texture = new Texture(Gdx.files.internal("data/landscapetiles/grass_horizontal_bush.png"));
        bushH = new TextureRegion(texture, 0, 0, 32, 32);
        texture = new Texture(Gdx.files.internal("data/landscapetiles/grass_vertical_bush.png"));
        bushV = new TextureRegion(texture, 0, 0, 32, 32);
        texture = new Texture(Gdx.files.internal("data/landscapetiles/grass_horizontal_flowerbush.png"));
        bushFlowersH = new TextureRegion(texture, 0, 0, 32, 32);
        texture = new Texture(Gdx.files.internal("data/landscapetiles/grass_vertical_flowerbush.png"));
        bushFlowersV = new TextureRegion(texture, 0, 0, 32, 32);

        texture = new Texture(Gdx.files.internal("data/landscapetiles/32tree1.png"));
        treeSmall = new TextureRegion(texture, 0, 0, 32, 32);
        treeSmall.flip(false, true);
        texture = new Texture(Gdx.files.internal("data/landscapetiles/64tree1.png"));
        treeBig = new TextureRegion(texture, 0, 0, 64, 64);
        treeBig.flip(false, true);

        texture = new Texture(Gdx.files.internal("data/landscapetiles/cobblestone1.png"));
        cobble = new TextureRegion(texture, 0, 0, 32, 32);
        texture = new Texture(Gdx.files.internal("data/landscapetiles/redcobblestone.png"));
        cobbleRed = new TextureRegion(texture, 0, 0, 32, 32);

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
        texture = new Texture(Gdx.files.internal("data/landscapetiles/grasstoroad_upper_right_corner.png"));
        asphaltCornerNE = new TextureRegion(texture, 0, 0, 32, 32);
        asphaltCornerSE = new TextureRegion(asphaltCornerNE);
        asphaltCornerSE.flip(true, false);
        asphaltCornerSW = new TextureRegion(asphaltCornerSE);
        asphaltCornerSW.flip(false, true);
        asphaltCornerNW = new TextureRegion(asphaltCornerNE);
        asphaltCornerNW.flip(false, true);

        //--------------------------
        //        Buildings
        //--------------------------

        // The CC and chapel don't work at the moment because their canvas dimensions are bad
//        texture = new Texture(Gdx.files.internal("data/landscapetiles/campuscenter.png"));
//        campusCenter = new TextureRegion(texture, 0, 0, 480, 128);
//        campusCenter.flip(false, true);
//        texture = new Texture(Gdx.files.internal("data/landscapetiles/chapel.png"));
//        chapel = new TextureRegion(texture, 0, 0, 192, 192);
//        chapel.flip(false, true);
        texture = new Texture(Gdx.files.internal("data/landscapetiles/dupre.png"));
        dupre = new TextureRegion(texture, 0, 0, 512, 128);
        dupre.flip(false, true);
        texture = new Texture(Gdx.files.internal("data/landscapetiles/weyerhauser.png"));
        weyerhauser = new TextureRegion(texture, 0, 0, 480, 128);
        weyerhauser.flip(false, true);

        MapLoader.initTileMap(); // Must be called after all tiles are loaded

        //--------------------------
        //        Entities
        //--------------------------

        texture = new Texture(Gdx.files.internal("data/landscapetiles/chest_closed.png"));
        chestClosed = new TextureRegion(texture, 0, 0, 512, 128);
        chestClosed.flip(false, true);
        texture = new Texture(Gdx.files.internal("data/landscapetiles/chest_open.png"));
        chestOpen = new TextureRegion(texture, 0, 0, 512, 128);
        chestOpen.flip(false, true);

        SpriteAndAnim saa = loadCharacter("steven/steven_canvas.png");
        steven = saa.sprites;
        stevenStepping = saa.animations;

        //--------------------------
        //          Other
        //--------------------------

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

    public static SpriteAndAnim loadCharacter(String path) {
        // as of 3/28/14, using the updated canvas that Jared was working on in class today
        EnumMap<Direction, TextureRegion> person = new EnumMap<Direction, TextureRegion>(Direction.class);
        EnumMap<Direction, TextureRegion> stepRight = new EnumMap<Direction, TextureRegion>(Direction.class);
        EnumMap<Direction, TextureRegion> stepLeft = new EnumMap<Direction, TextureRegion>(Direction.class);
        EnumMap<Direction, Animation> personAnim = new EnumMap<Direction, Animation>(Direction.class);
        texture = new Texture(Gdx.files.internal("data/" + path));

        TextureRegion[][] regions = TextureRegion.split(texture, 32, 32);
        for (int i=0; i<4; i++) {
            for (int j=0; j<4; j++) {
                regions[i][j].flip(false, true);
            }
            Direction dir = Direction.values()[i];
            person.put(dir, regions[i][0]);
            stepRight.put(dir, regions[i][1]);
            if (i % 2 == 0) {
                stepLeft.put(dir, regions[i][2]);
            } else {
                stepRight.put(dir, regions[i][1]);
            }
            TextureRegion[] array = { person.get(dir), stepRight.get(dir),
                    person.get(dir), stepLeft.get(dir) };
            Animation animation = new Animation(0.1f, array);
            animation.setPlayMode(Animation.LOOP);
            personAnim.put(dir, animation);
        }

        return new SpriteAndAnim(person, personAnim);
    }

    // simple wrapper class used as a return type for loadCharacter()
    private static class SpriteAndAnim {
        protected final EnumMap<Direction, TextureRegion> sprites;
        protected final EnumMap<Direction, Animation> animations;

        private SpriteAndAnim(EnumMap<Direction, TextureRegion> sprites, EnumMap<Direction, Animation> animations) {
            this.sprites = sprites;
            this.animations = animations;
        }
    }

    public static void dispose() {
        texture.dispose();
        font.dispose();
        shadow.dispose();
    }
}
