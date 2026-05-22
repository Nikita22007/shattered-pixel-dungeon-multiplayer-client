package com.shatteredpixel.shatteredpixeldungeon.network.actions;

public class ItemUpdateParser extends BaseItemActionParser {
    @Override
    protected String getMode() {
        return "update";
    }
}
