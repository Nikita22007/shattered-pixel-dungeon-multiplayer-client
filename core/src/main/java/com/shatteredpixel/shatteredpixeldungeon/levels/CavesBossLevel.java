package com.shatteredpixel.shatteredpixeldungeon.levels;

import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.effects.BlobEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.SparkParticle;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.DM300Sprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.PylonSprite;
import com.watabou.noosa.particles.Emitter;
import com.watabou.utils.GameMath;
import com.watabou.utils.Random;

public class CavesBossLevel {
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
}
