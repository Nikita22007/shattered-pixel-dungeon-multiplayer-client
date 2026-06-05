package com.shatteredpixel.shatteredpixeldungeon.network.actions.actors;

import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.network.ParseThread;
import com.shatteredpixel.shatteredpixeldungeon.network.actions.ActionParser;
import org.json.JSONException;
import org.json.JSONObject;

import static com.shatteredpixel.shatteredpixeldungeon.network.actions.DefaultActionParserRegistry.payloadObject;

public class CharUpdateParser implements ActionParser {
    @Override
    public void parse(ParseThread parseThread, JSONObject action) throws JSONException {
        JSONObject actorObj = payloadObject(action);
        int id = actorObj.getInt("id");

        Actor actor = Actor.findById(id);
        parseThread.parseActorChar(actorObj, id, actor);
    }
}
