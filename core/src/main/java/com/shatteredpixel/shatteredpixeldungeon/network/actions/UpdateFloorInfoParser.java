package com.shatteredpixel.shatteredpixeldungeon.network.actions;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.network.JsonStringHelper;
import com.shatteredpixel.shatteredpixeldungeon.network.ParseThread;
import org.json.JSONException;
import org.json.JSONObject;

public class UpdateFloorInfoParser implements ActionParser {
    @Override
    public void parse(ParseThread parseThread, JSONObject action) throws JSONException {
        if (action.has("depth")) {
            Dungeon.depth = action.getInt("depth");
        }
        if (action.has("branch")) {
            Dungeon.branch = action.getInt("branch");
        }
        if (Dungeon.level != null && action.has("feeling")) {
            Dungeon.level.feeling = Level.Feeling.valueOf(JsonStringHelper.getString(action, "feeling"));
        }
    }
}
