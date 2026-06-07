package com.shatteredpixel.shatteredpixeldungeon.network.actions.emitters;

import com.shatteredpixel.shatteredpixeldungeon.network.ParseThread;
import com.shatteredpixel.shatteredpixeldungeon.network.actions.ActionParser;
import org.json.JSONException;
import org.json.JSONObject;

public class EmitterBurstParser extends BaseEmitterParser implements ActionParser {

	@Override
	public void parse(ParseThread parseThread, JSONObject action) throws JSONException {
		createEmitter(action);
	}
}
