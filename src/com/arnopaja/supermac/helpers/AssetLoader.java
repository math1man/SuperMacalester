package com.arnopaja.supermac.helpers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * @author Ari Weiland
 */
public class AssetLoader {

    public static Preferences prefs;

    public static Texture texture;
    public static TextureRegion groundTile, pathTile, buildingTile;

    public static BitmapFont font, shadow;

    public static void load() {

        texture = new Texture(Gdx.files.internal("data/texture.png"));
        texture.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);

        // TODO: build a basic texture file and set up tiles

        font = new BitmapFont(Gdx.files.internal("data/text.fnt"));
        font.setScale(.25f, -.25f);
        shadow = new BitmapFont(Gdx.files.internal("data/shadow.fnt"));
        shadow.setScale(.25f, -.25f);

        prefs = Gdx.app.getPreferences("ZombieBird");
    }

    public static void dispose() {
        texture.dispose();
        font.dispose();
        shadow.dispose();
    }
}
