package com.shatteredpixel.shatteredpixeldungeon.actors.hero;

import com.badlogic.gdx.utils.IntMap;

public class TalentCache {
    private static final IntMap<Talent> talentsImageCache = new IntMap<>();
    static {
        for (Talent talent : Talent.values()) {
            talentsImageCache.put(talent.icon(), talent);
        }
    }
    public static Talent talentByIcon(int icon){
        return talentsImageCache.get(icon);
    }
}
