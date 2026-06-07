package com.shatteredpixel.shatteredpixeldungeon.network.actions;

import com.shatteredpixel.shatteredpixeldungeon.network.ParseThread;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.watabou.noosa.Game;
import com.watabou.noosa.Scene;
import org.json.JSONException;
import org.json.JSONObject;

public class UpdateCounterParser implements ActionParser {
    @Override
    public void parse(ParseThread parseThread, JSONObject action) throws JSONException {
        Scene scene = Game.scene();
        if (scene instanceof GameScene) {
            ((GameScene) scene).setCounter((float) action.getDouble("counter"));
        }
    }
}
