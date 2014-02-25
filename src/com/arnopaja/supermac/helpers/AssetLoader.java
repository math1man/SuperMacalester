package com.arnopaja.supermac.helpers;

import com.arnopaja.supermac.objects.Tile;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * @author Ari Weiland
 */
public class AssetLoader {

    private static final String MAP_NAME_LEADER = "map: ";
    private static final String MAP_WIDTH_LEADER = "width: ";
    private static final String MAP_HEIGHT_LEADER = "height: ";

    public static Preferences prefs;

    public static Texture texture;
    public static TextureRegion groundTile, pathTile, buildingTile;

    public static BitmapFont font, shadow;

    public static FileHandle mapHandle;

    public static void load() {

        texture = new Texture(Gdx.files.internal("data/texture.png"));
        texture.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);

        // TODO: build a basic texture file and set up tiles

        font = new BitmapFont(Gdx.files.internal("data/text.fnt"));
        font.setScale(.25f, -.25f);
        shadow = new BitmapFont(Gdx.files.internal("data/shadow.fnt"));
        shadow.setScale(.25f, -.25f);

        mapHandle = Gdx.files.internal("data/maps.txt");

        prefs = Gdx.app.getPreferences("SuperMacalester");
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
            }
        }
        return null;
    }

    public static void dispose() {
        texture.dispose();
        font.dispose();
        shadow.dispose();
    }
}
