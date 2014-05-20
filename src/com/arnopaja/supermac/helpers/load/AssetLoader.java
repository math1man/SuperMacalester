package com.arnopaja.supermac.helpers.load;

import com.arnopaja.supermac.battle.characters.BattleClass;
import com.arnopaja.supermac.helpers.SuperParser;
import com.arnopaja.supermac.helpers.dialogue.Dialogue;
import com.arnopaja.supermac.inventory.GenericItem;
import com.arnopaja.supermac.inventory.Spell;
import com.arnopaja.supermac.plot.Settings;
import com.arnopaja.supermac.world.grid.Direction;
import com.arnopaja.supermac.world.grid.Grid;
import com.arnopaja.supermac.world.objects.Tile;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.*;

/**
 * @author Ari Weiland
 */
public class AssetLoader {

    public static final float FONT_HEIGHT = Grid.GRID_PIXEL_DIMENSION * 3f / 4f; // A line is 3/4 of a grid space
    public static final float FONT_SHADOW_OFFSET = 0.06f;

    private static List<Texture> usedTextures = new ArrayList<Texture>();

    private static Config config;
    private static TileMap tiles = new TileMap();
    private static Map<String, TextureRegion> sprites = new HashMap<String, TextureRegion>();
    private static TextureRegion pauseButton;
    private static Map<String, CharacterAsset> characters = new HashMap<String, CharacterAsset>();
    private static Map<String, Grid> grids = new HashMap<String, Grid>();
    private static Map<String, Music> music = new HashMap<String, Music>();
    private static Map<String, Sound> sounds = new HashMap<String, Sound>();
    private static Map<String, Dialogue> dialogues = new HashMap<String, Dialogue>();
    private static Map<String, Dialogue> cleanDialogues = new HashMap<String, Dialogue>();
    private static BitmapFont font, shadow;
    private static Preferences prefs;

    //--------------------
    //    Load Methods
    //--------------------

    public static void load(Config c) {

        config = c;

        loadTiles(config.getTilesFile());
        loadSprites(config.getSpritesFile());
        loadPauseButton(config.getPauseImageFile());
        loadCharacters(config.getCharsDir());
        loadGrids(config.getMapsDir());
        // TODO: Give credit to Rolemusic for the music under the Creative Commons Attribution License
        // Artist: Rolemusic
        // Album: gigs n' contest
        loadMusic(config.getMusicFile());
        loadSounds(config.getSoundsFile());
        loadItems(config.getItemsFile());
        loadSpells(config.getSpellsFile());
        loadDialogues(config.getDialogueFile());
        loadCleanDialogues(config.getCleanDialogueFile());

        font = new BitmapFont(config.getFontFile());
        shadow = new BitmapFont(config.getFontShadowFile());
        scaleFont(FONT_HEIGHT / font.getLineHeight());

        prefs = Gdx.app.getPreferences("com_arnopaja_supermac_" + config.getVersion());
    }

    private static void loadTiles(FileHandle file) {
        String config = file.readString();
        JsonObject object = SuperParser.getJsonHead(config).getAsJsonObject();
        Texture texture = getTexture(file.parent().child(SuperParser.getString(object, "file")));
        JsonArray array = object.getAsJsonArray("tiles");
        for (JsonElement e : array) {
            JsonObject o = e.getAsJsonObject();
            String tileKey = SuperParser.getString(o, "key");
            int count = SuperParser.getInt(o, "frames", 1);
            float duration = SuperParser.getFloat(o, "duration", 0);
            int width = SuperParser.getInt(o, "width", 1);
            int height = SuperParser.getInt(o, "height", 1);
            boolean flipX = SuperParser.getBoolean(o, "flipX", false);
            boolean flipY = SuperParser.getBoolean(o, "flipY", false);
            int x = SuperParser.getInt(o, "x");
            int y = SuperParser.getInt(o, "y");
            TextureRegion[] frames = new TextureRegion[count];
            frames[0] = SpriteUtils.makeSprite(texture, x, y, width, height, flipX, flipY);
            if (count > 1) {
                for (int i=1; i<count; i++) {
                    x = SuperParser.getInt(o, "x" + i);
                    y = SuperParser.getInt(o, "y" + i);
                    frames[i] = SpriteUtils.makeSprite(texture, x, y, width, height, flipX, flipY);
                }
            }
            tiles.put(tileKey, new Animation(duration, frames));
        }
    }

