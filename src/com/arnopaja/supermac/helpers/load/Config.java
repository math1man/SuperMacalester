package com.arnopaja.supermac.helpers.load;

import com.arnopaja.supermac.helpers.Parsable;
import com.arnopaja.supermac.helpers.SuperParser;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * @author Ari Weiland
 */
public class Config implements Parsable {

    private final String version;
    private final String tilesFile;
    private final String spritesFile;
    private final String pauseImageFile;
    private final String charsDir;
    private final String mapsDir;
    private final String musicFile;
    private final String soundsFile;
    private final String itemsFile;
    private final String spellsFile;
    private final String battleClassesFile;
    private final String dialogueFile;
    private final String cleanDialogueFile;
    private final String plotFile;
    private final String entitiesFile;

    public Config(String version, String tilesFile, String spritesFile, String pauseImageFile, String charsDir,
                  String mapsDir, String musicFile, String soundsFile, String itemsFile, String spellsFile,
                  String battleClassesFile, String dialogueFile, String cleanDialogueFile, String plotFile, String entitiesFile) {
        this.version = version;
        this.tilesFile = tilesFile;
        this.spritesFile = spritesFile;
        this.pauseImageFile = pauseImageFile;
        this.charsDir = charsDir;
        this.mapsDir = mapsDir;
        this.musicFile = musicFile;
        this.soundsFile = soundsFile;
        this.itemsFile = itemsFile;
        this.spellsFile = spellsFile;
        this.battleClassesFile = battleClassesFile;
        this.dialogueFile = dialogueFile;
        this.cleanDialogueFile = cleanDialogueFile;
        this.plotFile = plotFile;
        this.entitiesFile = entitiesFile;
    }

    public String getVersion() {
        return version;
    }

    public FileHandle getTilesFile() {
        return getHandle(getFullPath(tilesFile));
    }

    public FileHandle getSpritesFile() {
        return getHandle(getFullPath(spritesFile));
    }

    public FileHandle getPauseImageFile() {
        return getHandle(getFullPath(pauseImageFile));
    }

    public FileHandle getCharsDir() {
        return getHandle(getFullPath(charsDir));
    }

    public FileHandle getMapsDir() {
        return getHandle(getFullPath(mapsDir));
    }

    public FileHandle getMusicFile() {
        return getHandle(getFullPath(musicFile));
    }

    public FileHandle getSoundsFile() {
        return getHandle(getFullPath(soundsFile));
    }

    public FileHandle getItemsFile() {
        return getHandle(getFullPath(itemsFile));
    }

    public FileHandle getSpellsFile() {
        return getHandle(getFullPath(spellsFile));
    }

    public FileHandle getBattleClassesFile() {
        return getHandle(getFullPath(battleClassesFile));
    }

    public FileHandle getDialogueFile() {
        return getHandle(getFullPath(dialogueFile));
    }

    public FileHandle getCleanDialogueFile() {
        return getHandle(getFullPath(cleanDialogueFile));
    }

    public FileHandle getPlotFile() {
        return getHandle(getFullPath(plotFile));
    }

    public FileHandle getEntitiesFile() {
        return getHandle(getFullPath(entitiesFile));
    }

    public FileHandle getFontFile() {
        return getHandle("font/text.fnt");
    }

    public FileHandle getFontShadowFile() {
        return getHandle("font/shadow.fnt");
    }

    private String getFullPath(String path) {
        return version + "/" + path;
    }

    private static FileHandle getHandle(String path) {
        return Gdx.files.internal("data/" + path);
    }

    public static Config retrieve() {
        return SuperParser.parse(getHandle("config.txt"), Config.class);
    }

    public static class Parser extends SuperParser<Config> {
        @Override
        public Config fromJson(JsonElement element) {
            JsonObject object = element.getAsJsonObject();
            String version = getString(object, "version");
            String tilesFile = getString(object, "tiles file");
            String spritesFile = getString(object, "sprites file");
            String pauseImageFile = getString(object, "pause image");
            String charsDir = getString(object, "characters directory");
            String mapsDir = getString(object, "maps directory");
            String musicDir = getString(object, "music file");
            String soundsDir = getString(object, "sounds file");
            String itemsFile = getString(object, "items file");
            String spellsFile = getString(object, "spells file");
            String battleClassesFile = getString(object, "battle classes file");
            String dialogueFile = getString(object, "dialogue file");
            String cleanDialogueFile = getString(object, "clean dialogue file");
            String plotFile = getString(object, "plot file");
            String entitiesFile = getString(object, "entities file");
            return new Config(version, tilesFile, spritesFile, pauseImageFile, charsDir,
                    mapsDir, musicDir, soundsDir, itemsFile, spellsFile,
                    battleClassesFile, dialogueFile, cleanDialogueFile, plotFile, entitiesFile);
        }

        @Override
        public JsonElement toJson(Config object) {
            JsonObject json = new JsonObject();
            addString(json, "version", object.version);
            addString(json, "tiles file", object.tilesFile);
            addString(json, "sprites file", object.spritesFile);
            addString(json, "pause image", object.pauseImageFile);
            addString(json, "characters directory", object.charsDir);
            addString(json, "maps directory", object.mapsDir);
            addString(json, "music file", object.musicFile);
            addString(json, "sounds file", object.soundsFile);
            addString(json, "items file", object.itemsFile);
            addString(json, "spells file", object.spellsFile);
            addString(json, "battle classes file", object.battleClassesFile);
            addString(json, "dialogue file", object.dialogueFile);
            addString(json, "clean dialogue file", object.cleanDialogueFile);
            addString(json, "plot file", object.plotFile);
            addString(json, "entities file", object.entitiesFile);
            return json;
        }
    }
}
