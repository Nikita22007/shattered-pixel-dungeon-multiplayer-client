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

package com.shatteredpixel.shatteredpixeldungeon.items.rings;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.ItemStatusHandler;
import com.shatteredpixel.shatteredpixeldungeon.items.KindofMisc;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;
import org.jetbrains.annotations.Contract;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;

public class Ring extends KindofMisc {
	
	protected Buff buff;

	private static final LinkedHashMap<String, Integer> gems = new LinkedHashMap<String, Integer>() {
		{
			put("garnet",ItemSpriteSheet.RING_GARNET);
			put("ruby",ItemSpriteSheet.RING_RUBY);
			put("topaz",ItemSpriteSheet.RING_TOPAZ);
			put("emerald",ItemSpriteSheet.RING_EMERALD);
			put("onyx",ItemSpriteSheet.RING_ONYX);
			put("opal",ItemSpriteSheet.RING_OPAL);
			put("tourmaline",ItemSpriteSheet.RING_TOURMALINE);
			put("sapphire",ItemSpriteSheet.RING_SAPPHIRE);
			put("amethyst",ItemSpriteSheet.RING_AMETHYST);
			put("quartz",ItemSpriteSheet.RING_QUARTZ);
			put("agate",ItemSpriteSheet.RING_AGATE);
			put("diamond",ItemSpriteSheet.RING_DIAMOND);
		}
	};
	
	private static ItemStatusHandler<Ring> handler;

	//rings cannot be 'used' like other equipment, so they ID purely based on exp
	private float levelsToID = 1;
	
	@SuppressWarnings("unchecked")
	public static void initGems() {
		handler = new ItemStatusHandler<>( (Class<? extends Ring>[])Generator.Category.RING.classes, gems );
	}

	public static void clearGems(){
		handler = null;
	}

	public static void save( Bundle bundle ) {
		handler.save( bundle );
	}

	public static void saveSelectively( Bundle bundle, ArrayList<Item> items ) {
		handler.saveSelectively( bundle, items );
	}
	
	@SuppressWarnings("unchecked")
	public static void restore( Bundle bundle ) {
		handler = new ItemStatusHandler<>( (Class<? extends Ring>[])Generator.Category.RING.classes, gems, bundle );
	}
	
	public Ring() {
		super();
		reset();
	}

	//anonymous rings are always IDed, do not affect ID status,
	//and their sprite is replaced by a placeholder if they are not known,
	//useful for items that appear in UIs, or which are only spawned for their effects
	protected boolean anonymous = false;
	public void anonymize(){
		if (!isKnown()) image = ItemSpriteSheet.RING_HOLDER;
		anonymous = true;
	}
	
	public void reset() {
		super.reset();
		levelsToID = 1;
		if (handler != null && handler.contains(this)){
			image = handler.image(this);
		} else {
			image = ItemSpriteSheet.RING_GARNET;
		}
	}

	@Override
	public boolean doUnequip( Hero hero, boolean collect, boolean single ) {
		if (super.doUnequip( hero, collect, single )) {

			if (buff != null) {
				buff.detach();
				buff = null;
			}

			return true;

		} else {

			return false;

		}
	}

	@Contract(pure=true)
	public boolean isKnown() {
		if (anonymous) return true;
		if (handler == null) return false;
		return true;
	}


	protected String statsInfo(){
		return "";
	}

	public String upgradeStat1(int level){
		return null;
	}

	public String upgradeStat2(int level){
		return null;
	}

	public String upgradeStat3(int level){
		return null;
	}

	@Override
	public Item upgrade() {
		super.upgrade();
		
		if (Random.Int(3) == 0) {
			cursed = false;
		}
		
		return this;
	}

	public void setIDReady(){
		levelsToID = -1;
	}

	public boolean readyToIdentify(){
		return !isIdentified() && levelsToID <= 0;
	}

	@Override
	public Item random() {
		//+0: 66.67% (2/3)
		//+1: 26.67% (4/15)
		//+2: 6.67%  (1/15)
		int n = 0;
		if (Random.Int(3) == 0) {
			n++;
			if (Random.Int(5) == 0){
				n++;
			}
		}
		level(n);
		
		//30% chance to be cursed
		if (Random.Float() < 0.3f) {
			cursed = true;
		}
		
		return this;
	}
	
	public static HashSet<Class<? extends Ring>> getKnown() {
		return handler.known();
	}
	
	public static HashSet<Class<? extends Ring>> getUnknown() {
		return new LinkedHashSet<>();
	}

	@Override
	public int value() {
		int price = 75;
		if (cursed && cursedKnown) {
			price /= 2;
		}
		if (levelKnown) {
			if (level() > 0) {
				price *= (level() + 1);
			} else if (level() < 0) {
				price /= (1 - level());
			}
		}
		if (price < 1) {
			price = 1;
		}
		return price;
	}

	@Contract(pure = true)
    @Override
	public int buffedLvl() {
		int lvl = super.buffedLvl();
		return lvl;
	}

	//just used for ring descriptions
	public int soloBonus(){
		if (cursed){
			return Math.min( 0, Ring.this.level()-2 );
		} else {
			return Ring.this.level()+1;
		}
	}

	//just used for ring descriptions
	public int soloBuffedBonus(){
		if (cursed){
			return Math.min( 0, Ring.this.buffedLvl()-2 );
		} else {
			return Ring.this.buffedLvl()+1;
		}
	}

	//just used for ring descriptions
	public int combinedBonus(Hero hero){
		int bonus = 0;
		if (hero.belongings.misc() != null && hero.belongings.misc().getClass() == getClass()){
			bonus += ((Ring)hero.belongings.misc()).soloBonus();
		}
		return bonus;
	}

	//just used for ring descriptions
	public int combinedBuffedBonus(Hero hero){
		int bonus = 0;
		if (hero.belongings.misc() != null && hero.belongings.misc().getClass() == getClass()){
			bonus += ((Ring)hero.belongings.misc()).soloBuffedBonus();
		}
		return bonus;
	}

	public class RingBuff extends Buff {

		@Override
		public boolean attachTo( Char target ) {
			if (super.attachTo( target )) {
				//if we're loading in and the hero has partially spent a turn, delay for 1 turn
				if (false && Dungeon.hero == null && cooldown() == 0 && target.cooldown() > 0) {
					spend(TICK);
				}
				return true;
			}
			return false;
		}

		@Override
		public boolean act() {
			spend( TICK );
			return true;
		}

		public int level(){
			return Ring.this.soloBonus();
		}

		public int buffedLvl(){
			return Ring.this.soloBuffedBonus();
		}

	}
}
