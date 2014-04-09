package com.arnopaja.supermac.helpers;

import com.arnopaja.supermac.helpers.parser.Leader;
import com.arnopaja.supermac.helpers.parser.ParsedObject;
import com.arnopaja.supermac.helpers.parser.Parser;
import com.arnopaja.supermac.world.grid.Building;
import com.arnopaja.supermac.world.grid.Grid;
import com.arnopaja.supermac.world.objects.Tile;

import java.util.Arrays;

/**
 * TODO: add non-quest entities to the maps
 * @author Ari Weiland
 */
public class MapLoader {

    public static Grid generateMap(String name) {
        ParsedObject object = getParsed(name);
        if (object == null) {
            return null;
        }
        return parseGrid(object.getData());
    }

    public static Building generateBuilding(String name) {
        ParsedObject object = getParsed(name);
        if (object == null) {
            return null;
        }
        int floorCount = object.getInt(Leader.FLOOR_COUNT);
        String[] data = object.getData();
        int height = data.length / floorCount;
        Grid[] floors = new Grid[floorCount];
        for (int i=0; i<floorCount; i++) {
            floors[i] = parseGrid(Arrays.copyOfRange(data, i * height, (i + 1) * height));
        }
        return new Building(floors, object.getInt(Leader.FIRST_FLOOR_INDEX));
    }

    private static ParsedObject getParsed(String name) {
        return Parser.parse(name, Leader.MAP, AssetLoader.mapHandle);
    }

    private static Grid parseGrid(String[] lines) {
        int height = lines.length;
        int width = lines[0].split("\t").length;
        Tile[][] tileArray = new Tile[width][height];
        for (int i=0; i<height; i++) {
            String[] tileCodes = lines[i].split("\t");
            for (int j=0; j<width; j++) {
                tileArray[j][i] = Tile.createTile(tileCodes[j].trim());
            }
        }
        SpriteUtils.split(tileArray);
        return new Grid(tileArray);
    }

    public static final TileMap TILE_MAP = new TileMap();

    public static void initTileMap() {
        TILE_MAP.put("g",   AssetLoader.grass0, AssetLoader.grass1, AssetLoader.grass2);
        TILE_MAP.put("bh",  AssetLoader.bushH);
        TILE_MAP.put("bv",  AssetLoader.bushV);
        TILE_MAP.put("bfh", AssetLoader.bushFlowersH);
        TILE_MAP.put("bfv", AssetLoader.bushFlowersV);
        TILE_MAP.put("ts",  AssetLoader.treeSmall);
        TILE_MAP.put("_tb", AssetLoader.treeBig);
        TILE_MAP.put("c",   AssetLoader.cobble);
        TILE_MAP.put("cr",  AssetLoader.cobbleRed);
        TILE_MAP.put("a",   AssetLoader.asphalt);
        TILE_MAP.put("ah",  AssetLoader.asphaltLineH);
        TILE_MAP.put("av",  AssetLoader.asphaltLineV);
        TILE_MAP.put("an",  AssetLoader.asphaltEdgeN);
        TILE_MAP.put("ae",  AssetLoader.asphaltEdgeE);
        TILE_MAP.put("as",  AssetLoader.asphaltEdgeS);
        TILE_MAP.put("aw",  AssetLoader.asphaltEdgeW);
        TILE_MAP.put("ane", AssetLoader.asphaltCornerNE);
        TILE_MAP.put("ase", AssetLoader.asphaltCornerSE);
        TILE_MAP.put("asw", AssetLoader.asphaltCornerSW);
        TILE_MAP.put("anw", AssetLoader.asphaltCornerNW);
//        TILE_MAP.put("_cc", AssetLoader.campusCenter);
//        TILE_MAP.put("_ch", AssetLoader.chapel);
        TILE_MAP.put("_d",  AssetLoader.dupre);
        TILE_MAP.put("_w",  AssetLoader.weyerhauser);
    }

}
