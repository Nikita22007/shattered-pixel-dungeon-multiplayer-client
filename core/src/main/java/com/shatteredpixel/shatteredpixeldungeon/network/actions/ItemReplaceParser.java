package com.shatteredpixel.shatteredpixeldungeon.network.actions;

public class ItemReplaceParser extends BaseItemActionParser {
    @Override
    protected String getMode() {
        return "replace";
    }
}