    private static void loadSprites(FileHandle file) {
        String config = file.readString();
        JsonObject object = SuperParser.getJsonHead(config).getAsJsonObject();
        Texture texture = getTexture(file.parent().child(SuperParser.getString(object, "file")));
        JsonArray array = object.getAsJsonArray("sprites");
        for (JsonElement e : array) {
            JsonObject o = e.getAsJsonObject();
            String name = SuperParser.getString(o, "name");
            int x = SuperParser.getInt(o, "x");
            int y = SuperParser.getInt(o, "y");
            TextureRegion sprite = SpriteUtils.makeSprite(texture, x, y);
            sprites.put(name, sprite);
        }
    }

    private static void loadPauseButton(FileHandle file) {
        pauseButton = SpriteUtils.makeSprite(getTexture(file), 0, 0, 2, 2);
    }

    private static void loadCharacters(FileHandle folder) {
        for (FileHandle file : folder.list()) {
            EnumMap<Direction, TextureRegion> person = new EnumMap<Direction, TextureRegion>(Direction.class);
            EnumMap<Direction, TextureRegion> stepRight = new EnumMap<Direction, TextureRegion>(Direction.class);
            EnumMap<Direction, TextureRegion> stepLeft = new EnumMap<Direction, TextureRegion>(Direction.class);
            EnumMap<Direction, Animation> personAnim = new EnumMap<Direction, Animation>(Direction.class);
            String name = file.nameWithoutExtension();
            Texture characterTexture = getTexture(file);

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
            characters.put(name, new CharacterAsset(person, personAnim));
        }
    }

    private static void loadGrids(FileHandle folder) {
        FileHandle[] handles = folder.list("txt");
        for (FileHandle handle : handles) {
            String name = handle.nameWithoutExtension().toLowerCase().replaceAll("[\\W_]", "");
            String raw = handle.readString();
            String[] lines = raw.split("\n");
            int height = lines.length;
            int width = lines[0].split("\t").length;
            Tile[][] tileArray = new Tile[width][height];
            for (int j=0; j<height; j++) {
                String[] tileCodes = lines[j].split("\t");
                for (int i=0; i<width; i++) {
                    tileArray[i][j] = Tile.createTile(tileCodes[i].trim());
                }
            }
            SpriteUtils.split(tileArray);
            grids.put(name, new Grid(name, tileArray));
        }
    }

    private static void loadMusic(FileHandle file) {
        String config = file.readString();
        JsonObject object = SuperParser.getJsonHead(config).getAsJsonObject();
        JsonArray array = object.getAsJsonArray("music");
        for (JsonElement e : array) {
            JsonObject o = e.getAsJsonObject();
            String track = SuperParser.getString(o, "file");
            String name = SuperParser.getString(o, "name");
            Music m = Gdx.audio.newMusic(file.parent().child(track));
            music.put(name, m);
        }
    }

    private static void loadSounds(FileHandle file) {
        String config = file.readString();
        JsonObject object = SuperParser.getJsonHead(config).getAsJsonObject();
        JsonArray array = object.getAsJsonArray("sounds");
        for (JsonElement e : array) {
            JsonObject o = e.getAsJsonObject();
            String sound = SuperParser.getString(o, "file");
            String name = SuperParser.getString(o, "name");
            Sound s = Gdx.audio.newSound(file.parent().child(sound));
            sounds.put(name, s);
        }
        BattleClass.init(); // needed to init the magic sounds
    }

    private static void loadItems(FileHandle file) {
        SuperParser.parseAll(file, GenericItem.class);
    }

    private static void loadSpells(FileHandle file) {
        SuperParser.parseAll(file, Spell.class);
    }

