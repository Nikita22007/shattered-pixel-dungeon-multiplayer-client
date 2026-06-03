package com.shatteredpixel.shatteredpixeldungeon.network.actions;

import com.shatteredpixel.shatteredpixeldungeon.network.ParseThread;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import org.json.JSONException;
import org.json.JSONObject;

public class KeysIndicatorParser implements ActionParser {
    @Override
    public void parse(ParseThread parseThread, JSONObject action) throws JSONException {
        GameScene.updateKeyDisplay(action.getJSONArray("keys_count"));
    }
}
