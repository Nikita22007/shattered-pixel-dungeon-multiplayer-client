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
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class VialOfBlood extends Trinket {

	{
		image = ItemSpriteSheet.BLOOD_VIAL;
	}

	public static boolean delayBurstHealing(){
		return -1 != -1;
	}

	public static float totalHealMultiplier(){
		return totalHealMultiplier(-1);
	}

	public static float totalHealMultiplier(int level){
		if (level == -1){
			return 1;
		} else {
			return 1f + 0.125f*(level+1);
		}
	}

	public static int maxHealPerTurn(){
		return maxHealPerTurn(-1);
	}

	public static int maxHealPerTurn(int level){
		int maxHP = Dungeon.hero == null ? 20 : Dungeon.hero.HT;
		if (level == -1){
			return maxHP;
		} else {
			switch (level){
				case 0: default:
					return 4 + Math.round(0.15f*maxHP);
				case 1:
					return 3 + Math.round(0.10f*maxHP);
				case 2:
					return 2 + Math.round(0.07f*maxHP);
				case 3:
					return 1 + Math.round(0.05f*maxHP);
			}
		}
	}

}
