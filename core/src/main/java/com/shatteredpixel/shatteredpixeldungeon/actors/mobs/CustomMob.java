package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;


import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.RatSprite;
//FIXME
public class CustomMob extends Mob {
    public String name;
    @Override
    public String name() {
        return Messages.get(name);
    }

    public CustomMob(int id) {
        name = "unknown";
        spriteClass = RatSprite.class;

        HP = HT = 1;
        defenseSkill = 1;

        maxLvl = 1;

        this.setId(id);
    }

}