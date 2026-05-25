package com.shatteredpixel.shatteredpixeldungeon.network.actions;

import com.nikita22007.multiplayer.utils.Log;
import com.nikita22007.multiplayer.utils.text.LocalizedString;
import com.shatteredpixel.shatteredpixeldungeon.Game;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.network.ParseThread;
import com.shatteredpixel.shatteredpixeldungeon.network.text.LocalizedStringParser;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.ui.GameLog;
import com.watabou.noosa.Scene;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MessagesParser implements ActionParser {

    private final LocalizedStringParser textParser = new LocalizedStringParser();

    @Override
    public void parse(ParseThread parseThread, JSONObject action) throws JSONException {
        JSONArray messages = action.has("messages")
                ? action.getJSONArray("messages")
                : DefaultActionParserRegistry.payloadArray(action);

        for (int i = 0; i < messages.length(); i++) {
            parseMessage(messages.getJSONObject(i));
        }
    }

    private void parseMessage(JSONObject messageObj) {
        Scene scene = Game.scene();
        if (!(scene instanceof GameScene)) {
            return;
        }

        GameLog log = ((GameScene) scene).getGameLog();
        try {
            LocalizedString text = textParser.parse(messageObj.get("text"));
            String resolved = Messages.resolve(text);
            if (messageObj.has("color")) {
                log.WriteMessage(resolved, messageObj.getInt("color"));
            } else {
                log.WriteMessageAutoColor(resolved);
            }
        } catch (JSONException e) {
            Log.w("MessagesParser", "Incorrect message");
        }
    }
}
