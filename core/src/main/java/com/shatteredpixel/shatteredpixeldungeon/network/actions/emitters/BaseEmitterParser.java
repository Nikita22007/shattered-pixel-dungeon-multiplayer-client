package com.shatteredpixel.shatteredpixeldungeon.network.actions.emitters;

import com.shatteredpixel.shatteredpixeldungeon.network.ParticleFactoryDeserializer;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.particles.Emitter;
import org.json.JSONException;
import org.json.JSONObject;

abstract class BaseEmitterParser {

	protected Emitter createEmitter(JSONObject action) throws JSONException {
		Emitter.Factory factory = ParticleFactoryDeserializer.deserialize(action.getJSONObject("factory"));
		if (factory == null) {
			GLog.n("incorrect factory: " + action.getJSONObject("factory").optString("factory_type"));
			return null;
		}

		Emitter emitter = GameScene.emitter();
		if (emitter == null) {
			return null;
		}
		if (!EmitterAnchorParser.apply(emitter, action.getJSONObject("anchor"))) {
			GLog.n("Incorrect emitter action: invalid anchor");
			return null;
		}

		emitter.start(
				factory,
				(float) action.getDouble("interval"),
				action.getInt("quantity")
		);
		return emitter;
	}
}
