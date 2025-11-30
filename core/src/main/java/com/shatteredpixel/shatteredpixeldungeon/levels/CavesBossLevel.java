package com.shatteredpixel.shatteredpixeldungeon.levels;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.effects.BlobEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.SparkParticle;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.DM300Sprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.PylonSprite;
import com.shatteredpixel.shatteredpixeldungeon.tiles.CustomTilemap;
import com.watabou.noosa.Image;
import com.watabou.noosa.Tilemap;
import com.watabou.noosa.particles.Emitter;
import com.watabou.utils.GameMath;
import com.watabou.utils.Random;
import com.watabou.utils.Rect;

public class CavesBossLevel {
    //hack
    private static int WIDTH = 33;
    private static int HEIGHT = 42;

    public static Rect diggableArea = new Rect(2, 11, 31, 40);
    public static Rect mainArena = new Rect(5, 14, 28, 37);
    public static Rect gate = new Rect(14, 13, 19, 14);
    public static int[] pylonPositions = new int[]{ 4 + 13*WIDTH, 28 + 13*WIDTH, 4 + 37*WIDTH, 28 + 37*WIDTH };

    public static class PylonEnergy extends Blob {



        @Override
        public void fullyClear() {
            super.fullyClear();
            energySourceSprite = null;
        }

        private static CharSprite energySourceSprite = null;

        private static Emitter.Factory DIRECTED_SPARKS = new Emitter.Factory() {
            @Override
            public void emit(Emitter emitter, int index, float x, float y) {
                if (energySourceSprite == null){
                    for (Char c : Actor.chars()){
                        if (c.sprite instanceof PylonSprite && c.alignment != Char.Alignment.NEUTRAL){
                            energySourceSprite = c.sprite;
                            break;
                        } else if (c.sprite instanceof DM300Sprite){
                            energySourceSprite = c.sprite;
                        }
                    }
                    if (energySourceSprite == null){
                        return;
                    }
                }

                float dist = (float)Math.max( Math.abs(energySourceSprite.x - x), Math.abs(energySourceSprite.y - y) );
                dist = GameMath.gate(0, dist-40, 320);
                //more sparks closer up
                if (Random.Float(360) > dist) {

                    SparkParticle s = ((SparkParticle) emitter.recycle(SparkParticle.class));
                    s.resetAttracting(x, y, energySourceSprite);
                }
            }

            @Override
            public boolean lightMode() {
                return true;
            }
        };

        @Override
        public String tileDesc() {
            return Messages.get(CavesBossLevel.class, "energy_desc");
        }

        @Override
        public void use( BlobEmitter emitter ) {
            super.use( emitter );
            energySourceSprite = null;
            emitter.pour(DIRECTED_SPARKS, 0.08f);
        }

    }
    public static class CityEntrance extends CustomTilemap {

        {
            texture = Assets.Environment.CAVES_BOSS;
        }

        private static short[] entryWay = new short[]{
                -1,  7,  7,  7, -1,
                -1,  1,  2,  3, -1,
                8,  1,  2,  3, 12,
                16,  9, 10, 11, 20,
                16, 16, 18, 20, 20,
                16, 17, 18, 19, 20,
                16, 16, 18, 20, 20,
                16, 17, 18, 19, 20,
                16, 16, 18, 20, 20,
                16, 17, 18, 19, 20,
                24, 25, 26, 27, 28
        };

        @Override
        public Tilemap create() {
            Tilemap v = super.create();
            int[] data = new int[tileW*tileH];
            int entryPos = 0;
            for (int i = 0; i < data.length; i++){

                //override the entryway
                if (i % tileW == tileW/2 - 2){
                    data[i++] = entryWay[entryPos++];
                    data[i++] = entryWay[entryPos++];
                    data[i++] = entryWay[entryPos++];
                    data[i++] = entryWay[entryPos++];
                    data[i] = entryWay[entryPos++];

                    //otherwise check if we are on row 2 or 3, in which case we need to override walls
                } else {
                    if (i / tileW == 2) data[i] = 13;
                    else if (i / tileW == 3) data[i] = 21;
                    else data[i] = -1;
                }
            }
            v.map( data, tileW );
            return v;
        }

    }

    public static class EntranceOverhang extends CustomTilemap{

