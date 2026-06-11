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

package com.shatteredpixel.shatteredpixeldungeon.items;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class Generator {

	public enum Category {
		;

		//in case there are multiple matches, this will return the latest match
		@Contract(pure = true)
		public static int order( @NotNull Item item ){
			//because we don't have Item class hierarchy we can sour t it only with mages
			//todo should we send this?
			//items without a category-defined order are sorted based on the spritesheet
			return Short.MAX_VALUE+item.image();
		}


	}


}
