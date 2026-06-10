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

package com.shatteredpixel.shatteredpixeldungeon.items.potions.exotic;

import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class PotionOfDivineInspiration extends ExoticPotion {
	
	{
		icon = ItemSpriteSheet.Icons.POTION_DIVINE;

		talentFactor = 2f;
	}

	protected static boolean identifiedByUse = false;

	public static class DivineInspirationTracker extends Buff {

		{
			type = buffType.POSITIVE;
			revivePersists = true;
		}

		private boolean[] boostedTiers = new boolean[5];

		private static final String BOOSTED_TIERS = "boosted_tiers";

		public void setBoosted( int tier ){
			boostedTiers[tier] = true;
		}

		public boolean isBoosted( int tier ){
			return boostedTiers[tier];
		}

	}
	
}
