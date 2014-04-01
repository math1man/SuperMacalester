package com.arnopaja.supermac.helpers;

import com.arnopaja.supermac.world.grid.Building;
import com.arnopaja.supermac.world.grid.Grid;
import com.arnopaja.supermac.world.objects.Tile;

import java.util.Arrays;
import java.util.regex.Pattern;

/**
 * @author Ari Weiland
 */
public class MapLoader {

    private static enum Leaders {
        MAP_NAME("map"),
        BUILDING_NAME("building"),
        FLOOR_COUNT("floors"),
        FIRST_FLOOR_INDEX("first_floor_index");

        public final String leader;
        public final int length;

        Leaders(String leader) {
            this.leader = leader;
            this.length = leader.length();
        }

        public boolean matches(String string) {
            return Pattern.matches("<" + leader + ":.*" + ">", string);
        }

        public String getParam(String string) {
            if (matches(string)) {
                return string.substring(length+2, string.length()-1).trim();
            }
            return null;
        }

        public String getEnd() {
            return "</" + leader + ">";
        }
    }

    public static final TileMap TILE_MAP = new TileMap();

    public static Grid generateMap(String name) {
        String raw = AssetLoader.mapHandle.readString();
        String[] lines = raw.split("\n");
        int[] indexes = getIndexes(name, lines);
        if (indexes == null) {
            return null;
        }
        return parseGrid(Arrays.copyOfRange(
                lines, indexes[0], indexes[1]));
    }

    public static Building generateBuilding(String name) {
        String raw = AssetLoader.mapHandle.readString();
        String[] lines = raw.split("\n");
        int[] indexes = getIndexes(name, lines);
        if (indexes == null) {
            return null;
        }
        int floorCount = new Integer(Leaders.FLOOR_COUNT.getParam(lines[indexes[2]]));
        int height = (indexes[1] - indexes[0]) / floorCount;
        Grid[] floors = new Grid[floorCount];
        for (int i=0; i<floorCount; i++) {
            floors[i] = parseGrid(Arrays.copyOfRange(lines, i * height, i + (height+1)));
        }
        int firstFloorIndex = new Integer(Leaders.FIRST_FLOOR_INDEX.getParam(lines[indexes[3]]));
        return new Building(floors, firstFloorIndex);
    }

    private static int[] getIndexes(String name, String[] lines) {
        boolean foundMap = false;
        boolean foundBuilding = false;
        int[] indexes = new int[4];
        for (int i=0; i < lines.length; i++) {
            if (name.equals(Leaders.MAP_NAME.getParam(lines[i]))) {
                foundMap = true;
                indexes[0] = i + 1;
                indexes[2] = -1;
                indexes[3] = -1;
            } else if (name.equals(Leaders.BUILDING_NAME.getParam(lines[i]))
                    && Leaders.FLOOR_COUNT.matches(lines[i+1])
                    && Leaders.FIRST_FLOOR_INDEX.matches(lines[i+2])) {
                foundBuilding = true;
                indexes[0] = i + 1;
                indexes[2] = i + 2;
                indexes[3] = i + 3;
            }
            if ((foundMap && lines[i].trim().equals(Leaders.MAP_NAME.getEnd())) ||
                    (foundBuilding && lines[i].trim().equals(Leaders.BUILDING_NAME.getEnd()))) {
                indexes[1] = i;
                return indexes;
            }
        }
        return null;
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
