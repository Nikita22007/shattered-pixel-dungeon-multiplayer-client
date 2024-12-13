package com.shatteredpixel.shatteredpixeldungeon.effects;

import com.badlogic.gdx.Gdx;
import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.tiles.DungeonTilemap;
import com.watabou.noosa.Game;
import com.watabou.noosa.Group;
import com.watabou.noosa.Image;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

public class PDLightning extends Group {
    private static final float DURATION = 0.3f;

    private float life;

    private int length;
    private float[] cx;
    private float[] cy;

    private Image[] arcsS;
    private Image[] arcsE;

    private Callback callback;

    public PDLightning( int[] cells, int length, Callback callback ) {

        super();

        this.callback = callback;

        Image proto = Effects.get( Effects.Type.LIGHTNING );
        float ox = 0;
        float oy = proto.height / 2;

        this.length = length;
        cx = new float[length];
        cy = new float[length];

        for (int i=0; i < length; i++) {
            int c = cells[i];
            cx[i] = (c % Dungeon.level.width() + 0.5f) * DungeonTilemap.SIZE;
            cy[i] = (c / Dungeon.level.width() + 0.5f) * DungeonTilemap.SIZE;
        }

        arcsS = new Image[length - 1];
        arcsE = new Image[length - 1];
        for (int i=0; i < length - 1; i++) {

            Image arc = arcsS[i] = new Image( proto );

            arc.x = cx[i] - arc.origin.x;
            arc.y = cy[i] - arc.origin.y;
            arc.origin.set( ox, oy );
            add( arc );

            arc = arcsE[i] = new Image( proto );
            arc.origin.set( ox, oy );
            add( arc );
        }

        life = DURATION;

        Sample.INSTANCE.play(Assets.Sounds.LIGHTNING );
    }

    private static final double A = 180 / Math.PI;

    @Override
    public void update() {
        super.update();

        if ((life -= Game.elapsed) < 0) {

            killAndErase();
            if (callback != null) {
                callback.call();
            }

        } else {

            float alpha = life / DURATION;

            for (int i=0; i < length - 1; i++) {

                float sx = cx[i];
                float sy = cy[i];
                float ex = cx[i+1];
                float ey = cy[i+1];

                float x2 = (sx + ex) / 2 + Random.Float( -4, +4 );
                float y2 = (sy + ey) / 2 + Random.Float( -4, +4 );

                float dx = x2 - sx;
                float dy = y2 - sy;
                Image arc = arcsS[i];
                arc.am = alpha;
                arc.angle = (float)(Math.atan2( dy, dx ) * A);
                arc.scale.x = (float)Math.sqrt( dx * dx + dy * dy ) / arc.width;

                dx = ex - x2;
                dy = ey - y2;
                arc = arcsE[i];
                arc.am = alpha;
                arc.angle = (float)(Math.atan2( dy, dx ) * A);
                arc.scale.x = (float)Math.sqrt( dx * dx + dy * dy ) / arc.width;
                arc.x = x2 - arc.origin.x;
                arc.y = y2 - arc.origin.x;
            }
        }
    }

    @Override
    public void draw() {
        Gdx.gl.glBlendFunc(Gdx.gl20.GL_SRC_ALPHA, Gdx.gl20.GL_ONE);
        super.draw();
        Gdx.gl.glBlendFunc(Gdx.gl20.GL_SRC_ALPHA, Gdx.gl20.GL_ONE_MINUS_SRC_ALPHA);
    }
}
