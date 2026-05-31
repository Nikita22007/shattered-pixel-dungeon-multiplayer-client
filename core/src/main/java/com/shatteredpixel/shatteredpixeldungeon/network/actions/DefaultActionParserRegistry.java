package com.shatteredpixel.shatteredpixeldungeon.network.actions;

import com.shatteredpixel.shatteredpixeldungeon.effects.Surprise;
import com.shatteredpixel.shatteredpixeldungeon.network.JsonStringHelper;
import com.shatteredpixel.shatteredpixeldungeon.network.ParseThread;
import com.shatteredpixel.shatteredpixeldungeon.network.actions.emitters.EmitterBurstParser;
import com.shatteredpixel.shatteredpixeldungeon.network.actions.emitters.EmitterPourParser;
import com.shatteredpixel.shatteredpixeldungeon.network.actions.emitters.EmitterStartParser;
import com.shatteredpixel.shatteredpixeldungeon.network.actions.emitters.EmitterStopParser;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.tiles.FadingTraps;
import com.shatteredpixel.shatteredpixeldungeon.ui.BossHealthBar;
import com.watabou.noosa.Camera;
import com.watabou.noosa.audio.Music;
import com.watabou.noosa.audio.Sample;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
        registry.register("emitter_burst", new EmitterBurstParser());
        registry.register("emitter_start", new EmitterStartParser());
        registry.register("emitter_pour", new EmitterPourParser());
        registry.register("emitter_stop", new EmitterStopParser());
        registry.register("emitter_decor", new EmitterDecorParser());
        registry.register("heap_drop_visual", new HeapDropVisualParser());
        registry.register("magic_missile_visual", new MagicMissileVisualParser());
        registry.register("spell_sprite", new SpellSpriteParser());
        registry.register("discover_tile", new DiscoverTileParser());
        registry.register("surprise_visual", new SurpriseVisualParser());
        registry.register("boss_health_bar", new BossHealthBarParser());
        registry.register("game_scene_flash", new GameSceneFlashParser());
        registry.register("fading_traps", new FadingTrapsParser());
        registry.register("reset_level", new ResetLevelParser());
        registry.register("show_banner", new ShowBannerParser());
        registry.register("redirect_server", new RedirectServerParser());
        registry.register("resize_level", new ResizeLevelParser());
        registry.register("set_level_visuals", new SetLevelVisualsParser());
        registry.register("set_level_entrance", new SetLevelEntranceParser());
        registry.register("set_level_exit", new SetLevelExitParser());
        registry.register("set_level_tiles", new SetLevelTilesParser());
        registry.register("set_level_states", new SetLevelStatesParser());
        registry.register("update_fov", new UpdateFovParser());
        registry.register("update_cells", new UpdateCellsParser());
        registry.register("interlevel_scene", new InterlevelSceneParser());
        registry.register("actor_update", new ActorUpdateParser());
        registry.register("actor_delete", new ActorDeleteParser());
        registry.register("buff_update", new BuffUpdateParser());
        registry.register("buff_remove", new BuffRemoveParser());
        registry.register("hero", new HeroParser());
        registry.register("messages", new MessagesParser());
        registry.register("inventory_rebuild", new InventoryRebuildParser());
        registry.register("inventory_define_special_slots", new InventoryDefineSpecialSlotsParser());
        registry.register("item_add", new ItemAddParser());
        registry.register("item_remove", new ItemRemoveParser());
        registry.register("item_update", new ItemUpdateParser());
        registry.register("item_replace", new ItemReplaceParser());
        registry.register("heap_update", new HeapUpdateParser());
        registry.register("heap_remove", new HeapRemoveParser());
        registry.register("show_window", new ShowWindowParser());
        registry.register("update_depth", new UpdateDepthParser());
        registry.register("update_counter", new UpdateCounterParser());
        registry.register("update_keys", new UpdateKeysParser());
        registry.register("unlock_badge", new UnlockBadgeParser());
        registry.register("show_message", new ShowMessageParser());
        registry.register("ui", new UiParser()); // Legacy
        registry.register("window", new WindowParser()); // Legacy
        registry.register("plant_update", new PlantUpdateParser());
        registry.register("plant_remove", new PlantRemoveParser());
        registry.register("trap_update", new TrapUpdateParser());
        registry.register("trap_remove", new TrapRemoveParser());
        registry.register("texturepack", new TexturePackParser());
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
            Sample.INSTANCE.unload(JsonStringHelper.getString(action, "sample"));
        }
    }

    private static class ShakeCameraParser implements ActionParser {
        public void parse(ParseThread parseThread, JSONObject action) throws JSONException {
            Camera.main.shake((float) action.getDouble("magnitude"), (float) action.getDouble("duration"));
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

    static JSONObject payloadObject(JSONObject action) throws JSONException {
        return action.has("payload") ? action.getJSONObject("payload") : action;
    }

    public static JSONArray payloadArray(JSONObject action) throws JSONException {
        return action.getJSONArray("payload");
    }

    private static class HeroParser implements ActionParser {
        public void parse(ParseThread parseThread, JSONObject action) throws JSONException {
            parseThread.parseHero(payloadObject(action));
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

}
