package com.shatteredpixel.shatteredpixeldungeon.network.actions.heaps;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.network.ParseThread;
import com.shatteredpixel.shatteredpixeldungeon.network.actions.ActionParser;
import org.json.JSONException;
import org.json.JSONObject;

public class HeapRemoveParser implements ActionParser {
    @Override
    public void parse(ParseThread parseThread, JSONObject action) throws JSONException {
        int pos = action.getInt("pos");
        if (Dungeon.level != null) {
            Heap heap = Dungeon.level.heaps.get(pos, null);
            if (heap != null) {
                Dungeon.level.heaps.remove(pos);
                heap.destroy();
            }
        }
    }
}
