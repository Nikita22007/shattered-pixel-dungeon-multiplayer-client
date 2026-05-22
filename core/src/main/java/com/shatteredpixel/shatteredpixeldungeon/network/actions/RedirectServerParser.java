package com.shatteredpixel.shatteredpixeldungeon.network.actions;

import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.network.Client;
import com.shatteredpixel.shatteredpixeldungeon.network.ParseThread;
import com.shatteredpixel.shatteredpixeldungeon.scenes.InterlevelScene;
import org.json.JSONException;
import org.json.JSONObject;

public class RedirectServerParser implements ActionParser {
    @Override
    public void parse(ParseThread parseThread, JSONObject action) throws JSONException {
        Client.disconnectWithoutSwitch();
        ShatteredPixelDungeon.switchScene(InterlevelScene.class);
        
        if (action.has("uuid")){
            com.shatteredpixel.shatteredpixeldungeon.network.NetworkPacket.redirectUUID = action.getString("uuid");
        }
        if(action.has("password")){
            com.shatteredpixel.shatteredpixeldungeon.network.NetworkPacket.password = action.getString("password");
        }
        Client.connect(action.getString("host"), action.getInt("port"));
    }
}
