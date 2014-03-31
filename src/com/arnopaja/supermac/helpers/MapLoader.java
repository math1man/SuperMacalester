package com.arnopaja.supermac.helpers;

import com.arnopaja.supermac.world.grid.Building;
import com.arnopaja.supermac.world.grid.Grid;
import com.arnopaja.supermac.world.objects.Tile;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.*;
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

    private static final Random random = new Random();

    private static final Map<String, TextureRegion> tileMap = new HashMap<String, TextureRegion>();
    private static final Map<String, Integer> randomTileMap = new HashMap<String, Integer>();

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

    private static Grid parseGrid(String[] grid) {
        int height = grid.length;
        int width = grid[0].split("\t").length;
        Tile[][] tileArray = new Tile[width][height];
        for (int i=0; i<height; i++) {
            String line = grid[i];
            String[] tileCodes = line.split("\t");
            for (int j=0; j<width; j++) {
                tileArray[j][i] = Tile.createTile(tileCodes[j].trim());
            }
        }
        return new Grid(tileArray);
    }

    /* notes:
    _ is a prefix for buildings
    numerals should be used only for arbitrary tiles (ie. different grasses)
    asterisks are used to represent an arbitrary numeral in the data file
    */
    public static void initTileMap() {
        tileMap.put("g0", AssetLoader.grass0);
        tileMap.put("g1", AssetLoader.grass1);
        tileMap.put("g2", AssetLoader.grass2);
        tileMap.put("bh", AssetLoader.bushH);
        tileMap.put("bv", AssetLoader.bushV);
        tileMap.put("bfh", AssetLoader.bushFlowersH);
        tileMap.put("bfv", AssetLoader.bushFlowersV);
        tileMap.put("ts", AssetLoader.treeSmall);
        tileMap.put("tb", AssetLoader.treeBig);
        tileMap.put("c", AssetLoader.cobble);
        tileMap.put("cr", AssetLoader.cobbleRed);
        tileMap.put("a", AssetLoader.asphalt);
        tileMap.put("ah", AssetLoader.asphaltLineH);
        tileMap.put("av", AssetLoader.asphaltLineV);
        tileMap.put("an", AssetLoader.asphaltEdgeN);
        tileMap.put("ae", AssetLoader.asphaltEdgeE);
        tileMap.put("as", AssetLoader.asphaltEdgeS);
        tileMap.put("aw", AssetLoader.asphaltEdgeW);
        tileMap.put("ane", AssetLoader.asphaltCornerNE);
        tileMap.put("ase", AssetLoader.asphaltCornerSE);
        tileMap.put("asw", AssetLoader.asphaltCornerSW);
        tileMap.put("anw", AssetLoader.asphaltCornerNW);
        tileMap.put("_cc", AssetLoader.campusCenter);
        tileMap.put("_ch", AssetLoader.chapel);
        tileMap.put("_d", AssetLoader.dupre);
        tileMap.put("_w", AssetLoader.weyerhauser);

        randomTileMap.put("g", 3);
    }

    public static TextureRegion getTileSprite(String tileKey) {
        if (tileKey.contains("*")) {
            tileKey = tileKey.replace("*", "");
            int temp = randomTileMap.get(tileKey);
            tileKey += random.nextInt(temp);
        }
        return tileMap.get(tileKey);
    }
}
