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
    public static TextureRegion bush, bushH, bushV, bushFlowersH, bushFlowersV;
    public static TextureRegion treeSmall, treeBig;
    public static TextureRegion cobble, cobbleRed;
    public static TextureRegion asphalt, asphaltLineH, asphaltLineV;
    public static TextureRegion asphaltEdgeN, asphaltEdgeE, asphaltEdgeS, asphaltEdgeW;
    public static TextureRegion asphaltCornerNE, asphaltCornerSE, asphaltCornerSW, asphaltCornerNW;
    public static TextureRegion campusCenter, chapel, dupre, weyerhauser;

    // Characters
    public static EnumMap<Direction, TextureRegion> mainChar;
    public static EnumMap<Direction, Animation> mainCharAnim;
    public static EnumMap<Direction, TextureRegion> beardGuy;
    public static EnumMap<Direction, Animation> beardGuyAnim;

    // Non-character entities
    public static TextureRegion chestOpen, chestClosed;

    public static FileHandle mapHandle, dialogueHandle;

    // Used for Battle
    public static TextureRegion battleBackground;

    public static BitmapFont font, shadow;

    public static void load() {

        texture = new Texture(getHandle("landscapetiles/big_tile_canvas.png"));
        texture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);

        //--------------------------
        //          Tiles
        //--------------------------

        treeBig = SpriteUtils.makeSprite(texture, 0, 0, 2, 2);
        treeSmall = SpriteUtils.makeSprite(texture, 2, 0);
        grass0 = SpriteUtils.makeSprite(texture, 3, 0);
        grass1 = SpriteUtils.makeSprite(texture, 4, 0);
        grass2 = SpriteUtils.makeSprite(texture, 5, 0);
        bush = SpriteUtils.makeSprite(texture, 6, 0);
        bushH = SpriteUtils.makeSprite(texture, 7, 0);
        bushFlowersH = SpriteUtils.makeSprite(texture, 8, 0);
        bushV = SpriteUtils.makeSprite(texture, 9, 0);
        bushFlowersV = SpriteUtils.makeSprite(texture, 10, 0);

        cobbleRed = SpriteUtils.makeSprite(texture, 0, 2);
        cobble = SpriteUtils.makeSprite(texture, 1, 2);

        asphaltEdgeE = SpriteUtils.makeSprite(texture, 0, 3);
        asphaltEdgeW = SpriteUtils.makeSprite(texture, 0, 3, false, true);
        asphaltEdgeN = SpriteUtils.makeSprite(texture, 0, 4);
        asphaltEdgeS = SpriteUtils.makeSprite(texture, 0, 4, true, false);
        asphaltCornerNE = SpriteUtils.makeSprite(texture, 2, 3);
        asphaltCornerSE = SpriteUtils.makeSprite(texture, 2, 3, true, false);
        asphaltCornerSW = SpriteUtils.makeSprite(texture, 2, 3, true, true);
        asphaltCornerNW = SpriteUtils.makeSprite(texture, 2, 3, false, true);
        asphaltLineH = SpriteUtils.makeSprite(texture, 4, 3);
        asphaltLineV = SpriteUtils.makeSprite(texture, 4, 4);


        texture = new Texture(getHandle("landscapetiles/asphalt_tile.png"));
        asphalt = SpriteUtils.makeSprite(texture, 0, 0, 1, 1);

        //--------------------------
        //        Buildings
        //--------------------------

        // The CC and chapel don't work at the moment because their canvas dimensions are bad
//        texture = new Texture(getHandle("landscapetiles/campuscenter.png"));
//        campusCenter = SpriteUtils.makeSprite(texture, 0, 0, 480, 128);
//        texture = new Texture(getHandle("landscapetiles/chapel.png"));
//        chapel = SpriteUtils.makeSprite(texture, 0, 0, 192, 192);
//        texture = new Texture(getHandle("landscapetiles/dupre.png"));
//        dupre = SpriteUtils.makeSprite(texture, 0, 0, 512, 128);
//        texture = new Texture(getHandle("landscapetiles/weyerhauser.png"));
//        weyerhauser = SpriteUtils.makeSprite(texture, 0, 0, 448, 128);

        MapLoader.initTileMap(); // Must be called after all tiles are loaded

        //--------------------------
        //        Entities
        //--------------------------

        font = new BitmapFont(getHandle("text.fnt"));
        shadow = new BitmapFont(getHandle("shadow.fnt"));
        texture = new Texture(Gdx.files.internal("data/landscapetiles/chest_closed.png"));
        chestClosed = new TextureRegion(texture, 0, 0, 512, 128);
        chestClosed.flip(false, true);
        texture = new Texture(Gdx.files.internal("data/landscapetiles/chest_open.png"));
        chestOpen = new TextureRegion(texture, 0, 0, 512, 128);
        chestOpen.flip(false, true);

        SpriteAndAnim saa = loadCharacter("steven/steven_canvas.png");
        mainChar = saa.sprites;
        mainCharAnim = saa.animations;

        //--------------------------
        //          Other
        //--------------------------

        font = new BitmapFont(Gdx.files.internal("data/text.fnt"));
        shadow = new BitmapFont(Gdx.files.internal("data/shadow.fnt"));

        mapHandle = getHandle("maps.txt");
        dialogueHandle = getHandle("dialogues.txt");

        prefs = Gdx.app.getPreferences("com_arnopaja_supermac");
    }

    private static FileHandle getHandle(String path) {
        return Gdx.files.internal("data/" + path);
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
        texture = new Texture(getHandle(path));

        TextureRegion[][] regions = SpriteUtils.split(texture);
        for (int i=0; i<4; i++) {
            Direction dir = Direction.values()[i];
            person.put(dir, regions[0][i]);
            stepRight.put(dir, regions[1][i]);
            if (i % 2 == 0) {
                stepLeft.put(dir, regions[2][i]);
            } else {
                stepLeft.put(dir, regions[1][i]);
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
