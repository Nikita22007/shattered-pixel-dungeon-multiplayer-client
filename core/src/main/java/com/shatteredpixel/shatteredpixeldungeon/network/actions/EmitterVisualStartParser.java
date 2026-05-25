package com.shatteredpixel.shatteredpixeldungeon.network.actions;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.network.ParseThread;
import com.shatteredpixel.shatteredpixeldungeon.network.ParticleFactoryDeserializer;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.tiles.DungeonTilemap;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.particles.Emitter;
import com.watabou.utils.PointF;

import org.json.JSONException;
import org.json.JSONObject;

public class EmitterVisualStartParser implements ActionParser {

    @Override
    public void parse(ParseThread parseThread, JSONObject action) throws JSONException {
        Char target = null;
        boolean fillTarget = true;
        PointF position = null;
        PointF shift = null;

        if (action.has("target_char")) {
            fillTarget = action.optBoolean("fill_target", true);
            int targetCharId = action.getInt("target_char");
            Actor targetActor = Actor.findById(targetCharId);
            if (targetActor instanceof Char) {
                target = (Char) targetActor;
                if (!target.sprite.visible) {
                    return;
                }
            } else {
                GLog.n("Incorrect EmitterVisualAction action: target is not char");
            }
        }

        if (action.has("pos")) {
            if (!Dungeon.level.heroFOV[action.getInt("pos")]) {
                return;
            }
            position = DungeonTilemap.tileToWorld(action.getInt("pos"));
        } else if (action.has("position_x")) {
            position = new PointF(
                    (float) action.getDouble("position_x"),
                    (float) action.getDouble("position_y")
            );
        }

        if (action.has("shift_x")) {
            shift = new PointF(
                    (float) action.getDouble("shift_x"),
                    (float) action.getDouble("shift_y")
            );
            if (position != null && ((shift.x != 0) || (shift.y != 0))) {
                position.x += shift.x;
                position.y += shift.y;
            }
        }

        Emitter.Factory factory = ParticleFactoryDeserializer.deserialize(action.getJSONObject("factory"));
        if (factory == null) {
            GLog.n("incorrect factory: " + action.getJSONObject("factory").optString("factory_type"));
            return;
        }

        Emitter emitter = GameScene.emitter();
        if (emitter == null) {
            return;
        }
        if ((target == null) && (position == null)) {
            GLog.n("Incorrect EmitterVisualAction action: no any target or position");
            return;
        }
        if ((target != null) && (shift != null) && ((shift.x != 0) || (shift.y != 0))) {
            position = new PointF(
                    target.sprite.x + shift.x,
                    target.sprite.y + shift.y
            );
            target = null;
        }
        if (target != null) {
            emitter.pos(target.sprite);
        } else {
            emitter.pos(position);
        }
        emitter.width = (float) action.getDouble("width");
        emitter.height = (float) action.getDouble("height");
        emitter.fillTarget = fillTarget;
        emitter.start(
                factory,
                (float) action.getDouble("interval"),
                action.getInt("quantity")
        );
        if (action.has("id")) {
            emitter.id = action.getInt("id");
            Emitter.infiniteEmitters.put(emitter.id, emitter);
        }
    }
}
