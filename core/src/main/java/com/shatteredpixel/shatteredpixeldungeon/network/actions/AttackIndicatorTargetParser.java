package com.shatteredpixel.shatteredpixeldungeon.network.actions;

import com.shatteredpixel.shatteredpixeldungeon.network.ParseThread;
import com.shatteredpixel.shatteredpixeldungeon.ui.AttackIndicator;
import org.json.JSONException;
import org.json.JSONObject;

public class AttackIndicatorTargetParser implements ActionParser {
    @Override
    public void parse(ParseThread parseThread, JSONObject action) throws JSONException {
        AttackIndicator.setCandidates(action.getInt("target"));
    }
}
