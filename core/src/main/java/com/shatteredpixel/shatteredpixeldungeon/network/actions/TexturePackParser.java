package com.shatteredpixel.shatteredpixeldungeon.network.actions;

import com.nikita22007.pixeldungeonmultiplayer.JavaUtils;
import com.nikita22007.pixeldungeonmultiplayer.TextureManager;
import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.network.JsonStringHelper;
import com.shatteredpixel.shatteredpixeldungeon.network.ParseThread;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndError;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

class TexturePackParser implements ActionParser {
    public void parse(ParseThread parseThread, JSONObject action) throws JSONException {
        try {
            String data = action.has("texturepack") ? JsonStringHelper.getString(action, "texturepack") : JsonStringHelper.getString(action, "payload");
            TextureManager.INSTANCE.loadTexturePack(JavaUtils.InputStreamFromBase64(data));
        } catch (IOException err) {
            ShatteredPixelDungeon.scene().add(new WndError("Malformed texture pack"));
        }
    }
}
