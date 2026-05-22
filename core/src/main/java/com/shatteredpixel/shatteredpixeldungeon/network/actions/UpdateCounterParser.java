package com.shatteredpixel.shatteredpixeldungeon.network.actions;

import com.shatteredpixel.shatteredpixeldungeon.network.ParseThread;
import org.json.JSONException;
import org.json.JSONObject;

public class UpdateCounterParser implements ActionParser {
    @Override
    public void parse(ParseThread parseThread, JSONObject action) throws JSONException {
        parseThread.parseUI(action);
    }
}
