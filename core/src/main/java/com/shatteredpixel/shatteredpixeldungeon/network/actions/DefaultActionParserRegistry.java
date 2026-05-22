package com.shatteredpixel.shatteredpixeldungeon.network.actions;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.effects.CheckedCell;
import com.shatteredpixel.shatteredpixeldungeon.effects.Enchanting;
import com.shatteredpixel.shatteredpixeldungeon.effects.Flare;
import com.shatteredpixel.shatteredpixeldungeon.effects.Surprise;
import com.shatteredpixel.shatteredpixeldungeon.items.CustomItem;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.network.ParseThread;
import com.shatteredpixel.shatteredpixeldungeon.network.Client;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.InterlevelScene;
import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.tiles.DungeonTilemap;
import com.shatteredpixel.shatteredpixeldungeon.tiles.FadingTraps;
import com.shatteredpixel.shatteredpixeldungeon.ui.BossHealthBar;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndError;
import com.nikita22007.pixeldungeonmultiplayer.JavaUtils;
import com.nikita22007.pixeldungeonmultiplayer.TextureManager;
import com.watabou.noosa.Game;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.Camera;
import com.watabou.noosa.audio.Music;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.PointF;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.level;

public class DefaultActionParserRegistry {

    public static ActionParserRegistry create() {
        ActionParserRegistry registry = new ActionParserRegistry();
        registry.register("sprite_action", new SpriteActionParser());
        registry.register("add_item_to_bag", new AddItemToBagParser());
        registry.register("show_status", new ShowStatusParser());
        registry.register("degradation", new DegradationParser());
        registry.register("show_banner", new BannerParser());
        registry.register("visual_show_banner", new BannerParser());
        registry.register("lightning_visual", new LightningVisualParser());
        registry.register("death_ray_centered_visual", new DeathRayCenteredVisualParser());
        registry.register("wound_visual", new WoundVisualParser());
        registry.register("ripple_visual", new RippleVisualParser());
        registry.register("missile_sprite_visual", new MissileSpriteVisualParser());
        registry.register("checked_cell_visual", new CheckedCellVisualParser());
        registry.register("play_sample", new PlaySampleParser());
        registry.register("music", new MusicParser());
        registry.register("load_sample", new LoadSampleParser());
        registry.register("unload_sample", new UnloadSampleParser());
        registry.register("shake_camera", new ShakeCameraParser());
        registry.register("enchanting_visual", new EnchantingVisualParser());
        registry.register("flare_visual", new FlareVisualParser());
        registry.register("emitter_visual", new EmitterVisualParser());
        registry.register("emitter_decor", new EmitterDecorParser());
        registry.register("heap_drop_visual", new HeapDropVisualParser());
        registry.register("magic_missile_visual", new MagicMissileVisualParser());
        registry.register("spell_sprite", new SpellSpriteParser());
        registry.register("discover_tile", new DiscoverTileParser());
        registry.register("surprise_visual", new SurpriseVisualParser());
        registry.register("boss_health_bar", new BossHealthBarParser());
        registry.register("game_scene_flash", new GameSceneFlashParser());
        registry.register("fading_traps", new FadingTrapsParser());
        registry.register("server_actions", new ServerActionsParser());
        registry.register("level_params", new LevelParamsParser());
        registry.register("map", new MapParser());
        registry.register("interlevel_scene", new InterlevelSceneParser());
        registry.register("actor_update", new ActorUpdateParser());
        registry.register("actor_delete", new ActorDeleteParser());
        registry.register("buffs", new BuffsParser());
        registry.register("hero", new HeroParser());
        registry.register("messages", new MessagesParser());
        registry.register("inventory", new InventoryParser());
        registry.register("heaps", new HeapsParser());
        registry.register("window", new WindowParser());
        registry.register("ui", new UiParser());
        registry.register("plants", new PlantsParser());
        registry.register("traps", new TrapsParser());
        registry.register("texturepack", new TexturePackParser());
        registry.register("redirect", new RedirectParser());
        return registry;
    }

