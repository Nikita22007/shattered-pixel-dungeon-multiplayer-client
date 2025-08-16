package com.shatteredpixel.shatteredpixeldungeon.tiles;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.Trap;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.network.SendData;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.watabou.noosa.Tilemap;
import com.watabou.noosa.tweeners.AlphaTweener;
import com.watabou.utils.Rect;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Arrays;

public class FadingTraps extends CustomTilemap {
    public static FadingTraps instance;

    {
        texture = Assets.Environment.TERRAIN_FEATURES;
    }

    int[] data;
    float fadeDelay = 2f;
    @Override
    public Tilemap create() {
        Tilemap v = super.create();
        if (data == null) {
            data = new int[tileW * tileH];
            Arrays.fill(data, -1);
        }
        v.map(data, tileW);
        return v;
    }

    @Override
    public String name(int tileX, int tileY) {
        int cell = (this.tileX + tileX) + Dungeon.level.width() * (this.tileY + tileY);
        if (Dungeon.level.traps.get(cell) != null) {
            return Messages.titleCase(Dungeon.level.traps.get(cell).name());
        }
        return super.name(tileX, tileY);
    }

    @Override
    public String desc(int tileX, int tileY) {
        int cell = (this.tileX + tileX) + Dungeon.level.width() * (this.tileY + tileY);
        if (Dungeon.level.traps.get(cell) != null) {
            return Dungeon.level.traps.get(cell).desc();
        }
        return super.desc(tileX, tileY);
    }

    private void remove() {
        if (vis != null) {
            vis.killAndErase();
        }
        Dungeon.level.customTiles.remove(this);
    }
    public static void fromJSON(JSONObject object){
        if (object.has("kill")){
            instance.remove();
            instance = null;
            return;
        }
        boolean newInstance = object.getBoolean("new");
        if (newInstance){
            if (instance != null){
                instance.remove();
                instance = null;
            }
        }
        if (instance == null) {
            instance = new FadingTraps();
        }
        instance.tileX = object.getInt("tileX");
        instance.tileY = object.getInt("tileY");
        instance.tileH = object.getInt("tileH");
        instance.tileW = object.getInt("tileW");
        JSONArray trapData = object.getJSONArray("data");
        JSONObject trapObject;
        instance.data = new int[trapData.length()];
        for (int i = 0; i < trapData.length(); i++) {
            trapObject = trapData.getJSONObject(i);
            instance.data[trapObject.getInt("pos")] = trapObject.getInt("data");
            instance.fadeDelay = 2f;

        }
        if (newInstance) {
            GameScene.add(instance, false);
            Dungeon.level.customTiles.add(instance);
        }
        instance.vis.alpha((float) object.getDouble("alpha"));
    }
}