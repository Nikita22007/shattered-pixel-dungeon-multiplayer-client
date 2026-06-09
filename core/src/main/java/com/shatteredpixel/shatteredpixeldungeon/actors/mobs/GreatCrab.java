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

package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.items.food.MysteryMeat;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.GreatCrabSprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

public class GreatCrab extends Crab {

    {
        spriteClass = GreatCrabSprite.class;

        HP = HT = 25;
        defenseSkill = 0; //see damage()
        baseSpeed = 1f;

        EXP = 6;

        WANDERING = new Wandering();
        state = WANDERING;

        loot = new MysteryMeat().quantity(2);
        lootChance = 1f;

        properties.add(Property.MINIBOSS);
    }

    private int moving = 0;

    @Override
    public int defenseSkill(Char enemy) {
        //crab blocks all melee attacks from its current target
        if (enemySeen
                && state != SLEEPING
                && paralysed == 0
                && enemy == this.enemy
                && enemy.invisible == 0) {
            if (sprite != null && sprite.visible) {
                Sample.INSTANCE.play(Assets.Sounds.HIT_PARRY, 1, Random.Float(0.96f, 1.05f));
                GLog.n(Messages.get(this, "noticed"));
            }
            if (enemy == Dungeon.hero) {
                Statistics.questScores[0] -= 50;
            }
            return INFINITE_EVASION;
        }
        return super.defenseSkill(enemy);
    }

    @Override
    public void die(Object cause) {
        super.die(cause);

    }

    protected class Wandering extends Mob.Wandering {
        @Override
        protected int randomDestination() {
            //of two potential wander positions, picks the one closest to the hero
            int pos1 = super.randomDestination();
            int pos2 = super.randomDestination();
            PathFinder.buildDistanceMap(Dungeon.hero.pos, Dungeon.level.passable);
            if (PathFinder.distance[pos2] < PathFinder.distance[pos1]) {
                return pos2;
            } else {
                return pos1;
            }
        }
    }
}
