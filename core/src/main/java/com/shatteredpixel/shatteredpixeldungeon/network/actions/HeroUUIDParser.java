package com.shatteredpixel.shatteredpixeldungeon.network.actions;

import com.badlogic.gdx.Gdx;
import com.shatteredpixel.shatteredpixeldungeon.SPDSettings;
import com.shatteredpixel.shatteredpixeldungeon.network.JsonStringHelper;
import com.shatteredpixel.shatteredpixeldungeon.network.ParseThread;
import org.json.JSONException;
import org.json.JSONObject;

public class HeroUUIDParser implements ActionParser {

    @Override
    public void parse(ParseThread parseThread, JSONObject action) throws JSONException {
        JSONObject payload = DefaultActionParserRegistry.payloadObject(action);
        
        String serverUUID = ParseThread.serverUUID;
        if (payload.has("uuid")) {
            String uuid = JsonStringHelper.getString(payload, "uuid");
            SPDSettings.heroUUID(serverUUID, uuid);
            Gdx.app.log("ParseThread", "heroUUID: " + uuid);
        }
    }
}
