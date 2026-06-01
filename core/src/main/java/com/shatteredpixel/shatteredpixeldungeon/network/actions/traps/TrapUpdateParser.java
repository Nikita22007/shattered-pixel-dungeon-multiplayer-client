package com.shatteredpixel.shatteredpixeldungeon.network.actions.traps;

import com.shatteredpixel.shatteredpixeldungeon.network.ParseThread;
import com.shatteredpixel.shatteredpixeldungeon.network.actions.ActionParser;
import org.json.JSONException;
import org.json.JSONObject;

public class TrapUpdateParser implements ActionParser {
    @Override
    public void parse(ParseThread parseThread, JSONObject action) throws JSONException {
        parseThread.parseTrap(action);
    }
}
