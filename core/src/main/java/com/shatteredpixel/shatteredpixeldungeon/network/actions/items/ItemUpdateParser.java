package com.shatteredpixel.shatteredpixeldungeon.network.actions.items;

public class ItemUpdateParser extends BaseItemActionParser {
    @Override
    protected String getMode() {
        return "update";
    }
}
