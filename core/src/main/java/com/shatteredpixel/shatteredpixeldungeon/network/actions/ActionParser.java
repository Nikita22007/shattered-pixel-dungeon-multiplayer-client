package com.shatteredpixel.shatteredpixeldungeon.network.actions;

import com.shatteredpixel.shatteredpixeldungeon.network.ParseThread;

import org.json.JSONException;
import org.json.JSONObject;

public interface ActionParser {
    void parse(ParseThread parseThread, JSONObject action) throws JSONException;
}
