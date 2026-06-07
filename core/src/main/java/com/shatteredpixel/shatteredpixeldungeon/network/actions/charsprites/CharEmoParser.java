package com.shatteredpixel.shatteredpixeldungeon.network.actions.charsprites;

import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.network.JsonStringHelper;
import com.shatteredpixel.shatteredpixeldungeon.network.ParseThread;
import com.shatteredpixel.shatteredpixeldungeon.network.actions.ActionParser;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import org.json.JSONException;
import org.json.JSONObject;

public class CharEmoParser implements ActionParser {
    @Override
    public void parse(ParseThread parseThread, JSONObject action) throws JSONException {
        Actor actor = Actor.findById(action.getInt("actor_id"));
        if (!(actor instanceof Char)) {
            return;
        }

        CharSprite sprite = ((Char) actor).sprite;
        if (sprite == null) {
            return;
        }

        if (!action.has("emotion") || action.isNull("emotion")) {
            sprite.hideEmo();
            return;
        }

        String emotion = JsonStringHelper.getString(action, "emotion");
        switch (emotion) {
            case "alert":
                sprite.showAlert();
                break;
            case "sleep":
                sprite.showSleep();
                break;
            case "lost":
                sprite.showLost();
                break;
            case "investigate":
                sprite.showInvestigate();
                break;
            default:
                GLog.h("Unknown emo: " + emotion + ". ID: " + ((Char) actor).id());
                sprite.showAlert();
                break;
        }
    }
}
