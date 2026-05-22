package com.shatteredpixel.shatteredpixeldungeon.network.actions;

import java.util.HashMap;
import java.util.Map;

public class ActionParserRegistry {

    private final Map<String, ActionParser> parsers = new HashMap<>();

    public void register(String actionName, ActionParser parser) {
        parsers.put(actionName, parser);
    }

    public ActionParser get(String actionName) {
        return parsers.get(actionName);
    }
}
