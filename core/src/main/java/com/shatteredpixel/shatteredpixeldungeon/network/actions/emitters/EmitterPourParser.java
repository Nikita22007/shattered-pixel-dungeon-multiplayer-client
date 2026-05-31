package com.shatteredpixel.shatteredpixeldungeon.network.actions.emitters;

import com.shatteredpixel.shatteredpixeldungeon.network.ParseThread;
import com.shatteredpixel.shatteredpixeldungeon.network.actions.ActionParser;
import com.watabou.noosa.particles.Emitter;
import org.json.JSONException;
import org.json.JSONObject;

public class EmitterPourParser extends BaseEmitterParser implements ActionParser {

	@Override
	public void parse(ParseThread parseThread, JSONObject action) throws JSONException {
		int id = action.getInt("id");
		Emitter old = Emitter.infiniteEmitters.remove(id);
		if (old != null) {
			old.on = false;
			old.killAndErase();
		}

		Emitter emitter = createEmitter(action);
		if (emitter != null) {
			emitter.id = id;
			Emitter.infiniteEmitters.put(id, emitter);
		}
	}
}
