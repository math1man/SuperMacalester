package com.arnopaja.supermac.helpers;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * @author Ari Weiland
 */
public class TileMap {
    private static final Random random = new Random();

    private final Map<String, TextureRegion> tileMap = new HashMap<String, TextureRegion>();
    private final Map<String, Integer> randomTileMap = new HashMap<String, Integer>();

    public TileMap() {
        put("g",   AssetLoader.grass0, AssetLoader.grass1, AssetLoader.grass2);
        put("bh",  AssetLoader.bushH);
        put("bv",  AssetLoader.bushV);
        put("bfh", AssetLoader.bushFlowersH);
        put("bfv", AssetLoader.bushFlowersV);
        put("ts",  AssetLoader.treeSmall);
        put("_tb", AssetLoader.treeBig);
        put("c",   AssetLoader.cobble);
        put("cr",  AssetLoader.cobbleRed);
        put("a",   AssetLoader.asphalt);
        put("ah",  AssetLoader.asphaltLineH);
        put("av",  AssetLoader.asphaltLineV);
        put("an",  AssetLoader.asphaltEdgeN);
        put("ae",  AssetLoader.asphaltEdgeE);
        put("as",  AssetLoader.asphaltEdgeS);
        put("aw",  AssetLoader.asphaltEdgeW);
        put("ane", AssetLoader.asphaltCornerNE);
        put("ase", AssetLoader.asphaltCornerSE);
        put("asw", AssetLoader.asphaltCornerSW);
        put("anw", AssetLoader.asphaltCornerNW);
//        put("_cc", AssetLoader.campusCenter);
//        put("_ch", AssetLoader.chapel);
        put("_d",  AssetLoader.dupre);
        put("_w",  AssetLoader.weyerhauser);
    }

    /**
     * Notes:
     * _ is a prefix for large tiles
     * only letters should be used otherwise
     * @param code
     * @param sprites
     */
    public void put(String code, TextureRegion... sprites) {
        int length = sprites.length;
        if (length > 1) {
            randomTileMap.put(code, length);
            for (int i=0; i<length; i++) {
                tileMap.put(code + i, sprites[i]);
            }
        } else {
            tileMap.put(code, sprites[0]);
        }
    }

    public TextureRegion get(String tileKey) {
        tileKey = tileKey.trim();
        if (randomTileMap.containsKey(tileKey)) {
            tileKey += random.nextInt(randomTileMap.get(tileKey));
        }
        return tileMap.get(tileKey);
    }

    public boolean contains(String tileKey) {
        return tileMap.containsKey(tileKey) || randomTileMap.containsKey(tileKey);
    }
}