    private static class SpriteActionParser implements ActionParser {
        public void parse(ParseThread parseThread, JSONObject action) throws JSONException {
            parseThread.parseSpriteAction(action);
        }
    }

    private static class AddItemToBagParser implements ActionParser {
        public void parse(ParseThread parseThread, JSONObject action) throws JSONException {
            parseThread.parse_update_bag_action(action);
        }
    }

    private static class ShowStatusParser implements ActionParser {
        public void parse(ParseThread parseThread, JSONObject action) throws JSONException {
            parseThread.parseShowStatusAction(action);
        }
    }

    private static class DegradationParser implements ActionParser {
        public void parse(ParseThread parseThread, JSONObject action) {
            parseThread.parseDegradationAction(action);
        }
    }

    private static class BannerParser implements ActionParser {
        public void parse(ParseThread parseThread, JSONObject action) {
            parseThread.parseBannerShowAction(action);
        }
    }

    private static class LightningVisualParser implements ActionParser {
        public void parse(ParseThread parseThread, JSONObject action) {
            parseThread.parseLightningVisualAction(action);
        }
    }

    private static class DeathRayCenteredVisualParser implements ActionParser {
        public void parse(ParseThread parseThread, JSONObject action) {
            parseThread.parseDeathRayCenteredVisualAction(action);
        }
    }

    private static class WoundVisualParser implements ActionParser {
        public void parse(ParseThread parseThread, JSONObject action) {
            parseThread.parseWoundVisualAction(action);
        }
    }

    private static class RippleVisualParser implements ActionParser {
        public void parse(ParseThread parseThread, JSONObject action) {
            parseThread.parseRippleVisualAction(action);
        }
    }

    private static class MissileSpriteVisualParser implements ActionParser {
        public void parse(ParseThread parseThread, JSONObject action) {
            parseThread.parseMissileSpriteVisualAction(action);
        }
    }

    private static class CheckedCellVisualParser implements ActionParser {
        public void parse(ParseThread parseThread, JSONObject action) throws JSONException {
            if (Dungeon.level.heroFOV[action.getInt("pos")]) {
                GameScene.effect(new CheckedCell(action.getInt("pos")));
            }
        }
    }

    private static class PlaySampleParser implements ActionParser {
        public void parse(ParseThread parseThread, JSONObject action) {
            Sample.INSTANCE.play(action);
        }
    }

    private static class MusicParser implements ActionParser {
        public void parse(ParseThread parseThread, JSONObject action) {
            Music.INSTANCE.parseAction(action).execute();
        }
    }

    private static class LoadSampleParser implements ActionParser {
        public void parse(ParseThread parseThread, JSONObject action) throws JSONException {
            Sample.INSTANCE.load(action.getJSONArray("samples"));
        }
    }

    private static class UnloadSampleParser implements ActionParser {
        public void parse(ParseThread parseThread, JSONObject action) throws JSONException {
            Sample.INSTANCE.unload(action.getString("sample"));
        }
    }

    private static class ShakeCameraParser implements ActionParser {
        public void parse(ParseThread parseThread, JSONObject action) throws JSONException {
            Camera.main.shake((float) action.getDouble("magnitude"), (float) action.getDouble("duration"));
        }
    }

    private static class EnchantingVisualParser implements ActionParser {
        public void parse(ParseThread parseThread, JSONObject action) throws JSONException {
            int targetCharId = action.getInt("target");
            Actor actor = Actor.findById(targetCharId);
            if (!(actor instanceof Char)) {
                GLog.n("Enchanting: Can't find char with id " + targetCharId + ". Ignored");
                return;
            }
            Item item = CustomItem.createItem(action.getJSONObject("item"));
            Enchanting.show((Char) actor, item);
        }
    }

