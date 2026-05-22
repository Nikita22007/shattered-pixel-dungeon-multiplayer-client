package com.shatteredpixel.shatteredpixeldungeon.network.actions;

public class ItemRemoveParser extends BaseItemActionParser {
    @Override
    protected String getMode() {
        return "remove";
    }
}
