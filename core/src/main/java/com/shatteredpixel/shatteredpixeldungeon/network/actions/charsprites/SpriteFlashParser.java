package com.shatteredpixel.shatteredpixeldungeon.network.actions.charsprites;

import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.network.ParseThread;
import com.shatteredpixel.shatteredpixeldungeon.network.actions.ActionParser;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import org.json.JSONException;
import org.json.JSONObject;

public class SpriteFlashParser implements ActionParser {

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

        if (action.has("flash_time")) {
            sprite.flash((float) action.getDouble("flash_time"));
        } else {
            sprite.flash();
        }
    }
}