    private static class FlareVisualParser implements ActionParser {
        public void parse(ParseThread parseThread, JSONObject action) throws JSONException {
            PointF position;
            if (action.has("pos")) {
                position = DungeonTilemap.tileCenterToWorld(action.getInt("pos"));
            } else {
                position = new PointF(
                        (float) action.getDouble("position_x"),
                        (float) action.getDouble("position_y")
                );
            }

            Flare flare = new Flare(action.getInt("rays"), (float) action.getDouble("radius"));
            flare.angle = (float) action.optDouble("angle", 45);
            flare.angularSpeed = (float) action.optDouble("angular_speed", 180);
            flare.color(action.getInt("color"), action.optBoolean("light_moode", true));
            GameScene.showFlare(flare, position, (float) action.getDouble("duration"));
        }
    }

    private static class EmitterVisualParser implements ActionParser {
        public void parse(ParseThread parseThread, JSONObject action) {
            parseThread.parseEmitterVisualAction(action);
        }
    }

    private static class EmitterDecorParser implements ActionParser {
        public void parse(ParseThread parseThread, JSONObject action) {
            level.addVisual(action);
        }
    }

    private static class HeapDropVisualParser implements ActionParser {
        public void parse(ParseThread parseThread, JSONObject action) throws JSONException {
            parseThread.parseHeadDropVisualAction(action);
        }
    }

    private static class MagicMissileVisualParser implements ActionParser {
        public void parse(ParseThread parseThread, JSONObject action) throws JSONException {
            parseThread.parseMagicMissileVisual(action);
        }
    }

    private static class SpellSpriteParser implements ActionParser {
        public void parse(ParseThread parseThread, JSONObject action) throws JSONException {
            parseThread.ShowSpellSprite(action);
        }
    }

    private static class DiscoverTileParser implements ActionParser {
        public void parse(ParseThread parseThread, JSONObject action) throws JSONException {
            GameScene.discoverTile(action.getInt("pos"), action.getInt("old_tile"));
        }
    }

    private static class SurpriseVisualParser implements ActionParser {
        public void parse(ParseThread parseThread, JSONObject action) throws JSONException {
            Surprise.hit(action.getInt("pos"), action.getInt("angle"));
        }
    }

    private static class BossHealthBarParser implements ActionParser {
        public void parse(ParseThread parseThread, JSONObject action) {
            BossHealthBar.parseAction(action);
        }
    }

    private static class GameSceneFlashParser implements ActionParser {
        public void parse(ParseThread parseThread, JSONObject action) throws JSONException {
            GameScene.flash(action.getInt("color"), action.getBoolean("light"));
        }
    }

    private static class FadingTrapsParser implements ActionParser {
        public void parse(ParseThread parseThread, JSONObject action) {
            FadingTraps.fromJSON(action);
        }
    }

    private static JSONObject payloadObject(JSONObject action) throws JSONException {
        return action.has("payload") ? action.getJSONObject("payload") : action;
    }

    private static JSONArray payloadArray(JSONObject action) throws JSONException {
        return action.getJSONArray("payload");
    }

    private static class ServerActionsParser implements ActionParser {
        public void parse(ParseThread parseThread, JSONObject action) throws JSONException {
            parseThread.parseServerActions(payloadArray(action));
        }
    }

    private static class LevelParamsParser implements ActionParser {
        public void parse(ParseThread parseThread, JSONObject action) throws JSONException {
            parseThread.parseLevelParams(payloadObject(action));
        }
    }

    private static class MapParser implements ActionParser {
        public void parse(ParseThread parseThread, JSONObject action) throws JSONException {
            parseThread.parseLevel(payloadObject(action));
        }
    }

    private static class InterlevelSceneParser implements ActionParser {
        public void parse(ParseThread parseThread, JSONObject action) throws JSONException {
            JSONObject ilsObj = payloadObject(action);
            if (ilsObj.has("state")) {
                InterlevelScene.phase = InterlevelScene.Phase.valueOf(ilsObj.getString("state").toUpperCase());
            }
            if (ilsObj.has("type")) {
                String modeName = ilsObj.getString("type").toUpperCase();
                if (modeName.equals("CUSTOM")) {
                    modeName = "NONE";
                }
                InterlevelScene.mode = InterlevelScene.Mode.valueOf(modeName);
            }
            InterlevelScene.reset_level = ilsObj.optBoolean("reset_level");
            if (ilsObj.has("message")) {
                InterlevelScene.customMessage = ilsObj.getString("message");
            }
            if (!(Game.scene() instanceof InterlevelScene)) {
                if (!((Game.scene() instanceof GameScene) && (InterlevelScene.phase == InterlevelScene.Phase.FADE_OUT))) {
                    Game.switchScene(InterlevelScene.class);
                }
            }
        }
    }

