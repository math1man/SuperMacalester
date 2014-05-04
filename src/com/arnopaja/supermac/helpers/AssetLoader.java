package com.arnopaja.supermac.helpers;

import com.arnopaja.supermac.helpers.dialogue.Dialogue;
import com.arnopaja.supermac.inventory.GenericItem;
import com.arnopaja.supermac.inventory.Spell;
import com.arnopaja.supermac.world.grid.Direction;
import com.arnopaja.supermac.world.grid.Grid;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Ari Weiland
 */
public class AssetLoader {

    public static final float FONT_SHADOW_OFFSET = 0.06f;

    private static Texture tilesTexture, entitiesTexture, characterTexture;

    // Tiles
    public static TextureRegion grass0, grass1, grass2;
    public static TextureRegion bush, bushH, bushV, bushFlowersH, bushFlowersV;
    public static TextureRegion treeSmall, treeBig;
    public static TextureRegion sidewalk, cobble, cobbleRed;
    public static TextureRegion asphalt, asphaltLineH, asphaltLineV;
    public static TextureRegion asphaltGrassN, asphaltGrassE, asphaltGrassS, asphaltGrassW;
    public static TextureRegion asphaltGrassNE, asphaltGrassSE, asphaltGrassSW, asphaltGrassNW;
    public static TextureRegion asphaltCobbleN, asphaltCobbleE, asphaltCobbleS, asphaltCobbleW;
    public static TextureRegion asphaltCobbleNE, asphaltCobbleSE, asphaltCobbleSW, asphaltCobbleNW;
    public static Animation asteroid;

    // Buildings
    public static TextureRegion art, artCommons, bigelow, campusCenter, carnegie, chapel, doty, dupre;
    public static TextureRegion humanities, kagin, kirk, leonardCenter, library, markim, music, oldMain;
    public static TextureRegion olin, olinRiceStairs, rice, theatre, thirtyMac, turk, wallace, weyerhauser;

    // Non-character entities
    public static TextureRegion chestBrownOpen, chestBrownClosed;
    public static TextureRegion chestRedOpen, chestRedClosed;
    public static TextureRegion chestGreenOpen, chestGreenClosed;
    public static TextureRegion garbageCan;

    // Characters
    public static Map<String, CharacterAsset> characterAssetMap = new HashMap<String, CharacterAsset>();

    // Battle Backgrounds
    public static Map<String, TextureRegion> battleBackgrounds = new HashMap<String, TextureRegion>();

    // Data file handles
    public static FileHandle itemHandle;
    public static FileHandle spellHandle;
    public static FileHandle dialogueHandle;
    public static FileHandle plotHandle;
    public static FileHandle mapHandle;
    public static FileHandle entitiesHandle;

    // Caches
    public static Map<String, Dialogue> dialogues = new HashMap<String, Dialogue>();
    public static Map<String, Grid> grids = new HashMap<String, Grid>();

    // Sounds
    public static Sound sound;

    // Font
    public static final float FONT_HEIGHT = Grid.GRID_PIXEL_DIMENSION * 3f / 4f; // A line is 3/4 of a grid space
    private static BitmapFont font, shadow;

    // Preferences
    public static Preferences prefs;

    public static void load() {

        tilesTexture = new Texture(getHandle("landscapetiles/tile_canvas.png"));
        tilesTexture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);

        //--------------------------
        //          Tiles
        //--------------------------

        sound = Gdx.audio.newSound(getHandle("sounds/compscimagic.mp3"));
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

        sidewalk = SpriteUtils.makeSprite(tilesTexture, 11, 0);
        cobbleRed = SpriteUtils.makeSprite(tilesTexture, 0, 2);
        cobble = SpriteUtils.makeSprite(tilesTexture, 1, 2);

