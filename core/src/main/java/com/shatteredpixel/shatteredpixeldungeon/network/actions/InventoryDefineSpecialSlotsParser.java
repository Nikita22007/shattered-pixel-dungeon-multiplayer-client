package com.shatteredpixel.shatteredpixeldungeon.network.actions;

import com.shatteredpixel.shatteredpixeldungeon.network.ParseThread;
import org.json.JSONException;
import org.json.JSONObject;

import static com.shatteredpixel.shatteredpixeldungeon.network.actions.DefaultActionParserRegistry.payloadArray;

public class InventoryDefineSpecialSlotsParser implements ActionParser {
    @Override
    public void parse(ParseThread parseThread, JSONObject action) throws JSONException {
        parseThread.parseInventoryDefineSpecialSlots(action.getJSONArray("slots"));
    }
}
