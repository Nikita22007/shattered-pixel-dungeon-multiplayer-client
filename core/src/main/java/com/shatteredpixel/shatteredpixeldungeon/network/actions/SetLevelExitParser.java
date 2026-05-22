package com.shatteredpixel.shatteredpixeldungeon.network.actions;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.network.ParseThread;
import org.json.JSONException;
import org.json.JSONObject;

public class SetLevelExitParser implements ActionParser {
    @Override
    public void parse(ParseThread parseThread, JSONObject action) throws JSONException {
        Dungeon.level.exit = action.getInt("pos");
    }
}
