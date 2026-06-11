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
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.CeremonialCandle;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.Wand;
import com.shatteredpixel.shatteredpixeldungeon.journal.Notes;
import com.shatteredpixel.shatteredpixeldungeon.levels.RegularLevel;
import com.shatteredpixel.shatteredpixeldungeon.sprites.WandmakerSprite;
import com.watabou.utils.Bundle;

import java.util.HashSet;

public class Wandmaker extends NPC {

    {
        spriteClass = WandmakerSprite.class;
    }

    @Override
    public Notes.Landmark landmark() {
        return Notes.Landmark.WANDMAKER;
    }

    @Override
    protected boolean act() {
        return super.act();
    }

    @Override
    public int defenseSkill(Char enemy) {
        return INFINITE_EVASION;
    }

    @Override
    public boolean add(Buff buff) {
        return false;
    }

    @Override
    public boolean reset() {
        return true;
    }

    public static class Quest {

        private static int type;
        // 1 = corpse dust quest
        // 2 = elemental embers quest
        // 3 = rotberry quest

        private static boolean spawned;

        private static boolean given;

        public static Wand wand1;
        public static Wand wand2;

        public static void reset() {
            spawned = false;
            type = 0;

            wand1 = null;
            wand2 = null;
        }

        private static final String NODE = "wandmaker";

        private static final String SPAWNED = "spawned";
        private static final String TYPE = "type";
        private static final String GIVEN = "given";
        private static final String WAND1 = "wand1";
        private static final String WAND2 = "wand2";

        private static final String RITUALPOS = "ritualpos";

        public static void storeInBundle(Bundle bundle) {

            Bundle node = new Bundle();

            node.put(SPAWNED, spawned);

            if (spawned) {

                node.put(TYPE, type);

                node.put(GIVEN, given);


                if (type == 2) {
                    node.put(RITUALPOS, CeremonialCandle.ritualPos);
                }

            }

            bundle.put(NODE, node);
        }

        public static void restoreFromBundle(Bundle bundle) {

            Bundle node = bundle.getBundle(NODE);

            if (!node.isNull() && (spawned = node.getBoolean(SPAWNED))) {

                type = node.getInt(TYPE);

                given = node.getBoolean(GIVEN);

                wand1 = (Wand) node.get(WAND1);
                wand2 = (Wand) node.get(WAND2);

                if (type == 2) {
                    CeremonialCandle.ritualPos = node.getInt(RITUALPOS);
                }

            } else {
                reset();
            }
        }


        //quest is active if:
        public static boolean active() {
            //it is not completed
            if (wand1 == null || wand2 == null
                    || !(Dungeon.level instanceof RegularLevel) || Dungeon.hero == null) {
                return false;
            }

            //and...
            if (type == 1) {
                //hero is in the mass grave room
                return true;

                //or if they are corpse dust cursed

            } else if (type == 2) {
                //hero has summoned the newborn elemental
                for (Mob m : Dungeon.level.mobs) {
                }

                //or hero is in the ritual room and all 4 candles are with them
                int candles = 0;
                if (Dungeon.hero.belongings.getItem(CeremonialCandle.class) != null) {
                    candles += Dungeon.hero.belongings.getItem(CeremonialCandle.class).quantity();
                }

                if (candles >= 4) {
                    return true;
                }

                for (Heap h : Dungeon.level.heaps.valueList()) {
                    for (Item i : h.items) {
                    }

                }

                if (candles >= 4) {
                    return true;
                }


                return false;
            } else {
                //hero is in the rot garden room and the rot heart is alive
                for (Mob m : Dungeon.level.mobs) {
                }

                return false;
            }
        }

        public static void complete() {
            wand1 = null;
            wand2 = null;

            Notes.remove(Notes.Landmark.WANDMAKER);
            //other quests award score when their boss is defeated
            if (Quest.type == 1) {
                Statistics.questScores[1] += 2000;
            }
        }
    }
}
