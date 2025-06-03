package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;


import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.RatSprite;
//FIXME
public class CustomMob extends Mob {
    @Override
    public String name() {
        String messageName = Messages.get(name);
        if (Messages.NO_TEXT_FOUND.equals(messageName)) {
            return name;
        }
        return messageName;
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