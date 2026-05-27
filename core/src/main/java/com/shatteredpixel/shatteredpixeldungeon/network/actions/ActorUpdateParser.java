package com.shatteredpixel.shatteredpixeldungeon.network.actions;

import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.network.JsonStringHelper;
import com.shatteredpixel.shatteredpixeldungeon.network.ParseThread;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import org.json.JSONException;
import org.json.JSONObject;

import static com.shatteredpixel.shatteredpixeldungeon.network.actions.DefaultActionParserRegistry.payloadObject;

public class ActorUpdateParser implements ActionParser {
    @Override
    public void parse(ParseThread parseThread, JSONObject action) throws JSONException {
        JSONObject actorObj = payloadObject(action);
        int ID = actorObj.getInt("id");
        boolean erase_old = actorObj.optBoolean("erase_old", false);

        if (!actorObj.has("type")) {
            GLog.n("Actor does not have type. Ignored");
            return;
        }

        Actor actor = (erase_old ? null : Actor.findById(ID));
        String type = JsonStringHelper.getString(actorObj, "type");
        switch (type) {
            case "char":
            case "character": {
                parseThread.parseActorChar(actorObj, ID, actor);
                break;
            }
            case "hero": {
                parseThread.parseActorHero(actorObj, ID, actor);
                break;
            }
            case "blob": {
                parseThread.parseActorBlob(actorObj, ID, actor);
                break;
            }
            default: {
                GLog.n("can't resolve actor type: \"" + type + "\". ID: " + ID);
            }
        }
    }
}
