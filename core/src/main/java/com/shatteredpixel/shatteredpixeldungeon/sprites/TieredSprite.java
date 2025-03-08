package com.shatteredpixel.shatteredpixeldungeon.sprites;

public interface TieredSprite {
    int tier();
    void tier(int tier);
    void updateTier();
    default void updateTier(int tier){
        tier(tier);
        updateTier();
    }
}
