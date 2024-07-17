package com.shatteredpixel.shatteredpixeldungeon.items.bags

import com.shatteredpixel.shatteredpixeldungeon.actors.Actor
import com.shatteredpixel.shatteredpixeldungeon.items.CustomItem.Companion.createItem
import com.shatteredpixel.shatteredpixeldungeon.items.Item
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet.Icons
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog
import org.json.JSONArray
import org.json.JSONObject
import java.util.*


class CustomBag(obj: JSONObject) : Bag(obj) {
    //FIXME
    public var icon: Icons = Icons.BACKPACK;

    init {
        cursedKnown = true // todo check it
        size = obj.getInt("size");
        if (obj.has("owner")) {
            owner = Actor.findById(obj.getInt("owner")) as Char?
        }
        if (obj.has("items")) {
            addItemsFromJSONArray(obj.getJSONArray("items"))
        }
        if (obj.has("icon")) {
            try {
                //FIXME
                icon = Icons.valueOf(obj.getString("icon").uppercase(Locale.ENGLISH))
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

}