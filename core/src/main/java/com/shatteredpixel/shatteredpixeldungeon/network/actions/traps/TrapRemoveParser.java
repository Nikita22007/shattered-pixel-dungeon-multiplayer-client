package com.shatteredpixel.shatteredpixeldungeon.network.actions.traps;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.network.ParseThread;
import com.shatteredpixel.shatteredpixeldungeon.network.actions.ActionParser;
import org.json.JSONException;
import org.json.JSONObject;

public class TrapRemoveParser implements ActionParser {
    @Override
    public void parse(ParseThread parseThread, JSONObject action) throws JSONException {
        int pos = action.getInt("pos");
        if (Dungeon.level != null) {
            Dungeon.level.traps.remove(pos);
            com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene.updateMap(pos);
        }
    }
}
