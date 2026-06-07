package com.shatteredpixel.shatteredpixeldungeon.network.actions.items;

import com.shatteredpixel.shatteredpixeldungeon.network.ParseThread;
import com.shatteredpixel.shatteredpixeldungeon.network.actions.ActionParser;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseItemActionParser implements ActionParser {
    protected abstract String getMode();

    @Override
    public void parse(ParseThread parseThread, JSONObject action) throws JSONException {
        JSONArray pathArr = action.getJSONArray("path");
        List<Integer> path = new ArrayList<>(pathArr.length());
        for (int i = 0; i < pathArr.length(); i++) {
            path.add(pathArr.getInt(i));
        }
        
        JSONObject itemObj = action.optJSONObject("item");
        parseThread.parseItemAction(path, itemObj, getMode());
    }
}
