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
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.KindofMisc;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.utils.Random;
import org.jetbrains.annotations.Contract;

public class Ring extends KindofMisc {
	
	protected Buff buff;

	//rings cannot be 'used' like other equipment, so they ID purely based on exp
	private float levelsToID = 1;


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
        return false;
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
