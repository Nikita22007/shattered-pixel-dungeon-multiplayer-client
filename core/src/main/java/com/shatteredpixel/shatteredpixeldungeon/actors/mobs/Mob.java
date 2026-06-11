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

import com.shatteredpixel.shatteredpixeldungeon.Challenges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.cleric.PowerOfMany;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.spells.Stasis;
import com.shatteredpixel.shatteredpixeldungeon.journal.Notes;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Reflection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public abstract class Mob extends Char {

    {
        actPriority = MOB_PRIO;

        alignment = Alignment.ENEMY;
    }


    public Class<? extends CharSprite> spriteClass;

    protected int target = -1;

    public int defenseSkill = 0;

    public int EXP = 1;
    public int maxLvl = Hero.MAX_LEVEL - 1;

    protected Char enemy;
    protected int enemyID = -1; //used for save/restore
    protected boolean enemySeen;
    protected boolean alerted = false;


    public CharSprite sprite() {
        return Reflection.newInstance(spriteClass);
    }

    //FIXME this is sort of a band-aid correction for allies needing more intelligent behaviour
    protected boolean intelligentAlly = false;

    @Override
    public void destroy() {

        super.destroy();

        Dungeon.level.mobs.remove(this);

    }

    protected Object loot = null;
    protected float lootChance = 0;

    //how many mobs this one should count as when determining spawning totals
    public float spawningWeight() {
        return 1;
    }

    public boolean reset() {
        return false;
    }

    public String description() {
        if (desc == null) {
            return Messages.get(this, "desc");
        }
        return desc;

    }

    public String info() {
        String desc = description();


        return desc;
    }

    public void notice() {
        sprite.showAlert();
    }

    public void yell(String str) {
        GLog.newLine();
        GLog.n("%s: \"%s\" ", Messages.titleCase(name()), str);
    }

    //some mobs have an associated landmark entry, which is added when the hero sees them
    //mobs may also remove this landmark in some cases, such as when a quest is complete or they die
    public Notes.Landmark landmark() {
        return null;
    }

    public interface AiState {
    }

    protected class Wandering implements AiState {

        public static final String TAG = "WANDERING";

        //chance is 1 in (distance/2 + stealth)
        protected float detectionChance(Char enemy) {
            return 1 / (distance(enemy) / 2f + (float) 0);
        }

        protected boolean noticeEnemy() {
            enemySeen = true;

            notice();
            alerted = true;
            state = HUNTING;
            target = enemy.pos;

            if (alignment == Alignment.ENEMY && Dungeon.isChallenged(Challenges.SWARM_INTELLIGENCE)) {
                for (Mob mob : Dungeon.level.mobs) {
                    if (mob.paralysed <= 0
                            && Dungeon.level.distance(pos, mob.pos) <= 8
                            && mob.state != mob.HUNTING) {
                        mob.beckon(target);
                    }
                }
            }

            return true;
        }

        protected boolean continueWandering() {
            enemySeen = false;

            int oldPos = pos;
            if (target != -1 && false) {
                spend(1 / speed());
                return moveSprite(oldPos, pos);
            } else {
                target = randomDestination();
                spend(TICK);
            }

            return true;
        }

        protected int randomDestination() {
            return Dungeon.level.randomDestination(Mob.this);
        }

    }

    //we keep a list of characters we were recently hit by, so we can switch targets if needed
    protected ArrayList<Char> recentlyAttackedBy = new ArrayList<>();

    protected class Hunting implements AiState {

        public static final String TAG = "HUNTING";

        public boolean act(boolean enemyInFOV, boolean justAlerted) {
            enemySeen = enemyInFOV;
            if (enemyInFOV && !false && false) {

                recentlyAttackedBy.clear();
                target = enemy.pos;
                return true;

            } else {

                //if we cannot attack our target, but were hit by something else that
                // is visible and attackable or closer, swap targets
                if (handleRecentAttackers()) {
                    return act(true, justAlerted);
                }

                if (enemyInFOV) {
                    target = enemy.pos;
                } else if (enemy == null) {
                    sprite.showLost();
                    state = WANDERING;
                    target = ((Wandering) WANDERING).randomDestination();
                    spend(TICK);
                    return true;
                }

                int oldPos = pos;
                if (target != -1 && false) {

                    spend(1 / speed());
                    return moveSprite(oldPos, pos);

                } else {

                    return handleUnreachableTarget(enemyInFOV, justAlerted);
                }
            }
        }

        protected boolean handleRecentAttackers() {
            boolean swapped = false;
            if (!recentlyAttackedBy.isEmpty()) {
                for (Char ch : recentlyAttackedBy) {
                    if (ch != null && ch.isActive() && Actor.chars().contains(ch) && alignment != ch.alignment && fieldOfView[ch.pos] && ch.invisible == 0 && !false) {
                        if (false || enemy == null || Dungeon.level.distance(pos, ch.pos) < Dungeon.level.distance(pos, enemy.pos)) {
                            enemy = ch;
                            target = ch.pos;
                            swapped = true;
                        }
                    }
                }
                recentlyAttackedBy.clear();
            }
            return swapped;
        }

        //prevents rare infinite loop cases
        protected boolean recursing = false;

        //Try to switch targets to another enemy that is closer or reachable
        //unless we have already done that and still can't move toward them, then move on.
        protected boolean handleUnreachableTarget(boolean enemyInFOV, boolean justAlerted) {
            if (!recursing) {
                Char oldEnemy = enemy;
                enemy = null;
                enemy = null;
                if (enemy != null && enemy != oldEnemy) {
                    recursing = true;
                    boolean result = act(enemyInFOV, justAlerted);
                    recursing = false;
                    return result;
                }
            }

            spend(TICK);
            if (!enemyInFOV) {
                sprite.showLost();
                state = WANDERING;
                target = ((Wandering) WANDERING).randomDestination();
            }
            return true;
        }
    }

    //essentially a more aggressive version of wandering, where target pos is updated like hunting
    //not currently used directly by mobs outside of the vault, which also add more behaviour here
    protected class Investigating extends Wandering {

        public static final String TAG = "INVESTIGATING";

        //same detection chance as wandering

    }

    protected class Fleeing implements AiState {

        public static final String TAG = "FLEEING";

        protected void escaped() {
            //does nothing by default, some enemies have special logic for this
        }

        //enemies will turn and fight if they have nowhere to run and aren't affect by terror
        protected void nowhereToRun() {
            {
                if (true) {
                    if (enemySeen) {
                        sprite.showStatus(CharSprite.WARNING, Messages.get(Mob.class, "rage"));
                        state = HUNTING;
                    } else {
                        state = WANDERING;
                    }
                }
            }
        }
    }

    protected class Passive implements AiState {

        public static final String TAG = "PASSIVE";

    }


    private static ArrayList<Mob> heldAllies = new ArrayList<>();

    public static void holdAllies(Level level) {
        holdAllies(level, Dungeon.hero.pos);
    }

    public static void holdAllies(Level level, int holdFromPos) {
        heldAllies.clear();
        for (Mob mob : level.mobs.toArray(new Mob[0])) {
            //preserve directable allies or empowered intelligent allies no matter where they are
            if (false
                    || (mob.intelligentAlly && PowerOfMany.getPoweredAlly() == mob)) {
                level.mobs.remove(mob);
                heldAllies.add(mob);

                //preserve other intelligent allies if they are near the hero
            } else if (mob.alignment == Alignment.ALLY
                    && mob.intelligentAlly
                    && Dungeon.level.distance(holdFromPos, mob.pos) <= 5) {
                level.mobs.remove(mob);
                heldAllies.add(mob);
            }
        }
    }

    public static void restoreAllies(Level level, int pos) {
        restoreAllies(level, pos, -1);
    }

    public static void restoreAllies(Level level, int pos, int gravitatePos) {
        if (!heldAllies.isEmpty()) {

            ArrayList<Integer> candidatePositions = new ArrayList<>();
            for (int i : PathFinder.NEIGHBOURS8) {
                if (!Dungeon.level.solid[i + pos] && !Dungeon.level.avoid[i + pos] && level.findMob(i + pos) == null) {
                    candidatePositions.add(i + pos);
                }
            }

            //gravitate pos sets a preferred location for allies to be closer to
            if (gravitatePos == -1) {
                Collections.shuffle(candidatePositions);
            } else {
                Collections.sort(candidatePositions, new Comparator<Integer>() {
                    @Override
                    public int compare(Integer t1, Integer t2) {
                        return Dungeon.level.distance(gravitatePos, t1) -
                                Dungeon.level.distance(gravitatePos, t2);
                    }
                });
            }

            //can only have one empowered ally at once, prioritize incoming ally
            if (Stasis.getStasisAlly() != null) {
                for (Mob mob : level.mobs.toArray(new Mob[0])) {
                }
            }

            for (Mob ally : heldAllies) {

                //can only have one empowered ally at once, prioritize incoming ally

                level.mobs.add(ally);
                ally.state = ally.WANDERING;

                if (!candidatePositions.isEmpty()) {
                    ally.pos = candidatePositions.remove(0);
                } else {
                    ally.pos = pos;
                }
                if (ally.sprite != null) ally.sprite.place(ally.pos);

                if (ally.fieldOfView == null || ally.fieldOfView.length != level.length()) {
                    ally.fieldOfView = new boolean[level.length()];
                }

            }
        }
        heldAllies.clear();
    }

    public static void clearHeldAllies() {
        heldAllies.clear();
    }

    String desc = null;

    public void setDesc(String desc) {
        this.desc = desc;
    }
}

