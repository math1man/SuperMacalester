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

    public static Grid generateMap(String name) {
        String raw = AssetLoader.mapHandle.readString();
        String[] lines = raw.split("\n");
        Params params = getIndexes(name, lines);
        if (params == null) {
            return null;
        }
        return parseGrid(Arrays.copyOfRange(lines, params.getStart(), params.getEnd()));
    }

    public static Building generateBuilding(String name) {
        String raw = AssetLoader.mapHandle.readString();
        String[] lines = raw.split("\n");
        Params params = getIndexes(name, lines);
        if (params == null) {
            return null;
        }
        int floorCount = params.getFloorCount();
        int height = params.getHeight();
        Grid[] floors = new Grid[floorCount];
        for (int i=0; i<floorCount; i++) {
            floors[i] = parseGrid(Arrays.copyOfRange(lines, i * height, i + (height+1)));
        }
        return new Building(floors, params.getFirstFloorIndex());
    }

    private static Params getIndexes(String name, String[] lines) {
        Params params = new Params();
        int start = -1;
        for (int i=0; i < lines.length; i++) {
            String line = lines[i].trim();
            if (start == -1) {
                if (Leaders.MAP_NAME.isParam(line, name)) {
                    start = i + 1;
                }
            } else {
                if (Leaders.FLOOR_COUNT.matches(line)) {
                    params.setFloorCount(Leaders.FLOOR_COUNT.getParam(line));
                    start++;
                } else if (Leaders.FIRST_FLOOR_INDEX.matches(line)) {
                    params.setFirstFloorIndex(Leaders.FIRST_FLOOR_INDEX.getParam(line));
                    start++;
                } else if (Leaders.MAP_NAME.isEnd(line)) {
                    params.setStart(start);
                    params.setEnd(i);
                    return params;
                }
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

    private static enum Leaders {
        MAP_NAME("map"),
        FLOOR_COUNT("floors"),
        FIRST_FLOOR_INDEX("first_floor_index");

        public final String leader;

        Leaders(String leader) {
            this.leader = leader;
        }

        public boolean matches(String line) {
            return Pattern.matches("<" + leader + ":.*" + ">", line);
        }

        public String getParam(String line) {
            if (matches(line)) {
                return line.substring(leader.length()+2, line.length()-1).trim();
            }
            return null;
        }

        public boolean isParam(String line, String param) {
            return param.equals(getParam(line));
        }

        public String getEnd() {
            return "</" + leader + ">";
        }

        public boolean isEnd(String line) {
            return line.equals(getEnd());
        }
    }

    private static class Params {
        private int start = 0; // inclusive
        private int end = 0;   // exclusive
        private int floorCount = 1;
        private int firstFloorIndex = 0;

        public int getStart() { return start; }
        public void setStart(int start) { this.start = start; }
        public int getEnd() { return end; }
        public void setEnd(int end) { this.end = end; }
        public int getFloorCount() { return floorCount; }
        public void setFloorCount(String floorCount) { this.floorCount = Integer.parseInt(floorCount); }
        public int getHeight() { return (end - start) / floorCount; }
        public int getFirstFloorIndex() { return firstFloorIndex; }
        public void setFirstFloorIndex(String firstFloorIndex) { this.firstFloorIndex = Integer.parseInt(firstFloorIndex); }
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
