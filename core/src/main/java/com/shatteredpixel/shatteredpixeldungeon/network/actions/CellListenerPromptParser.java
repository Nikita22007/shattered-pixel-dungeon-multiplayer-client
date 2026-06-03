package com.shatteredpixel.shatteredpixeldungeon.network.actions;

import com.shatteredpixel.shatteredpixeldungeon.network.ParseThread;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import org.json.JSONObject;

public class CellListenerPromptParser implements ActionParser {
    @Override
    public void parse(ParseThread parseThread, JSONObject action) {
        GameScene.defaultCellListener.setCustomPrompt(action.optString("prompt", null));
    }
}
