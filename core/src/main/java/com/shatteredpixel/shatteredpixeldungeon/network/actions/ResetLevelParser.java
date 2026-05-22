package com.shatteredpixel.shatteredpixeldungeon.network.actions;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.SewerLevel;
import com.shatteredpixel.shatteredpixeldungeon.network.ParseThread;
import org.json.JSONException;
import org.json.JSONObject;

public class ResetLevelParser implements ActionParser {
    @Override
    public void parse(ParseThread parseThread, JSONObject action) throws JSONException {
        Level old_level = Dungeon.level;
        Dungeon.level = new SewerLevel();
        Dungeon.level.create(old_level);
    }
}
