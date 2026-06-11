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

import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class TrapMechanism extends Trinket {

	{
		image = ItemSpriteSheet.TRAP_MECHANISM;
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

	public static float revealHiddenTrapChance(){
		return revealHiddenTrapChance(-1);
	}

	public static float revealHiddenTrapChance( int level ){
		if (level == -1){
			return 0f;
		} else {
			return 0.1f + 0.1f*level;
		}
	}

}
