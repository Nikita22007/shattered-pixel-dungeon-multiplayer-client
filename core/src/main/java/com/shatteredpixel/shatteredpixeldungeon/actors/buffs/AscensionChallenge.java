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

package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;

import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.watabou.noosa.Image;

public class AscensionChallenge extends Buff {

	//distant mobs get constantly beckoned to the hero at 2+ stacks

	//mobs move at 2x speed when not hunting/fleeing at 4 stacks or higher

	//hero speed is halved and capped at 1x at 6+ stacks

	{
		revivePersists = true;
	}

    @Override
	public boolean act() {

		spend(TICK);
		return true;
	}

	@Override
	public int icon() {
		return BuffIndicator.AMULET;
	}

	@Override
	public void tintIcon(Image icon) {
        icon.hardlight(0.5f, 1, 0);
    }

	@Override
	public String desc() {
		String desc = Messages.get(this, "desc");
		desc += "\n";
        desc += "\n" + Messages.get(this, "desc_clear");

        return desc;
	}

}
