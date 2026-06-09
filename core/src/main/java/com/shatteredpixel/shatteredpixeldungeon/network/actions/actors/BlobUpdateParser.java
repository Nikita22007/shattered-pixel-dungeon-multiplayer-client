package com.shatteredpixel.shatteredpixeldungeon.network.actions.actors;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.CustomBlob;
import com.shatteredpixel.shatteredpixeldungeon.network.ParseThread;
import com.shatteredpixel.shatteredpixeldungeon.network.actions.ActionParser;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.utils.Log;
import org.json.JSONException;
import org.json.JSONObject;

import static com.shatteredpixel.shatteredpixeldungeon.network.actions.DefaultActionParserRegistry.payloadObject;

public class BlobUpdateParser implements ActionParser {
    @Override
    public void parse(ParseThread parseThread, JSONObject actorObj) throws JSONException {
        int id = actorObj.getInt("id");

        Actor actor = Actor.findById(id);
        if (!(actor instanceof CustomBlob)) {
            Actor.remove(actor);
            if (actor != null) {
                Log.e("BlobUpdateParser",  "Expected CustomBlob, but received: " + actor );
            }
            actor = new CustomBlob(id);
        }
        CustomBlob blob = (CustomBlob) actor;
        blob.update(actorObj);
        GameScene.add(blob);
        Dungeon.level.blobsList.add(blob);
    }
}
