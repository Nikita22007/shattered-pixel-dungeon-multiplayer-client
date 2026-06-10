/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2025 Evan Debenham
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.Ratmogrify;
import com.shatteredpixel.shatteredpixeldungeon.journal.Notes;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.RatKingSprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.Holiday;

public class RatKing extends NPC {

    {
        spriteClass = RatKingSprite.class;

        state = SLEEPING;
    }

    @Override
    public int defenseSkill(Char enemy) {
        return INFINITE_EVASION;
    }

    @Override
    public float speed() {
        return 2f;
    }

    @Override
    protected Char chooseEnemy() {
        return null;
    }

    @Override
    public boolean add(Buff buff) {
        return false;
    }

    @Override
    public boolean reset() {
        return true;
    }

    //***This functionality is for when rat king may be summoned by a distortion trap

    @Override
    protected void onAdd() {
        super.onAdd();
        if (false && Dungeon.depth != 5) {
            yell(Messages.get(this, "confused"));
        }
    }

    @Override
    public Notes.Landmark landmark() {
        return Dungeon.depth == 5 ? Notes.Landmark.RAT_KING : null;
    }

    @Override
    protected boolean act() {
        if (Dungeon.depth < 5) {
            if (pos == Dungeon.level.exit()) {
                destroy();
                sprite.killAndErase();
            } else {
                target = Dungeon.level.exit();
            }
        } else if (Dungeon.depth > 5) {
            if (pos == Dungeon.level.entrance()) {
                destroy();
                sprite.killAndErase();
            } else {
                target = Dungeon.level.entrance();
            }
        }
        return super.act();
    }

    //***

    @Override
    public String description() {
        if (Dungeon.hero != null && Dungeon.hero.armorAbility instanceof Ratmogrify) {
            return Messages.get(this, "desc_crown");
        } else if (Holiday.getCurrentHoliday() == Holiday.APRIL_FOOLS) {
            return Messages.get(this, "desc_birthday");
        } else if (Holiday.getCurrentHoliday() == Holiday.WINTER_HOLIDAYS) {
            return Messages.get(this, "desc_winter");
        } else {
            return super.description();
        }
    }
}
