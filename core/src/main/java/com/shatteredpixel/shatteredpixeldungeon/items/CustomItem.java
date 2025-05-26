package com.shatteredpixel.shatteredpixeldungeon.items;

import java.util.ArrayList;

import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.CustomBag;
import com.shatteredpixel.shatteredpixeldungeon.network.SendData;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;

import com.watabou.noosa.ColorBlock;
import com.watabou.noosa.Visual;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Iterator;

import static com.nikita22007.pixeldungeonmultiplayer.TranslationUtils.translateItemImage;
import static com.shatteredpixel.shatteredpixeldungeon.network.ParseThread.isConnectedToOldServer;

public class CustomItem extends Item {
    protected String name;
    protected String spriteSheet;
    protected String descString;

    protected ArrayList<String> actionsList = new ArrayList<>();

    protected boolean identified = false;
    protected int maxDurability = 1;
    protected ItemSprite.Glowing glowing;

    public boolean showBar = false;
    public UI ui = new UI();

    public static CustomItem createItem(JSONObject obj) {
        if (obj.has("size")) {
            return new CustomBag(obj);
        }
        return new CustomItem(obj);
    }

    protected CustomItem() {
        super();
    }

    @Override
    public boolean isEquipped( @NotNull Hero hero ) {
        return hero.belongings.isEquipped(this);
    }

    protected CustomItem(JSONObject obj) {
        this();
        cursedKnown = true; // todo check it
        update(obj);
    }
    int energyVal;
    public void update(JSONObject obj) {
        Iterator<String> it = obj.keys();
        while (it.hasNext()) {
            String token = it.next();
            switch (token) {
                case "name":
                    name = obj.getString(token);
                    break;
                case "info":
                    descString = obj.getString(token);
                    break;
                case "image":
                    if (isConnectedToOldServer()) {
                        image = translateItemImage(obj.getInt(token));
                    } else {
                        image = obj.getInt(token);
                    }
                    break;
                case "icon":
                    if (!isConnectedToOldServer()) {
                        icon = obj.getInt(token);
                    }
                    break;
                case "stackable":
                    stackable = obj.getBoolean(token);
                    break;
                case "quantity":
                case "count":
                    quantity = obj.getInt(token);
                    break;
                case "level":
                    level(obj.getInt(token));
                    break;
                case "level_known":
                    levelKnown = obj.getBoolean(token);
                    break;
                case "cursed":
                    cursed = obj.getBoolean(token);
                    break;
                case "identified":
                    identified = obj.getBoolean(token);
                    break;
                case "actions":
                    parseActions(obj.getJSONArray(token));
                    break;
                case "default_action":
                    String action = obj.getString(token);
                    defaultAction = action.equals("null") ? null : action;
                    break;
                case "ui":
                    JSONObject uiObj = obj.getJSONObject(token);
                    ui = new UI(uiObj);
                    break;
                case "show_bar":
                    showBar = obj.getBoolean(token);
                    break;
                case "glowing":
                    if (obj.isNull(token)) {
                        glowing = null;
                    } else {
                        JSONObject glowingObj = obj.getJSONObject(token);
                        glowing = new ItemSprite.Glowing(glowingObj);
                    }
                    break;
                case "sprite_sheet":
                    this.spriteSheet = obj.getString(token);
                    break;
                case "energy_value": {
                    this.energyVal = obj.getInt(token);
                    break;
                }
            }
        }
    }

    @Override
    public int energyVal() {
        return energyVal;
    }

    private void parseActions(JSONArray actionsArr) {
        ArrayList<String> actions = new ArrayList<>(actionsArr.length());
        for (int i = 0; i < actionsArr.length(); i++) {
            String action = actionsArr.getString(i);
            actions.add(action);
        }
        actionsList = actions;
    }

    @Override
    public String desc() {
        return descString != null ? descString : "idk,wtf";
    }

    public UI getUi() {
        return ui;
    }
    @Override
    public ItemSprite.Glowing glowing() {
        return glowing;
    }

    @Override
    public ArrayList<String> actions(Hero hero) {
        return new ArrayList<>(actionsList);
    }
    public String actionName(String action, Hero hero){
        return action;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public void execute(Hero hero, String action) {
        SendData.SendItemAction(this, hero, action);
    }

    @Override
    public boolean isIdentified() {
        return identified;
    }
        public static class UI {
            private final Label topLeft;
            private final Label topRight;
            private final Label bottomRight;
            private final ColorBlock background;

            public UI(JSONObject obj) {
                JSONObject topLeftObj = obj.isNull("top_left") ? null : obj.optJSONObject("top_left");
                if (topLeftObj == null) {
                    topLeft = new Label(null, null, false);
                } else {
                    Integer color = null;
                    if (topLeftObj.has("color") && !topLeftObj.isNull("color")) {
                        color = topLeftObj.optInt("color", 0);
                    }
                    topLeft = new Label(
                            color,
                            topLeftObj.isNull("text") ? null : topLeftObj.optString("text", ""),
                            topLeftObj.optBoolean("visible", false)
                    );
                }

                JSONObject topRightObj = obj.optJSONObject("top_right");
                if (topRightObj == null) {
                    topRight = new Label(null, null, false);
                } else {
                    Integer color = null;
                    if (topRightObj.has("color") && !topRightObj.isNull("color")) {
                        color = topRightObj.optInt("color", 0);
                    }
                    topRight = new Label(
                            color,
                            topRightObj.isNull("text") ? null : topRightObj.optString("text", ""),
                            topRightObj.optBoolean("visible", false)
                    );
                }

                JSONObject bottomRightObj = obj.optJSONObject("bottom_right");
                if (bottomRightObj == null) {
                    bottomRight = new Label(null, null, false);
                } else {
                    Integer color = null;
                    if (bottomRightObj.has("color") && !bottomRightObj.isNull("color")) {
                        color = bottomRightObj.optInt("color", 0);
                    }
                    bottomRight = new Label(
                            color,
                            bottomRightObj.isNull("text") ? null : bottomRightObj.optString("text", ""),
                            bottomRightObj.optBoolean("visible", false)
                    );
                }
                if (obj.isNull("background")) {
                    background = null;
                } else {
                    JSONObject bgObj = obj.getJSONObject("background");
                    background = new ColorBlock(1,1,  0);
                    background.ra = (float) bgObj.optDouble("ra", 0);
                    background.ga = (float) bgObj.optDouble("ga", 0);
                    background.ba = (float) bgObj.optDouble("ba", 0);
                }
            }

            public UI() {
                topLeft = new Label(null, null, false);
                topRight = new Label(null, null, false);
                bottomRight = new Label(null, null, false);
                background = null;
            }

            public Label getTopLeft() {
                return topLeft;
            }

            public Label getTopRight() {
                return topRight;
            }

            public Label getBottomRight() {
                return bottomRight;
            }

            public ColorBlock getBackground() {
                return background;
            }
        }

            public static class Label {
                private final String text;
                private final Integer color;
                private final boolean visible;

                public Label(Integer color, String text, boolean visible) {
                    this.text = text;
                    this.color = color;
                    this.visible = visible;
                }

                public String getText() {
                    return text;
                }

                public Integer getColor() {
                    return color;
                }
                public boolean isVisible() {
                    return visible;
                }
    }
}




