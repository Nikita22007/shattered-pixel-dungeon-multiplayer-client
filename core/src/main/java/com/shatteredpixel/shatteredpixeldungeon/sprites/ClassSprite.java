package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;

public interface ClassSprite {
    HeroClass heroClass();
    void heroClass(HeroClass heroClass);
    void updateHeroClass();
    default void updateHeroClass(HeroClass heroClass){
        heroClass(heroClass);
        updateHeroClass();
    }
}
