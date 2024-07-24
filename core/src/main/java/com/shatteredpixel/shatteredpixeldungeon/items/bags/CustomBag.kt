package com.shatteredpixel.shatteredpixeldungeon.items.bags

import com.shatteredpixel.shatteredpixeldungeon.actors.Actor
import com.shatteredpixel.shatteredpixeldungeon.actors.Char
import com.shatteredpixel.shatteredpixeldungeon.items.Item
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet
import com.shatteredpixel.shatteredpixeldungeon.ui.Icons
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog
import org.json.JSONArray
import org.json.JSONObject
import java.util.Locale


open class CustomBag(obj: JSONObject) : Bag(obj) {
    private var size: Int;

    //FIXME
    public var icon: Icons = Icons.BACKPACK;

    init {
        cursedKnown = true // todo check it
        size = obj.optInt("size", -1);
        if (size < 0) {
            size = obj.getInt("capacity");
        }
        if (obj.has("owner")) {
            owner = Actor.findById(obj.getInt("owner")) as Char?
        }
        if (obj.has("items")) {
            addItemsFromJSONArray(obj.getJSONArray("items"))
        }
        if (obj.has("icon")) {
            try {
                Icons.valueOf(obj.getString("icon").uppercase(Locale.ENGLISH))
            } catch (e: RuntimeException) {
                GLog.n("incorrect icon: " + e.message);
                e.printStackTrace();
            }
        }
    }

    private fun addItemsFromJSONArray(arr: JSONArray) {
        for (i in 0 until arr.length()) {
            val itemObj = arr.getJSONObject(i);
            val item: Item = createItem(itemObj)
            items.add(item)
        }
    }

    override fun capacity(): Int {
        return size;
    }
}