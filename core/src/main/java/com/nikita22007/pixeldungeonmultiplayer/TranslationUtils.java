package com.nikita22007.pixeldungeonmultiplayer;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;

public class TranslationUtils {
    public static String translateTilesTexture(String texture) {
        switch (texture){
            case "tiles0.png": return Assets.Environment.TILES_SEWERS;
            case "tiles1.png": return Assets.Environment.TILES_PRISON;
            case "tiles2.png": return Assets.Environment.TILES_CAVES;
            case "tiles3.png": return Assets.Environment.TILES_CITY;
            case "tiles4.png": return Assets.Environment.TILES_HALLS;
            default: return "tiles_city";
        }
    }

    public static int translateCell(int id) {
        switch (id) {
            case 0: return Terrain.CHASM;
            case 1: return Terrain.EMPTY;
            case 2: return Terrain.GRASS;
            case 3: return Terrain.EMPTY_WELL;
            case 4: return Terrain.WALL;
            case 5: return Terrain.DOOR;
            case 6: return Terrain.OPEN_DOOR;
            case 7: return Terrain.ENTRANCE;
            case 8: return Terrain.EXIT;
            case 9: return Terrain.EMBERS;
            case 10: return Terrain.LOCKED_DOOR;
            case 11: return Terrain.PEDESTAL;
            case 12: return Terrain.WALL_DECO;
            case 13: return Terrain.BARRICADE;
            case 14: return Terrain.EMPTY_SP;
            case 15: return Terrain.PEDESTAL;

            default: return id;
        }
    }
}
