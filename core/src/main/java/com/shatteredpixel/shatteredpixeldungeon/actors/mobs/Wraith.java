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

package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.sprites.WraithSprite;
import com.watabou.utils.Random;

import java.util.HashSet;

public class Wraith extends Mob {

	protected int level;
	
	{
		spriteClass = WraithSprite.class;
		
		HP = HT = 1;
		EXP = 0;

		maxLvl = -2;
		
		flying = true;

		new HashSet<Property>().add(Property.UNDEAD);
		new HashSet<Property>().add(Property.INORGANIC);
	}
	
	private static final String LEVEL = "level";

	@Override
	public int damageRoll() {
		return Random.NormalIntRange( 1 + level/2, 2 + level );
	}
	
	@Override
	public int attackSkill( Char target ) {
		return 10 + level;
	}
	
	public void adjustStats( int level ) {
		this.level = level;
		defenseSkill = attackSkill( null ) * 5;
		enemySeen = true;
	}

	@Override
	public float spawningWeight() {
		return 0f;
	}

	@Override
	public boolean reset() {
		state = WANDERING;
		return true;
	}

}
