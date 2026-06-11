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

import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.watabou.utils.Bundle;

public abstract class Trinket extends Item {

	{
		levelKnown = true;

		unique = true;
	}

	@Override
	public boolean isUpgradable() {
		return false;
	}

	@Override
	public String info() {
		String info = super.info();
		info += "\n\n" + statsDesc();
		return info;
	}

	public abstract String statsDesc();

	public int energyVal() {
		return 5;
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		levelKnown = cursedKnown = true; //for pre-2.5 saves
	}

}
