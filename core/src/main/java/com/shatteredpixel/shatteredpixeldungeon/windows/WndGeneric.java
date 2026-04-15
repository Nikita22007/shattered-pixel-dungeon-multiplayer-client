package com.shatteredpixel.shatteredpixeldungeon.windows;

import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.ui.RedButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.network.SendData;
import com.watabou.noosa.ui.Component;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class WndGeneric extends Window {
    private JSONArray componentsJson;
    private List<Component> generatedNodes = new ArrayList<>();
    private JSONObject rawData;

    public WndGeneric(int id, JSONObject data) {
        super();
        this.id = id;
        this.rawData = data;
        
        JSONObject args = data.optJSONObject("args");
        if (args == null) args = data.optJSONObject("params");
        
        if (args != null) {
            this.componentsJson = args.optJSONArray("components");
        } else {
            this.componentsJson = data.optJSONArray("components");
        }
        if (this.componentsJson == null) this.componentsJson = new JSONArray();

        for (int i = 0; i < componentsJson.length(); i++) {
            JSONObject cData = componentsJson.optJSONObject(i);
            if (cData == null) continue;
            
            String type = cData.optString("type", "component");
            Component node = null;
            
            if (type.equals("red_button")) {
                node = new RedButton(cData.optString("text")) {
                    @Override
                    protected void onClick() {
                        SendData.sendWindowResult(WndGeneric.this.id, cData.optInt("id"));
                    }
                };
            } else if (type.equals("text")) {
                int fontSize = cData.optInt("size", 6);
                node = new RenderedTextBlock(cData.optString("text"), fontSize);
                int color = cData.optInt("color", -1);
                if (color != -1) ((RenderedTextBlock) node).hardlight(color);
            }

            if (node != null) {
                add(node);
                generatedNodes.add(node);
            } else {
                generatedNodes.add(null);
            }
        }

        applyLayout();
    }

    @Override
    public void resize(int w, int h) {
        super.resize(w, h);
    }

    private void applyLayout() {
        if (componentsJson == null || rawData == null) return;
        boolean landscape = PixelScene.landscape();
        String mode = landscape ? "landscape" : "portrait";

        JSONObject args = rawData.optJSONObject("args");
        if (args == null) args = rawData.optJSONObject("params");
        JSONObject data = (args != null) ? args : rawData;

        JSONObject wCond = data.optJSONObject("w");
        JSONObject hCond = data.optJSONObject("h");
        if (wCond != null && hCond != null) {
            int ww = (int) wCond.optDouble(mode, 0);
            int hh = (int) hCond.optDouble(mode, 0);
            if (ww > 0 && hh > 0) {
                super.resize(ww, hh);
            }
        }
        
        for (int i = 0; i < componentsJson.length(); i++) {
            JSONObject cData = componentsJson.optJSONObject(i);
            Component node = generatedNodes.get(i);
            if (node == null || cData == null) continue;

            JSONObject xCond = cData.optJSONObject("x");
            JSONObject yCond = cData.optJSONObject("y");
            JSONObject wCompCond = cData.optJSONObject("w");
            JSONObject hCompCond = cData.optJSONObject("h");

            if (xCond != null && yCond != null && wCompCond != null && hCompCond != null) {
                float cx = (float) xCond.optDouble(mode, 0);
                float cy = (float) yCond.optDouble(mode, 0);
                float cw = (float) wCompCond.optDouble(mode, 0);
                float ch = (float) hCompCond.optDouble(mode, 0);
                if (node instanceof RenderedTextBlock) {
                    ((RenderedTextBlock) node).maxWidth((int) cw);
                }
                node.setRect(cx, cy, cw, ch);
            }
        }
    }
}