    private static class ActorUpdateParser implements ActionParser {
        public void parse(ParseThread parseThread, JSONObject action) throws JSONException {
            parseThread.parseActorUpdate(payloadObject(action));
        }
    }

    private static class ActorDeleteParser implements ActionParser {
        public void parse(ParseThread parseThread, JSONObject action) throws JSONException {
            parseThread.parseActorDelete(payloadObject(action));
        }
    }

    private static class BuffsParser implements ActionParser {
        public void parse(ParseThread parseThread, JSONObject action) throws JSONException {
            parseThread.parseBuffs(payloadArray(action));
        }
    }

    private static class HeroParser implements ActionParser {
        public void parse(ParseThread parseThread, JSONObject action) throws JSONException {
            parseThread.parseHero(payloadObject(action));
        }
    }

    private static class MessagesParser implements ActionParser {
        public void parse(ParseThread parseThread, JSONObject action) throws JSONException {
            parseThread.parseMessages(payloadArray(action));
        }
    }

    private static class InventoryParser implements ActionParser {
        public void parse(ParseThread parseThread, JSONObject action) throws JSONException {
            parseThread.parseInventory(payloadObject(action));
        }
    }

    private static class HeapsParser implements ActionParser {
        public void parse(ParseThread parseThread, JSONObject action) throws JSONException {
            JSONArray heaps = payloadArray(action);
            for (int i = 0; i < heaps.length(); i++) {
                parseThread.parseHeap(heaps.getJSONObject(i));
            }
        }
    }

    private static class WindowParser implements ActionParser {
        public void parse(ParseThread parseThread, JSONObject action) throws JSONException {
            parseThread.parseWindow(payloadObject(action));
        }
    }

    private static class UiParser implements ActionParser {
        public void parse(ParseThread parseThread, JSONObject action) throws JSONException {
            parseThread.parseUI(payloadObject(action));
        }
    }

    private static class PlantsParser implements ActionParser {
        public void parse(ParseThread parseThread, JSONObject action) throws JSONException {
            parseThread.parsePlants(payloadArray(action));
        }
    }

    private static class TrapsParser implements ActionParser {
        public void parse(ParseThread parseThread, JSONObject action) throws JSONException {
            parseThread.parseTraps(payloadArray(action));
        }
    }

    private static class TexturePackParser implements ActionParser {
        public void parse(ParseThread parseThread, JSONObject action) throws JSONException {
            try {
                TextureManager.INSTANCE.loadTexturePack(JavaUtils.InputStreamFromBase64(action.getString("payload")));
            } catch (IOException err) {
                ShatteredPixelDungeon.scene().add(new WndError("Malformed texture pack"));
            }
        }
    }

    private static class RedirectParser implements ActionParser {
        public void parse(ParseThread parseThread, JSONObject action) throws JSONException {
            JSONObject redirectObject = payloadObject(action);
            Client.disconnectWithoutSwitch();
            ShatteredPixelDungeon.switchScene(InterlevelScene.class);
            if (redirectObject.has("uuid")){
                com.shatteredpixel.shatteredpixeldungeon.network.NetworkPacket.redirectUUID = redirectObject.getString("uuid");
            }
            if(redirectObject.has("password")){
                com.shatteredpixel.shatteredpixeldungeon.network.NetworkPacket.password = redirectObject.getString("password");
            }
            Client.connect(redirectObject.getString("host"), redirectObject.getInt("port"));
        }
    }
}
