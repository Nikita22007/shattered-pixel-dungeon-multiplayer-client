package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;

import com.nikita22007.pixeldungeonmultiplayer.Text;
import com.watabou.noosa.Image;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Constructor;

import static com.nikita22007.pixeldungeonmultiplayer.TranslationUtils.translateBuffIcon;
import static com.shatteredpixel.shatteredpixeldungeon.network.ParseThread.isConnectedToOldServer;

public class CustomBuff extends Buff {
    private int icon = 0;

    private Text desc = new Text("unknown");
    float rm = 0;
    float gm = 0;
    float bm = 0;
    private Text name = new Text("unknown");
    private float iconFadePercent;

    public CustomBuff(JSONObject obj) throws JSONException {
        //TODO: check this
        // Where is buff ID?
        buff_id = obj.getInt("id");
        update(obj);
    }

    public void update(JSONObject obj) throws JSONException {
        setIcon(obj.optInt("icon", icon));
        setDesc(Text.of(obj.opt("desc")));
        if (obj.has("hardlight")){
            JSONObject hardlight = obj.getJSONObject("hardlight");
            rm = (float) hardlight.getDouble("rm");
            gm = (float) hardlight.getDouble("gm");
            bm = (float) hardlight.getDouble("bm");
        }
        if (obj.has("name")) {
            this.name = Text.of(obj.get("name"));
        }
        if (obj.has("fade_percent")) {
            iconFadePercent = Float.parseFloat(obj.getString("fade_percent"));
        } else {
            iconFadePercent = 0f; //default to no fade
        }
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

    @Override
    public void tintIcon(Image icon) {
        icon.hardlight(rm, gm , bm);
    }

    public void setDesc(Text desc) {
        this.desc = desc;
    }

    @Override
    public String name(){
        return name.asString();
    }

    @Override
    public String desc() {
        return desc.asString();
    }

    @Override
    public float iconFadePercent() {
        return iconFadePercent;
    }
}