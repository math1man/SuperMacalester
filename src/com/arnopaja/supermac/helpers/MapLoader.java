package com.arnopaja.supermac.helpers;

import com.arnopaja.supermac.world.grid.Building;
import com.arnopaja.supermac.world.grid.Grid;
import com.arnopaja.supermac.world.objects.Tile;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.regex.Pattern;

/**
 * TODO: add non-quest entities to the maps
 * @author Ari Weiland
 */
public class MapLoader {

    private static final Random random = new Random();

    private static final Map<String, TextureRegion> tileMap = new HashMap<String, TextureRegion>();
    private static final Map<String, Integer> randomTileMap = new HashMap<String, Integer>();

    public static Grid generateGrid(String name) {
        return parseGrid(AssetLoader.getMap(name));
    }

    public static Building generateBuilding(String name) {
        String raw = AssetLoader.getMap(name);
        String[] floorStrings = raw.split("<floor>");
        int firstFloorIndex = 0;

        int floorCount = floorStrings.length;
        Grid[] floors = new Grid[floorCount];
        for (int i=0; i<floorCount; i++) {
            if (i == 0) {
                String[] temp = floorStrings[i].split("\n", 2);
                String first = temp[0].trim();
                if (Pattern.matches("<first floor:\\d*>", first)) {
                    floorStrings[i] = temp[1];
                    firstFloorIndex = Integer.parseInt(first.replaceAll("\\D*", ""));
                }
            }
            floors[i] = parseGrid(floorStrings[i]);
        }
        return new Building(floors, firstFloorIndex);
    }

    private static Grid parseGrid(String string) {
        String[] lines = string.split("\n");
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
        return new Grid(tileArray);
    }

    public static void initTileMap() {
        Tile.TILE_MAP.put("g",   AssetLoader.grass0,
                                 AssetLoader.grass1,
                                 AssetLoader.grass2);
        Tile.TILE_MAP.put("b",   AssetLoader.bush);
        Tile.TILE_MAP.put("bh",  AssetLoader.bushH);
        Tile.TILE_MAP.put("bv",  AssetLoader.bushV);
        Tile.TILE_MAP.put("bfh", AssetLoader.bushFlowersH);
        Tile.TILE_MAP.put("bfv", AssetLoader.bushFlowersV);
        Tile.TILE_MAP.put("ts",  AssetLoader.treeSmall);
        Tile.TILE_MAP.put("tb", AssetLoader.treeBig);
        Tile.TILE_MAP.put("c",   AssetLoader.cobble);
        Tile.TILE_MAP.put("cr",  AssetLoader.cobbleRed);
        Tile.TILE_MAP.put("a",   AssetLoader.asphalt);
        Tile.TILE_MAP.put("ah",  AssetLoader.asphaltLineH);
        Tile.TILE_MAP.put("av",  AssetLoader.asphaltLineV);
        Tile.TILE_MAP.put("an",  AssetLoader.asphaltEdgeN);
        Tile.TILE_MAP.put("ae",  AssetLoader.asphaltEdgeE);
        Tile.TILE_MAP.put("as",  AssetLoader.asphaltEdgeS);
        Tile.TILE_MAP.put("aw",  AssetLoader.asphaltEdgeW);
        Tile.TILE_MAP.put("ane", AssetLoader.asphaltCornerNE);
        Tile.TILE_MAP.put("ase", AssetLoader.asphaltCornerSE);
        Tile.TILE_MAP.put("asw", AssetLoader.asphaltCornerSW);
        Tile.TILE_MAP.put("anw", AssetLoader.asphaltCornerNW);

        Tile.TILE_MAP.put("_ar", AssetLoader.art);
        Tile.TILE_MAP.put("_ac", AssetLoader.artCommons);
        Tile.TILE_MAP.put("_bi", AssetLoader.bigelow);
        Tile.TILE_MAP.put("_cc", AssetLoader.campusCenter);
        Tile.TILE_MAP.put("_ca", AssetLoader.carnegie);
        Tile.TILE_MAP.put("_ch", AssetLoader.chapel);
        Tile.TILE_MAP.put("_do", AssetLoader.doty);
        Tile.TILE_MAP.put("_du", AssetLoader.dupre);
        Tile.TILE_MAP.put("_hu", AssetLoader.humanities);
        Tile.TILE_MAP.put("_ka", AssetLoader.kagin);
        Tile.TILE_MAP.put("_ki", AssetLoader.kirk);
        Tile.TILE_MAP.put("_lc", AssetLoader.leonardCenter);
        Tile.TILE_MAP.put("_li", AssetLoader.library);
        Tile.TILE_MAP.put("_ma", AssetLoader.markim);
        Tile.TILE_MAP.put("_mu", AssetLoader.music);
        Tile.TILE_MAP.put("_om", AssetLoader.oldMain);
        Tile.TILE_MAP.put("_ol", AssetLoader.olin);
        Tile.TILE_MAP.put("_ri", AssetLoader.rice);
        Tile.TILE_MAP.put("_th", AssetLoader.theatre);
        Tile.TILE_MAP.put("_tm", AssetLoader.thirtyMac);
        Tile.TILE_MAP.put("_tu", AssetLoader.turk);
        Tile.TILE_MAP.put("_wa", AssetLoader.wallace);
        Tile.TILE_MAP.put("_we", AssetLoader.weyerhauser);
    }
}
