package com.shatteredpixel.shatteredpixeldungeon.network.actions.items.inventory;

import com.shatteredpixel.shatteredpixeldungeon.network.ParseThread;
import com.shatteredpixel.shatteredpixeldungeon.network.actions.ActionParser;
import org.json.JSONException;
import org.json.JSONObject;

public class InventoryRebuildParser implements ActionParser {
    @Override
    public void parse(ParseThread parseThread, JSONObject action) throws JSONException {
        parseThread.parseInventory(action);
    }
}
