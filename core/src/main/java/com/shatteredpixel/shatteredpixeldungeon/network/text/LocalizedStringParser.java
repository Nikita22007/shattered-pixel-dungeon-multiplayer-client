package com.shatteredpixel.shatteredpixeldungeon.network.text;

import com.nikita22007.multiplayer.utils.text.LocalizedKey;
import com.nikita22007.multiplayer.utils.text.LocalizedString;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

public class LocalizedStringParser {

    public LocalizedString parse(Object value) throws JSONException {
        if (value instanceof JSONObject) {
            JSONObject object = (JSONObject) value;
            String type = object.getString("type");
            Object[] args = parseArgs(object.optJSONArray("args"));

            if ("raw".equals(type)) {
                return LocalizedString.raw(object.optString("raw", ""), args);
            }
            if ("transform".equals(type)) {
                return LocalizedString.transform(
                        parseTransform(object.getString("transform")),
                        parse(object.get("text"))
                );
            }
            if ("concat".equals(type)) {
                return LocalizedString.concat(parseArgs(object.optJSONArray("parts")));
            }
            if ("truncate".equals(type)) {
                return LocalizedString.truncate(
                        parse(object.get("text")),
                        object.getInt("max_length"),
                        object.optString("ellipsis", "")
                );
            }
            if ("replace".equals(type)) {
                return parse(object.get("text")).replace(
                        parseChar(object.getString("old_char")),
                        parseChar(object.getString("new_char"))
                );
            }
            if ("key".equals(type)) {
                return LocalizedString.key(parseKey(object.getJSONObject("key")), args);
            }
            throw new JSONException("Unknown localized string type: " + type);
        }

        if (value == JSONObject.NULL || value == null) {
            return null;
        }

        return LocalizedString.raw(String.valueOf(value));
    }

    private LocalizedString.Transform parseTransform(String transform) {
        return LocalizedString.Transform.valueOf(transform.toUpperCase(Locale.ROOT));
    }

    private LocalizedKey parseKey(JSONObject object) throws JSONException {
        String type = object.getString("type");
        if (!"localized_key".equals(type)) {
            throw new JSONException("Expected localized_key, got: " + type);
        }
        String owner = object.has("owner") && !object.isNull("owner") ? object.getString("owner") : null;
        return new LocalizedKey(owner, object.getString("name"));
    }

    private char parseChar(String value) throws JSONException {
        if (value.length() != 1) {
            throw new JSONException("Expected single character, got: " + value);
        }
        return value.charAt(0);
    }

    private Object[] parseArgs(JSONArray array) throws JSONException {
        if (array == null) {
            return new Object[0];
        }

        Object[] args = new Object[array.length()];
        for (int i = 0; i < array.length(); i++) {
            Object arg = array.get(i);
            if (isLocalizedStringObject(arg)) {
                args[i] = parse(arg);
            } else if (arg == JSONObject.NULL) {
                args[i] = null;
            } else {
                args[i] = arg;
            }
        }
        return args;
    }

    private boolean isLocalizedStringObject(Object value) {
        if (!(value instanceof JSONObject)) {
            return false;
        }
        JSONObject object = (JSONObject) value;
        return object.has("type") && !"localized_key".equals(object.optString("type"));
    }
}
