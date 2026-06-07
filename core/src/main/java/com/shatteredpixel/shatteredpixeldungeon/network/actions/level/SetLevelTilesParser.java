package com.shatteredpixel.shatteredpixeldungeon.network.actions.level;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.network.ParseThread;
import com.nikita22007.pixeldungeonmultiplayer.TranslationUtils;
import com.shatteredpixel.shatteredpixeldungeon.network.actions.ActionParser;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.shatteredpixel.shatteredpixeldungeon.network.ParseThread.isConnectedToOldServer;

public class SetLevelTilesParser implements ActionParser {
    @Override
    public void parse(ParseThread parseThread, JSONObject action) throws JSONException {
        JSONArray map = action.getJSONArray("tiles");
        if (isConnectedToOldServer()) {
            for (int i = 0; i < map.length(); i++) {
                Dungeon.level.map[i] = TranslationUtils.translateCell(map.getInt(i), i);
            }
        } else {
            for (int i = 0; i < map.length(); i++) {
                Dungeon.level.map[i] = map.getInt(i);
            }
        }
    }
}
