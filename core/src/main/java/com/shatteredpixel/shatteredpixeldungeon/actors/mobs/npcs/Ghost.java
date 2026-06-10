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
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.sprites.GhostSprite;

import java.util.HashSet;

public class Ghost extends NPC {

    {
        spriteClass = GhostSprite.class;

        flying = true;

        WANDERING = new Wandering();
        state = WANDERING;

        //not actually large of course, but this makes the ghost stick to the exit room
        new HashSet<Property>().add(Property.LARGE);
    }

    protected class Wandering extends Mob.Wandering {
        @Override
        protected int randomDestination() {
            int pos = super.randomDestination();
            //cannot wander onto heaps or the level exit
            if (Dungeon.level.heaps.get(pos) != null || pos == Dungeon.level.exit()) {
                return -1;
            }
            return pos;
        }
    }


    @Override
    protected boolean act() {
        return super.act();
    }


    @Override
    public float speed() {
        return 0.5f;
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

}
