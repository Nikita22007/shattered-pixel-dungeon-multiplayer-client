package com.shatteredpixel.shatteredpixeldungeon.network.actions;

import com.shatteredpixel.shatteredpixeldungeon.network.Client;
import com.shatteredpixel.shatteredpixeldungeon.network.ParseThread;
import org.json.JSONObject;

public class ConnectionRejectedParser implements ActionParser {
    @Override
    public void parse(ParseThread parseThread, JSONObject action) {
        Client.disconnectWithoutSwitch();
        ParseThread.returnToMainScreen(action.optString("message", "Connection rejected"));
    }
}
