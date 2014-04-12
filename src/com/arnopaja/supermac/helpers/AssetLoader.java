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

    private static Texture tilesTexture, entitiesTexture, characterTexture;

    // Tiles
    public static TextureRegion grass0, grass1, grass2;
    public static TextureRegion bush, bushH, bushV, bushFlowersH, bushFlowersV;
    public static TextureRegion treeSmall, treeBig;
    public static TextureRegion cobble, cobbleRed;
    public static TextureRegion asphalt, asphaltLineH, asphaltLineV;
    public static TextureRegion asphaltEdgeN, asphaltEdgeE, asphaltEdgeS, asphaltEdgeW;
    public static TextureRegion asphaltCornerNE, asphaltCornerSE, asphaltCornerSW, asphaltCornerNW;

    // Buildings
    public static TextureRegion art, artCommons, bigelow, campusCenter, carnegie, chapel, doty, dupre;
    public static TextureRegion humanities, kagin, kirk, leonardCenter, library, markim, music, oldMain;
    public static TextureRegion olin, rice, theatre, thirtyMac, turk, wallace, weyerhauser;

    // Non-character entities
    public static TextureRegion chestBrownOpen, chestBrownClosed;
    public static TextureRegion chestRedOpen, chestRedClosed;
    public static TextureRegion chestGreenOpen, chestGreenClosed;

    // Characters
    public static EnumMap<Direction, TextureRegion> mainChar;
    public static EnumMap<Direction, Animation> mainCharAnim;
    public static EnumMap<Direction, TextureRegion> beardGuy;
    public static EnumMap<Direction, Animation> beardGuyAnim;

    // Battle Backgrounds
    public static TextureRegion battleBackground;

    // Data file handles
    public static FileHandle mapHandle, dialogueHandle;

    // Font
    public static BitmapFont font, shadow;

    public static void load() {

        tilesTexture = new Texture(getHandle("landscapetiles/tile_canvas.png"));
        tilesTexture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);

        //--------------------------
        //          Tiles
        //--------------------------

        treeBig = SpriteUtils.makeSprite(tilesTexture, 0, 0, 2, 2);
        treeSmall = SpriteUtils.makeSprite(tilesTexture, 2, 0);
        grass0 = SpriteUtils.makeSprite(tilesTexture, 3, 0);
        grass1 = SpriteUtils.makeSprite(tilesTexture, 4, 0);
        grass2 = SpriteUtils.makeSprite(tilesTexture, 5, 0);
        bush = SpriteUtils.makeSprite(tilesTexture, 6, 0);
        bushH = SpriteUtils.makeSprite(tilesTexture, 7, 0);
        bushFlowersH = SpriteUtils.makeSprite(tilesTexture, 8, 0);
        bushV = SpriteUtils.makeSprite(tilesTexture, 9, 0);
        bushFlowersV = SpriteUtils.makeSprite(tilesTexture, 10, 0);

        cobbleRed = SpriteUtils.makeSprite(tilesTexture, 0, 2);
        cobble = SpriteUtils.makeSprite(tilesTexture, 1, 2);

        asphaltEdgeE = SpriteUtils.makeSprite(tilesTexture, 0, 3);
        asphaltEdgeW = SpriteUtils.makeSprite(tilesTexture, 0, 3, false, true);
        asphaltEdgeN = SpriteUtils.makeSprite(tilesTexture, 0, 4);
        asphaltEdgeS = SpriteUtils.makeSprite(tilesTexture, 0, 4, true, false);
        asphaltCornerNE = SpriteUtils.makeSprite(tilesTexture, 2, 3);
        asphaltCornerSE = SpriteUtils.makeSprite(tilesTexture, 2, 3, true, false);
        asphaltCornerSW = SpriteUtils.makeSprite(tilesTexture, 2, 3, true, true);
        asphaltCornerNW = SpriteUtils.makeSprite(tilesTexture, 2, 3, false, true);
        asphaltLineH = SpriteUtils.makeSprite(tilesTexture, 4, 3);
        asphaltLineV = SpriteUtils.makeSprite(tilesTexture, 4, 4);

        //--------------------------
        //        Buildings
        //--------------------------

        weyerhauser = SpriteUtils.makeSprite(tilesTexture, 0, 5, 24, 12);
        campusCenter = SpriteUtils.makeSprite(tilesTexture, 24, 5, 20, 16);
        chapel = SpriteUtils.makeSprite(tilesTexture, 44, 5, 14, 16);
        library = SpriteUtils.makeSprite(tilesTexture, 0, 17, 16, 12);
        oldMain = SpriteUtils.makeSprite(tilesTexture, 16, 17, 6, 12);


        tilesTexture = new Texture(getHandle("landscapetiles/asphalt_tile.png"));
        asphalt = SpriteUtils.makeSprite(tilesTexture, 0, 0, 1, 1);

        MapLoader.initTileMap(); // Must be called after all tiles and buildings are loaded

        //--------------------------
        //        Entities
        //--------------------------

        entitiesTexture = new Texture(getHandle("entities/entities_canvas.png"));
        chestBrownClosed = SpriteUtils.makeSprite(tilesTexture, 0, 0);
        chestBrownOpen = SpriteUtils.makeSprite(tilesTexture, 0, 1);
        chestRedClosed = SpriteUtils.makeSprite(tilesTexture, 1, 0);
        chestRedOpen = SpriteUtils.makeSprite(tilesTexture, 1, 1);
        chestGreenClosed = SpriteUtils.makeSprite(tilesTexture, 2, 0);
        chestGreenOpen = SpriteUtils.makeSprite(tilesTexture, 2, 1);

        //--------------------------
        //       Characters
        //--------------------------

        SpriteAndAnim saa = loadCharacter("entities/steven/steven_canvas.png");
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

    public static SpriteAndAnim loadCharacter(String path) {
        // as of 3/28/14, using the updated canvas that Jared was working on in class today
        EnumMap<Direction, TextureRegion> person = new EnumMap<Direction, TextureRegion>(Direction.class);
        EnumMap<Direction, TextureRegion> stepRight = new EnumMap<Direction, TextureRegion>(Direction.class);
        EnumMap<Direction, TextureRegion> stepLeft = new EnumMap<Direction, TextureRegion>(Direction.class);
        EnumMap<Direction, Animation> personAnim = new EnumMap<Direction, Animation>(Direction.class);
        characterTexture = new Texture(getHandle(path));

        TextureRegion[][] regions = SpriteUtils.split(characterTexture);
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

    public static void dispose() {
        tilesTexture.dispose();
        entitiesTexture.dispose();
        characterTexture.dispose();
        font.dispose();
        shadow.dispose();
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
}
