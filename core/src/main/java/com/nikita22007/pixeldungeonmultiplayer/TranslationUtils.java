package com.nikita22007.pixeldungeonmultiplayer;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.ParalyticGas;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.Trap;

import static com.shatteredpixel.shatteredpixeldungeon.levels.traps.Trap.*;

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
    //TODO: add trap support
    public static int translateCell(int id, int cell) {
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
            case 15: return Terrain.HIGH_GRASS;
            case 24: return Terrain.EMPTY_DECO;
            case 25: return Terrain.LOCKED_EXIT;
            case 26: return Terrain.UNLOCKED_EXIT;
            //no sign
            case 29: return Terrain.GRASS;
            case 34: return Terrain.WELL;
            case 35: return Terrain.STATUE;
            case 36: return Terrain.STATUE_SP;
            case 41: return Terrain.BOOKSHELF;
            case 42: return Terrain.ALCHEMY;
            case 43: return Terrain.CHASM;
            case 44: return Terrain.CHASM;
            case 45: return Terrain.CHASM;
            case 46: return Terrain.CHASM;
            case 16: return Terrain.SECRET_DOOR;
            case 17: setTrap(cell, GREEN, GRILL, true); return Terrain.TRAP;
            case 18: setTrap(cell, GREEN, GRILL, false); return Terrain.SECRET_TRAP;
            case 19: setTrap(cell, RED, GRILL, true);return Terrain.TRAP;
            case 20: setTrap(cell, ORANGE, DOTS, false);return Terrain.SECRET_TRAP;

            case 21: setTrap(cell, YELLOW, DOTS, true);return Terrain.TRAP;
            case 22: setTrap(cell, YELLOW, DOTS, false);return Terrain.SECRET_TRAP;
            case 23: setTrap(cell, YELLOW, DOTS, true);return Terrain.INACTIVE_TRAP;
            case 27: setTrap(cell, VIOLET, DOTS, true);return Terrain.TRAP;
            case 28: setTrap(cell, VIOLET, DOTS, false);return Terrain.SECRET_TRAP;
            case 30: setTrap(cell, RED, DOTS, true);return Terrain.TRAP;
            case 31: setTrap(cell, RED, DOTS, false);return Terrain.SECRET_TRAP;
            case 32: setTrap(cell, TEAL, DOTS, true);return Terrain.TRAP;
            case 33: setTrap(cell, TEAL, DOTS, false);return Terrain.SECRET_TRAP;
            case 37: setTrap(cell, GREY, DOTS, true);return Terrain.TRAP;
            case 38: setTrap(cell, GREY, DOTS, false);return Terrain.SECRET_TRAP;
            case 39: setTrap(cell, TEAL, WAVES, true);return Terrain.TRAP;
            case 40: setTrap(cell, TEAL, WAVES, true);return Terrain.SECRET;

            //TODO: check this
            case 48: return Terrain.WATER;
            case 63: return Terrain.WATER;
            //TODO: check this. Is this from codes?
            //case 00x1: return Terrain.PASSABLE;
            default: return id;
        }
    }
    private static void setTrap(int cell, int trapColor, int trapShape, boolean trapVisible) {
        Trap trap = new Trap() {
            {
                color = trapColor;
                shape = trapShape;
                visible = trapVisible;
            }
            @Override
            public void activate() {

            }

        };
        Dungeon.level.setTrap(trap, cell);
    }
}
