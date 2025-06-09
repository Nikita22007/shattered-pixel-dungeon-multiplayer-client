package com.nikita22007.pixeldungeonmultiplayer;

import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Text {
    String key;
    String text;
    Object[] args = new Object[0];
    public static String fromMessage(String key, Object... args){
        String toFetch = key;
        toFetch = removeRoot(key);
        return Messages.get(toFetch, args);
    }
    public static String removeRoot(String key){
        return key.replace("com.shatteredpixel.shatteredpixeldungeon.", "");
    }
    public static Text of(Object jsonOrString) {

        if (jsonOrString instanceof String) {
            return new Text((String) jsonOrString);
        } else if (jsonOrString instanceof JSONObject){
            return new Text((JSONObject) jsonOrString);
        }
        return new Text();
    }
    public static Text of(String jsonOrString){
        try {
            JSONObject object = new JSONObject(jsonOrString);
            return Text.of(object);
        } catch (JSONException e){
            return Text.of((Object)jsonOrString);
        }
    }
    public String asString(){
        if (key == null){
            return text;
        } else {
            return Messages.get(key, args);
        }

    }

    public Text(JSONObject object) {
        if (object.has("key")){
            key = removeRoot(object.getString("key"));
        } else {
            GLog.n("Message object without key");
            text = "!!NO TEXT FOUND!!";
        }
        if (object.has("args")){
            JSONArray argsArray = object.getJSONArray("args");
            args = new Object[argsArray.length()];
            for (int i = 0; i < argsArray.length(); i++) {
                args[i] = argsArray.get(i);
            }
        }
    }

    public Text() {
    }

    public Text(String text) {
        this.text = text;
    }
}
