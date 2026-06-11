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

package com.shatteredpixel.shatteredpixeldungeon.items.trinkets;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class MossyClump extends Trinket {

	{
		image = ItemSpriteSheet.MOSSY_CLUMP;
	}

	public static float overrideNormalLevelChance(){
		return overrideNormalLevelChance(-1);
	}

	public static float overrideNormalLevelChance( int level ){
		if (level == -1){
			return 0f;
		} else {
			return 0.25f + 0.25f*level;
		}
	}

	//true for grass, false for water
	//ensures a little consistency of RNG
	private ArrayList<Boolean> levelFeels = new ArrayList<>();
	private int shuffles = 0;

	public static Level.Feeling getNextFeeling(){
		MossyClump clump = Dungeon.hero.belongings.getItem(MossyClump.class);
		if (clump == null) {
			return Level.Feeling.NONE;
		}
		if (clump.levelFeels.isEmpty()){
			Random.pushGenerator(Dungeon.seed+1);
				clump.levelFeels.add(true);
				clump.levelFeels.add(true);
				clump.levelFeels.add(false);
				clump.levelFeels.add(false);
				clump.levelFeels.add(false);
				clump.levelFeels.add(false);
				for (int i = 0; i <= clump.shuffles; i++) {
					Random.shuffle(clump.levelFeels);
				}
				clump.shuffles++;
			Random.popGenerator();
		}

		return clump.levelFeels.remove(0) ? Level.Feeling.GRASS : Level.Feeling.WATER;
	}

}
