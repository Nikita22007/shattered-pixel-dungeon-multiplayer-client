package com.shatteredpixel.shatteredpixeldungeon.network.actions.emitters;

import com.shatteredpixel.shatteredpixeldungeon.network.ParticleFactoryDeserializer;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.particles.Emitter;
import org.json.JSONException;
import org.json.JSONObject;

abstract class BaseEmitterParser {

	protected Emitter createEmitter(JSONObject action) throws JSONException {
		Emitter emitter = GameScene.emitter();
		if (emitter == null) {
			return null;
		}
		if (EmitterParser.configure(emitter, action)) {
			return emitter;
		}
		return null;
	}
}
