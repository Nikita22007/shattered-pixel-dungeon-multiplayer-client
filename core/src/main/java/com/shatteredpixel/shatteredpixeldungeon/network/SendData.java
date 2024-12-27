package com.shatteredpixel.shatteredpixeldungeon.network;

import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.items.CustomItem;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.util.List;

import static com.shatteredpixel.shatteredpixeldungeon.network.Client.flush;
import static com.shatteredpixel.shatteredpixeldungeon.network.Client.packet;


public class SendData {
    //---------------------------Hero
    public static void SendHeroClass(HeroClass heroClass) {
        Client.sendHeroClass(heroClass);
    }

    public static void SendCellListenerCell(Integer cell) {
        packet.packAndAddCellListenerCell(cell);
        flush();
    }

    public static void SendItemAction(CustomItem item, Hero hero, String action) {
        packet.packAndAddUsedAction(item, action, hero);
        flush();
    }

    public static void sendWindowResult(int id, int result) {
        if (-1 == id) {
            return; //internal window
        }
        packet.packAndAddWindowsResult(id, result, null);
        flush();
    }

    public static void sendBagWindowResult(int id, int result, List<Integer> path) {
        JSONArray arr = new JSONArray();
        for (int i = 0; i < path.size(); i += 1) {
            arr.put(path.get(i));
        }
        JSONObject resObj = new JSONObject();
        try {
            resObj.put("item_path", arr);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (-1 == id) {
            return; //internal window
        }
        packet.packAndAddWindowsResult(id, result, resObj);
        flush();
    }

    public static void sendToolbarAction(String action) {
        packet.packAndAddTollbarAction(action);
        flush();
    }

    public static void sendWaitAction() {
        sendToolbarAction("WAIT");
    }

    public static void sendRestAction() {
        sendToolbarAction("SLEEP");
    }

    public static void sendSearchAction() {
        sendToolbarAction("SEARCH");
    }

    public static void sendTalentUpgrade(Talent talent) {
        packet.packAndAddTalentUpgrade(talent);
        flush();
    }
}