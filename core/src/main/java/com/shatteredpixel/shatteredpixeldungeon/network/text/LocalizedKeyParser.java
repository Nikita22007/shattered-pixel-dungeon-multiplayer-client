package com.shatteredpixel.shatteredpixeldungeon.network.text;

import com.nikita22007.multiplayer.utils.text.LocalizedKey;

import org.json.JSONException;
import org.json.JSONObject;

public class LocalizedKeyParser {

    public LocalizedKey parse(JSONObject object) throws JSONException {
        String owner = object.has("owner") && !object.isNull("owner") ? object.getString("owner") : null;
        return new LocalizedKey(owner, object.getString("name"));
    }
}
