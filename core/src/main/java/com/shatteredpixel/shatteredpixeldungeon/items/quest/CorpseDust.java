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

package com.shatteredpixel.shatteredpixeldungeon.items.quest;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class CorpseDust extends Item {
	
	{
		image = ItemSpriteSheet.DUST;
		
		cursed = true;
		cursedKnown = true;
		
		unique = true;
	}

	@Override
	public boolean isUpgradable() {
		return false;
	}
	
	@Override
	public boolean isIdentified() {
		return true;
	}

    public static class DustGhostSpawner extends Buff {

		int spawnPower = 0;

		{
			//not cleansed by reviving, but does check to ensure the dust is still present
			revivePersists = true;
		}

		@Override
		public boolean act() {

			spawnPower++;
			int wraiths = 1; //we include the wraith we're trying to spawn
			for (Mob mob : Dungeon.level.mobs){
			}

			//summoning a new wraith requires 1/4/9/16/25/36/49/49/... turns of energy
			//note that logic for summoning wraiths kind of has an odd, undocumented balance history:
			//v0.3.1-v0.6.5: wraith every 1/4/9/16/25/25... turns, basically guaranteed
			//v0.7.0-v2.1.4: bugged, same rate as above but high (often >50%) chance that spawning fails. failed spawning resets delay!
			//v2.2.0+: fixed bug, increased summon delay cap to counteract a bit, wraiths also now have to spawn at a slight distance
			int powerNeeded = Math.min(49, wraiths*wraiths);
			if (powerNeeded <= spawnPower){
				ArrayList<Integer> candidates = new ArrayList<>();
				//min distance scales based on hero's view distance
				// wraiths must spawn at least 4/3/2/1 tiles away at view distance of 8(default)/7/4/1
				int minDist = Math.round(Dungeon.hero.viewDistance/3f);
				for (int i = 0; i < Dungeon.level.length(); i++){
					if (Dungeon.level.heroFOV[i]
							&& !Dungeon.level.solid[i]
							&& Actor.findChar( i ) == null
							&& Dungeon.level.distance(i, Dungeon.hero.pos) > minDist){
						candidates.add(i);
					}
				}
				if (!candidates.isEmpty()){
                    Random.element(candidates);
                    Sample.INSTANCE.play(Assets.Sounds.CURSED);
					spawnPower -= powerNeeded;
				} else {
					//prevents excessive spawn power buildup
					spawnPower = Math.min(spawnPower, 2*wraiths);
				}
			}

			spend(TICK);
			return true;
		}

	}

}