    private static void loadDialogues(FileHandle file) {
        dialogues = SuperParser.parseAll(file, Dialogue.class);
    }

    private static void loadCleanDialogues(FileHandle file) {
        cleanDialogues = SuperParser.parseAll(file, Dialogue.class);
    }

    //--------------------
    //    Get Methods
    //--------------------

    public static Config getConfig() {
        return config;
    }

    public static Animation getTile(String tileKey) {
        return tiles.get(tileKey);
    }

    public static TextureRegion getSprite(String name) {
        return sprites.get(name);
    }

    public static TextureRegion getPauseButton() {
        return pauseButton;
    }

    public static CharacterAsset getCharacter(String name) {
        return characters.get(name.toLowerCase());
    }

    public static Grid getGrid(String gridName) {
        return grids.get(gridName);
    }

    public static Map<String,Grid> getGrids() {
        return grids;
    }

    public static Music getMusic(String name) {
        return music.get(name);
    }

    public static Map<String, Music> getMusic() {
        return music;
    }

    public static Sound getSound(String name) {
        return sounds.get(name);
    }

    public static Dialogue getDialogue(String name) {
        if (Settings.isClean()) {
            return cleanDialogues.get(name);
        } else {
            return dialogues.get(name);
        }
    }

    public static FileHandle getPlotFile() {
        return config.getPlotFile();
    }

    public static FileHandle getEntitiesFile() {
        return config.getEntitiesFile();
    }

    public static Preferences getPrefs() {
        return prefs;
    }

    //--------------------
    //    Font Methods
    //--------------------

    public static void scaleFont(float scale) {
        font.setScale(scale, -scale);
        shadow.setScale(scale, -scale);
    }

    public static void drawWrappedFont(SpriteBatch batch, String string, float x, float y, float width) {
        float shadowOffset = font.getLineHeight() * FONT_SHADOW_OFFSET * -1;
        AssetLoader.shadow.drawWrapped(batch, string, x + shadowOffset, y + shadowOffset, width);
        AssetLoader.font.drawWrapped(batch, string, x, y, width);
    }

    //--------------------
    //       Other
    //--------------------

    public static void dispose() {
        for (Texture texture : usedTextures) {
            texture.dispose();
        }
        for (Music m : music.values()) {
            m.dispose();
        }
        for (Sound s : sounds.values()) {
            s.dispose();
        }
        font.dispose();
        shadow.dispose();
    }

    private static Texture getTexture(FileHandle handle) {
        Texture texture = new Texture(handle);
        texture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        usedTextures.add(texture);
        return texture;
    }

    private static class TileMap {
        private static final Random random = new Random();

        private final Map<String, Animation> tileMap = new HashMap<String, Animation>();
        private final Map<String, Integer> randomTileMap = new HashMap<String, Integer>();

        public void put(String key, TextureRegion... sprites) {
            int length = sprites.length;
            Animation[] animations = new Animation[length];
            for (int i=0; i<length; i++) {
                animations[i] = new Animation(1, sprites[i]);
            }
            put(key, animations);
        }

        public void put(String key, Animation... animations) {
            int shift = 0;
            int length = animations.length;
            if (contains(key)) {
                if (tileMap.containsKey(key)) { // add the old animation to the array
                    animations = Arrays.copyOf(animations, length + 1);
                    animations[length] = tileMap.get(key);
                    length++;
                    tileMap.remove(key);
                } else { // just add them as a continuation of the randomness
                    shift = randomTileMap.get(key);
                }
            }
            if (length + shift > 1) {
                randomTileMap.put(key, length + shift);
                for (int i=0; i<length; i++) {
                    tileMap.put(key + (i + shift), animations[i]);
                }
            } else {
                tileMap.put(key, animations[0]);
            }
        }

        public Animation get(String key) {
            key = key.trim();
            if (randomTileMap.containsKey(key)) {
                key += random.nextInt(randomTileMap.get(key));
            }
            return tileMap.get(key);
        }

        public boolean contains(String key) {
            return tileMap.containsKey(key) || randomTileMap.containsKey(key);
        }
    }
}
