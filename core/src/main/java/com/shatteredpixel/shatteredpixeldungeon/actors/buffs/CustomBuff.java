package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Constructor;

import static com.nikita22007.pixeldungeonmultiplayer.TranslationUtils.translateBuffIcon;
import static com.shatteredpixel.shatteredpixeldungeon.network.ParseThread.isConnectedToOldServer;

public class CustomBuff extends Buff {
    private int icon = 0;

    private String desc = "unknown";

    public CustomBuff(JSONObject obj) throws JSONException {
        //TODO: check this
        // Where is buff ID?
        buff_id = obj.getInt("id");
        update(obj);
    }

    public void update(JSONObject obj) throws JSONException {
        setIcon(obj.optInt("icon", icon));
        setDesc(obj.optString("desc", desc));
    }

    public int icon() {
        return icon;
    }

    public void setIcon(int icon) {
        if (isConnectedToOldServer()){
         this.icon = translateBuffIcon(icon);
        } else {
            this.icon = icon;
        }
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public String toString()  {
        return desc;
    }

}