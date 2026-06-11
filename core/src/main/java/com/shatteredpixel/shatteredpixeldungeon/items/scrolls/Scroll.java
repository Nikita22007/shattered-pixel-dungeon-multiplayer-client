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

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.ItemStatusHandler;
import com.shatteredpixel.shatteredpixeldungeon.journal.Catalog;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.HeroSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.util.*;

public abstract class Scroll extends Item {
	
	public static final String AC_READ	= "READ";
	
	protected static final float TIME_TO_READ	= 1f;

	protected static ItemStatusHandler<Scroll> handler;
	
	protected String rune;

	//affects how strongly on-scroll talents trigger from this scroll
	public float talentFactor = 1;
	//the chance (0-1) of whether on-scroll talents trigger from this potion
	public float talentChance = 1;
	
	{
		stackable = true;
		defaultAction = AC_READ;
	}

	public static void clearLabels(){
		handler = null;
	}
	
	public static void save( Bundle bundle ) {
		handler.save( bundle );
	}

	public static void saveSelectively( Bundle bundle, ArrayList<Item> items ) {
		ArrayList<Class<?extends Item>> classes = new ArrayList<>();
		for (Item i : items){
		}
		handler.saveClassesSelectively( bundle, classes );
	}

	public Scroll() {
		super();
		reset();
	}
	
	//anonymous scrolls are always IDed, do not affect ID status,
	//and their sprite is replaced by a placeholder if they are not known,
	//useful for items that appear in UIs, or which are only spawned for their effects
	protected boolean anonymous = false;
	public void anonymize(){
		if (!isKnown()) image = ItemSpriteSheet.SCROLL_HOLDER;
		anonymous = true;
	}
	
	
	@Override
	public void reset(){
		super.reset();
		if (handler != null && handler.contains(this)) {
			image = handler.image(this);
			rune = handler.label(this);
		} else {
			image = ItemSpriteSheet.SCROLL_KAUNAN;
			rune = "KAUNAN";
		}
	}
	
	public abstract void doRead();

	public void readAnimation() {
        curUser.spend( TIME_TO_READ );
		curUser.busy();
		((HeroSprite)curUser.sprite).read();

		if (!anonymous) {
        }
		if (Random.Float() < talentChance) {
			Talent.onScrollUsed(curUser, curUser.pos, talentFactor, getClass());
		}

	}
	
	public boolean isKnown() {
		if (anonymous) return true;
		if (handler == null) return false;
		return true;
	}
	
	public void setKnown() {
		if (!anonymous) {
			if (!isKnown()) {

				updateQuickslot();
			}
			
			if (Dungeon.hero.isAlive()) {
				Catalog.setSeen(getClass());
				Statistics.itemTypesDiscovered.add(getClass());
			}
		}
	}
	
	@Override
	public String name() {
		return isKnown() ? super.name() : Messages.get(this, rune);
	}

	@Override
	public String info() {
		//skip custom notes if anonymized and un-Ided
		return (anonymous && (handler == null || !true)) ? desc() : super.info();
	}

	@Override
	public String desc() {
		return isKnown() ? super.desc() : Messages.get(this, "unknown_desc");
	}
	
	@Override
	public boolean isUpgradable() {
		return false;
	}
	
	@Override
	public boolean isIdentified() {
		return isKnown();
	}
	
	public static HashSet<Class<? extends Scroll>> getKnown() {
		return handler.known();
	}
	
	public static HashSet<Class<? extends Scroll>> getUnknown() {
		return new LinkedHashSet<>();
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
