package com.shatteredpixel.shatteredpixeldungeon.network.actions;

import com.shatteredpixel.shatteredpixeldungeon.network.ParseThread;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.particles.Emitter;

import org.json.JSONException;
import org.json.JSONObject;

public class EmitterVisualStopParser implements ActionParser {

    @Override
    public void parse(ParseThread parseThread, JSONObject action) throws JSONException {
        int id = action.getInt("id");
        Emitter emitter = Emitter.infiniteEmitters.get(id);
        if (emitter != null) {
            emitter.killAndErase();
        } else {
            GLog.n("Failed to find emitter");
        }
    }
}
