package com.shatteredpixel.shatteredpixeldungeon.network.actions;

import com.shatteredpixel.shatteredpixeldungeon.network.ParseThread;
import org.json.JSONException;
import org.json.JSONObject;

public class ShowMessageParser implements ActionParser {
    @Override
    public void parse(ParseThread parseThread, JSONObject action) throws JSONException {
        JSONObject msg = action.getJSONObject("message");
        // parseMessages expected a JSONArray, so we wrap it for compatibility or call directly
        parseThread.parseMessage(msg);
    }
}
