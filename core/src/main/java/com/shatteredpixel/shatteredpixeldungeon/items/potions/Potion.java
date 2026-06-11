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

package com.shatteredpixel.shatteredpixeldungeon.items.potions;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Fire;
import com.shatteredpixel.shatteredpixeldungeon.effects.Splash;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

import java.util.*;

public class Potion extends Item {

	public static final String AC_DRINK = "DRINK";

	protected String color;

	//affects how strongly on-potion talents trigger from this potion
	protected float talentFactor = 1;
	//the chance (0-1) of whether on-potion talents trigger from this potion
	protected float talentChance = 1;
	
	{
		stackable = true;
		defaultAction = AC_DRINK;
	}


	public Potion() {
		super();
		reset();
	}
	
	//anonymous potions are always IDed, do not affect ID status,
	//and their sprite is replaced by a placeholder if they are not known,
	//useful for items that appear in UIs, or which are only spawned for their effects
	protected boolean anonymous = false;
	public void anonymize(){
		if (!isKnown()) image = ItemSpriteSheet.POTION_HOLDER;
		anonymous = true;
	}



	public boolean isKnown() {
		if (anonymous) return true;
		return true;
	}

	@Override
	public String name() {
		return isKnown() ? super.name() : Messages.get(this, color);
	}


	@Override
	public String desc() {
		return isKnown() ? super.desc() : Messages.get(this, "unknown_desc");
	}
	
	@Override
	public boolean isIdentified() {
		return isKnown();
	}
	
	@Override
	public boolean isUpgradable() {
		return false;
	}
	
	public static HashSet<Class<? extends Potion>> getKnown() {
		return new HashSet<>();
	}
	
	public static HashSet<Class<? extends Potion>> getUnknown() {
		return new LinkedHashSet<>();
	}

	protected int splashColor(){
		return anonymous ? 0x00AAFF : ItemSprite.pick( image, 5, 9 );
	}
	
	protected void splash( int cell ) {
		Fire fire = (Fire)Dungeon.level.blobs.get( Fire.class );
		if (fire != null) {
			fire.clear(cell);
		}

		Char ch = Actor.findChar(cell);
		if (ch != null && ch.alignment == Char.Alignment.ALLY) {
        }

		if (Dungeon.level.heroFOV[cell]) {
			if (ch != null) {
				Splash.at(ch.sprite.center(), splashColor(), 5);
			} else {
				Splash.at(cell, splashColor(), 5);
			}
		}
	}
	
	@Override
	public int value() {
		return 30 * quantity;
	}

	@Override
	public int energyVal() {
		return 6 * quantity;
	}

}
