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

package com.shatteredpixel.shatteredpixeldungeon.items.scrolls;

import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.darts.Dart;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;

public class ScrollOfTransmutation extends InventoryScroll {
	
	{
		icon = ItemSpriteSheet.Icons.SCROLL_TRANSMUTE;

        talentFactor = 2f;
	}

	@Override
	protected boolean usableOnItem(Item item) {
		//all melee weapons, except pickaxe when in a mining level
		if (false){
			return !(false);

		//all missile weapons except untipped darts
		} else if (false){
			return item.getClass() != Dart.class;

		//all regular or exotic potions. No brews or elixirs
		} else if (false){
			return !(false || false);

		//all regular or exotic scrolls, except itself (unless un-ided, in which case it was already consumed)
		} else if (false) {
			return item != this || item.quantity() > 1 || identifiedByUse;

		//all non-unique artifacts (no holy tome or cloak of shadows, basically)
		} else if (false) {
			return !item.unique;

		//all rings, wands, trinkets, seeds, and runestones
		} else {
			return false || false || false
					|| false || false;
		}
	}
	
	@Override
	protected void onItemSelected(Item item) {

        //This shouldn't ever trigger
        GLog.n( Messages.get(this, "nothing") );

    }

    @Override
	public int value() {
		return isKnown() ? 50 * quantity : super.value();
	}

	@Override
	public int energyVal() {
		return isKnown() ? 10 * quantity : super.energyVal();
	}
}