        asphalt = SpriteUtils.makeSprite(tilesTexture, 5, 4);
        asphaltGrassE = SpriteUtils.makeSprite(tilesTexture, 0, 3);
        asphaltGrassW = SpriteUtils.makeSprite(tilesTexture, 0, 3, false, true);
        asphaltGrassN = SpriteUtils.makeSprite(tilesTexture, 0, 4);
        asphaltGrassS = SpriteUtils.makeSprite(tilesTexture, 0, 4, true, false);
        asphaltGrassNE = SpriteUtils.makeSprite(tilesTexture, 2, 3);
        asphaltGrassSE = SpriteUtils.makeSprite(tilesTexture, 2, 3, true, false);
        asphaltGrassSW = SpriteUtils.makeSprite(tilesTexture, 2, 3, true, true);
        asphaltGrassNW = SpriteUtils.makeSprite(tilesTexture, 2, 3, false, true);
        asphaltLineH = SpriteUtils.makeSprite(tilesTexture, 4, 3);
        asphaltLineV = SpriteUtils.makeSprite(tilesTexture, 4, 4);
        asphaltCobbleE = SpriteUtils.makeSprite(tilesTexture, 6, 3, false, true);
        asphaltCobbleW = SpriteUtils.makeSprite(tilesTexture, 6, 3);
        asphaltCobbleN = SpriteUtils.makeSprite(tilesTexture, 5, 3);
        asphaltCobbleS = SpriteUtils.makeSprite(tilesTexture, 5, 3, true, false);
        asphaltCobbleNE = SpriteUtils.makeSprite(tilesTexture, 6, 4, false, true);
        asphaltCobbleSE = SpriteUtils.makeSprite(tilesTexture, 6, 4, true, true);
        asphaltCobbleSW = SpriteUtils.makeSprite(tilesTexture, 6, 4, true, false);
        asphaltCobbleNW = SpriteUtils.makeSprite(tilesTexture, 6, 4);

        //--------------------------
        //        Buildings
        //--------------------------

        weyerhauser = SpriteUtils.makeSprite(tilesTexture, 0, 5, 24, 12);
        campusCenter = SpriteUtils.makeSprite(tilesTexture, 24, 5, 20, 16);
        chapel = SpriteUtils.makeSprite(tilesTexture, 44, 5, 14, 16);
        kirk = SpriteUtils.makeSprite(tilesTexture, 58, 29, 32, 16);
        leonardCenter = SpriteUtils.makeSprite(tilesTexture, 58, 5, 52, 24);
        library = SpriteUtils.makeSprite(tilesTexture, 0, 17, 16, 12);
        oldMain = SpriteUtils.makeSprite(tilesTexture, 16, 17, 6, 12);
        olin = SpriteUtils.makeSprite(tilesTexture, 42, 21, 16, 32);
        olinRiceStairs = SpriteUtils.makeSprite(tilesTexture, 38, 43, 3, 5);
        rice = SpriteUtils.makeSprite(tilesTexture, 34, 53, 24, 16);

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

        loadCharacter("Tom", "entities/steven.png");
        loadCharacter("Betsy", "entities/betsy.png");
        loadCharacter("Jeff", "entities/beardguy.png");

        //--------------------------
        //          Other
        //--------------------------

        // TODO: battle backgrounds (for now, we just use RenderGrids)

        itemHandle = getHandle("items.txt");
        spellHandle = getHandle("spells.txt");
        dialogueHandle = getHandle("macalester/dialogues.txt");
        mapHandle = getHandle("macalester/maps");
        plotHandle = getHandle("macalester/plot.txt");
        entitiesHandle = getHandle("macalester/entities.txt");

        SuperParser.parseAll(AssetLoader.itemHandle, GenericItem.class);
        SuperParser.parseAll(AssetLoader.spellHandle, Spell.class);
        dialogues = SuperParser.parseAll(AssetLoader.dialogueHandle, Dialogue.class);
        grids = MapLoader.generateGrids(AssetLoader.mapHandle);

        font = new BitmapFont(Gdx.files.internal("data/text.fnt"));
        shadow = new BitmapFont(Gdx.files.internal("data/shadow.fnt"));
        scaleFont(FONT_HEIGHT / AssetLoader.font.getLineHeight());

        prefs = Gdx.app.getPreferences("com_arnopaja_supermac");
    }

    public static void loadCharacter(String name, String path) {
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
        characterAssetMap.put(name, new CharacterAsset(person, personAnim));
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

    public static String getMap(String name) {
        return mapHandle.child(name + ".txt").readString();
    }

    public static TextureRegion getBackground(String name) {
        return battleBackgrounds.get(name);
    }

    public static CharacterAsset getCharacter(String name) {
        return characterAssetMap.get(name);
    }

    private static FileHandle getHandle(String path) {
        return Gdx.files.internal("data/" + path);
    }

    public static void dispose() {
        tilesTexture.dispose();
        entitiesTexture.dispose();
        characterTexture.dispose();
        font.dispose();
        shadow.dispose();
    }
}
