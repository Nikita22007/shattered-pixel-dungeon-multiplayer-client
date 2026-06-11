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
import org.jetbrains.annotations.Contract;

public class ShardOfOblivion extends Trinket {

	{
		image = ItemSpriteSheet.OBLIVION_SHARD;
	}

    @Contract(pure = true)
	public static boolean passiveIDDisabled(){
        return -1 >= 0;
	}

	public static float lootChanceMultiplier(){
        return lootChanceMultiplier(-1);
	}

	public static float lootChanceMultiplier(int level) {
        if (level < 0) return 1f;

        int wornUnIDed = 0;
        if (Dungeon.hero.belongings.weapon() != null && !Dungeon.hero.belongings.weapon().isIdentified()) {
            wornUnIDed++;
        }
        if (Dungeon.hero.belongings.armor() != null && !Dungeon.hero.belongings.armor().isIdentified()) {
            wornUnIDed++;
        }
        if (Dungeon.hero.belongings.ring() != null && !Dungeon.hero.belongings.ring().isIdentified()) {
            wornUnIDed++;
        }
        if (Dungeon.hero.belongings.misc() != null && !Dungeon.hero.belongings.misc().isIdentified()) {
            wornUnIDed++;
        }

        wornUnIDed = Math.min(wornUnIDed, level + 1);
        return 1f + .2f * wornUnIDed;

    }
}
