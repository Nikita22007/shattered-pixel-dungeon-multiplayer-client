package com.shatteredpixel.shatteredpixeldungeon.network.text;

import com.nikita22007.multiplayer.utils.text.LocalizedString;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

public class LocalizedStringParser {

    private final LocalizedKeyParser keyParser = new LocalizedKeyParser();

    public LocalizedString parse(Object value) throws JSONException {
        if (value instanceof JSONObject) {
            JSONObject object = (JSONObject) value;
            String mode = object.optString("mode", "key");
            Object[] args = parseArgs(object.optJSONArray("args"));

            if ("raw".equals(mode)) {
                return LocalizedString.raw(object.optString("raw", ""), args);
            }
            if ("transform".equals(mode)) {
                return LocalizedString.transform(
                        parseTransform(object.getString("transform")),
                        parse(object.get("text"))
                );
            }
            if ("concat".equals(mode)) {
                return LocalizedString.concat(parseArgs(object.optJSONArray("parts")));
            }
            return LocalizedString.key(keyParser.parse(object.getJSONObject("key")), args);
        }

        if (value == JSONObject.NULL || value == null) {
            return LocalizedString.raw("");
        }

        return LocalizedString.raw(String.valueOf(value));
    }

    private LocalizedString.Transform parseTransform(String transform) {
        return LocalizedString.Transform.valueOf(transform.toUpperCase(Locale.ROOT));
    }

    private Object[] parseArgs(JSONArray array) throws JSONException {
        if (array == null) {
            return new Object[0];
        }

        Object[] args = new Object[array.length()];
        for (int i = 0; i < array.length(); i++) {
            Object arg = array.get(i);
            if (arg instanceof JSONObject && ((JSONObject) arg).has("mode")) {
                args[i] = parse(arg);
            } else if (arg == JSONObject.NULL) {
                args[i] = null;
            } else {
                args[i] = arg;
            }
        }
        return args;
    }
}
