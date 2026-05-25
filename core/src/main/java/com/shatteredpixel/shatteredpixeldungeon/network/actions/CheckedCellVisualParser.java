package com.shatteredpixel.shatteredpixeldungeon.network.actions;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.effects.CheckedCell;
import com.shatteredpixel.shatteredpixeldungeon.network.ParseThread;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import org.json.JSONException;
import org.json.JSONObject;

class CheckedCellVisualParser implements ActionParser {
    public void parse(ParseThread parseThread, JSONObject action) throws JSONException {
        if (Dungeon.level.heroFOV[action.getInt("pos")]) {
            GameScene.effect(new CheckedCell(action.getInt("pos")));
        }
    }
}