        {
            texture = Assets.Environment.CAVES_BOSS;
        }

        private static short[] entryWay = new short[]{
                0,  7,  7,  7,  4,
                0, 15, 15, 15,  4,
                -1, 23, 23, 23, -1,
                -1, -1, -1, -1, -1,
                -1,  6, -1, 14, -1,
                -1, -1, -1, -1, -1,
                -1,  6, -1, 14, -1,
                -1, -1, -1, -1, -1,
                -1,  6, -1, 14, -1,
                -1, -1, -1, -1, -1,
                -1, -1, -1, -1, -1,
        };

        @Override
        public Tilemap create() {
            Tilemap v = super.create();
            int[] data = new int[tileW*tileH];
            int entryPos = 0;
            for (int i = 0; i < data.length; i++){

                //copy over this row of the entryway
                if (i % tileW == tileW/2 - 2){
                    data[i++] = entryWay[entryPos++];
                    data[i++] = entryWay[entryPos++];
                    data[i++] = entryWay[entryPos++];
                    data[i++] = entryWay[entryPos++];
                    data[i] = entryWay[entryPos++];
                } else {
                    data[i] = -1;
                }
            }
            v.map( data, tileW );
            return v;
        }

    }

    public static class ArenaVisuals extends CustomTilemap {

        {
            texture = Assets.Environment.CAVES_BOSS;
        }

        @Override
        public Tilemap create() {
            Tilemap v = super.create();
            updateState( );

            return v;
        }

        public void updateState( ){
            if (vis != null){
                int[] data = new int[tileW*tileH];
                int j = Dungeon.level.width() * tileY;
                for (int i = 0; i < data.length; i++){

                    if (Dungeon.level.map[j] == Terrain.EMPTY_SP) {
                        for (int k : pylonPositions) {
                            if (k == j) {
                                //FIXME Maybe also send dungeon locking and unlocking?
                                if (Actor.findChar(k) != null && !(Actor.findChar(k).sprite instanceof PylonSprite)) {
                                    data[i] = 38;
                                }
                            } else if (Dungeon.level.adjacent(k, j)) {
                                int w = Dungeon.level.width;
                                data[i] = 54 + (j % w + 8 * (j / w)) - (k % w + 8 * (k / w));
                            }
                        }
                    } else if (Dungeon.level.map[j] == Terrain.INACTIVE_TRAP){
                        data[i] = 37;
                    } else if (gate.inside(Dungeon.level.cellToPoint(j))){
                        int idx = Dungeon.level.solid[j] ? 40 : 32;
                        data[i++] = idx++;
                        data[i++] = idx++;
                        data[i++] = idx++;
                        data[i++] = idx++;
                        data[i] = idx;
                        j += 4;
                    } else {
                        data[i] = -1;
                    }

                    j++;
                }
                vis.map(data, tileW);
            }
        }

        @Override
        public String name(int tileX, int tileY) {
            int i = tileX + tileW*(tileY + this.tileY);
            if (Dungeon.level.map[i] == Terrain.INACTIVE_TRAP){
                return Messages.get(CavesBossLevel.class, "wires_name");
            } else if (gate.inside(Dungeon.level.cellToPoint(i))){
                return Messages.get(CavesBossLevel.class, "gate_name");
            }

            return super.name(tileX, tileY);
        }

        @Override
        public String desc(int tileX, int tileY) {
            int i = tileX + tileW*(tileY + this.tileY);
            if (Dungeon.level.map[i] == Terrain.INACTIVE_TRAP){
                return Messages.get(CavesBossLevel.class, "wires_desc");
            } else if (gate.inside(Dungeon.level.cellToPoint(i))){
                if (Dungeon.level.solid[i]){
                    return Messages.get(CavesBossLevel.class, "gate_desc");
                } else {
                    return Messages.get(CavesBossLevel.class, "gate_desc_broken");
                }
            }
            return super.desc(tileX, tileY);
        }

        @Override
        public Image image(int tileX, int tileY) {
            int i = tileX + tileW*(tileY + this.tileY);
            for (int k : pylonPositions){
                if (Dungeon.level.distance(i, k) <= 1){
                    return null;
                }
            }

            return super.image(tileX, tileY);

        }
    }
}
