package com.arnopaja.supermac.helpers.load;

import com.arnopaja.supermac.world.grid.Grid;
import com.arnopaja.supermac.world.objects.Tile;
import com.badlogic.gdx.files.FileHandle;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Ari Weiland
 */
public class MapLoader {

    public static Map<String, Grid> generateGrids(FileHandle folder) {
        FileHandle[] handles = folder.list("txt");
        Map<String, Grid> maps = new HashMap<String, Grid>();
        for (FileHandle handle : handles) {
            String name = handle.nameWithoutExtension().toLowerCase().replaceAll("[\\W_]", "");
            String data = handle.readString();
            maps.put(name, parseGrid(name, data));
        }
        return maps;
    }

    private static Grid parseGrid(String name, String raw) {
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
        return new Grid(name, tileArray);
    }

    public static void initTileMap() {
        Tile.TILE_MAP.put("g",    AssetLoader.grass0,
                                  AssetLoader.grass1,
                                  AssetLoader.grass2);
        Tile.TILE_MAP.put("b",    AssetLoader.bush);
        Tile.TILE_MAP.put("bh",   AssetLoader.bushH);
        Tile.TILE_MAP.put("bv",   AssetLoader.bushV);
        Tile.TILE_MAP.put("bfh",  AssetLoader.bushFlowersH);
        Tile.TILE_MAP.put("bfv",  AssetLoader.bushFlowersV);
        Tile.TILE_MAP.put("ts",   AssetLoader.treeSmall);
        Tile.TILE_MAP.put("tb",   AssetLoader.treeBig);
        Tile.TILE_MAP.put("s",    AssetLoader.sidewalk);
        Tile.TILE_MAP.put("c",    AssetLoader.cobble);
        Tile.TILE_MAP.put("cr",   AssetLoader.cobbleRed);
        Tile.TILE_MAP.put("a",    AssetLoader.asphalt);
        Tile.TILE_MAP.put("ah",   AssetLoader.asphaltLineH);
        Tile.TILE_MAP.put("av",   AssetLoader.asphaltLineV);
        Tile.TILE_MAP.put("an",   AssetLoader.asphaltGrassN);
        Tile.TILE_MAP.put("ae",   AssetLoader.asphaltGrassE);
        Tile.TILE_MAP.put("as",   AssetLoader.asphaltGrassS);
        Tile.TILE_MAP.put("aw",   AssetLoader.asphaltGrassW);
        Tile.TILE_MAP.put("ane",  AssetLoader.asphaltGrassNE);
        Tile.TILE_MAP.put("ase",  AssetLoader.asphaltGrassSE);
        Tile.TILE_MAP.put("asw",  AssetLoader.asphaltGrassSW);
        Tile.TILE_MAP.put("anw",  AssetLoader.asphaltGrassNW);
        Tile.TILE_MAP.put("acn",  AssetLoader.asphaltCobbleN);
        Tile.TILE_MAP.put("ace",  AssetLoader.asphaltCobbleE);
        Tile.TILE_MAP.put("acs",  AssetLoader.asphaltCobbleS);
        Tile.TILE_MAP.put("acw",  AssetLoader.asphaltCobbleW);
        Tile.TILE_MAP.put("acne", AssetLoader.asphaltCobbleNE);
        Tile.TILE_MAP.put("acse", AssetLoader.asphaltCobbleSE);
        Tile.TILE_MAP.put("acsw", AssetLoader.asphaltCobbleSW);
        Tile.TILE_MAP.put("acnw", AssetLoader.asphaltCobbleNW);
        Tile.TILE_MAP.put("ast",  AssetLoader.asteroid);

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
        Tile.TILE_MAP.put("_or", AssetLoader.olinRiceStairs);
        Tile.TILE_MAP.put("_ri", AssetLoader.rice);
        Tile.TILE_MAP.put("_th", AssetLoader.theatre);
        Tile.TILE_MAP.put("_tm", AssetLoader.thirtyMac);
        Tile.TILE_MAP.put("_tu", AssetLoader.turk);
        Tile.TILE_MAP.put("_wa", AssetLoader.wallace);
        Tile.TILE_MAP.put("_we", AssetLoader.weyerhauser);
        Tile.TILE_MAP.put("_wh", AssetLoader.wh);
        Tile.TILE_MAP.put("_wv", AssetLoader.wv);
        Tile.TILE_MAP.put("_wne", AssetLoader.wne);
        Tile.TILE_MAP.put("_wse", AssetLoader.wse);
        Tile.TILE_MAP.put("_wsw", AssetLoader.wsw);
        Tile.TILE_MAP.put("_wnw", AssetLoader.wnw);
        Tile.TILE_MAP.put("_wtn", AssetLoader.wtn);
        Tile.TILE_MAP.put("_wte", AssetLoader.wte);
        Tile.TILE_MAP.put("_wts", AssetLoader.wts);
        Tile.TILE_MAP.put("_wtw", AssetLoader.wtw);
        Tile.TILE_MAP.put("_wp", AssetLoader.wp);
        Tile.TILE_MAP.put("_wen", AssetLoader.wen);
        Tile.TILE_MAP.put("_wee" , AssetLoader.wee);
        Tile.TILE_MAP.put("_wes", AssetLoader.wes);
        Tile.TILE_MAP.put("_wew", AssetLoader.wew);
        Tile.TILE_MAP.put("_wene", AssetLoader.wene);
        Tile.TILE_MAP.put("_wese", AssetLoader.wese);
        Tile.TILE_MAP.put("_wesw", AssetLoader.wesw);
        Tile.TILE_MAP.put("_wenw", AssetLoader.wenw);
        Tile.TILE_MAP.put("_wetn", AssetLoader.wetn);
        Tile.TILE_MAP.put("_wets", AssetLoader.wets);
        Tile.TILE_MAP.put("_wetw", AssetLoader.wetw);
        Tile.TILE_MAP.put("_wete", AssetLoader.wete);




    }
}
