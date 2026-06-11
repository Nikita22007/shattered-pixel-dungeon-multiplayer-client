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

package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroSubClass;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfRecharging;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.Wand;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.particles.Emitter;
import com.watabou.noosa.particles.PixelParticle;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class MagesStaff extends MeleeWeapon {

	private Wand wand;

	public static final String AC_IMBUE = "IMBUE";
	public static final String AC_ZAP	= "ZAP";

	private static final float STAFF_SCALE_FACTOR = 0.75f;

	{
		image = ItemSpriteSheet.MAGES_STAFF;
		hitSound = Assets.Sounds.HIT;
		hitSoundPitch = 1.1f;

		tier = 1;

		defaultAction = AC_ZAP;
		usesTargeting = true;

		unique = true;
	}

	public MagesStaff() {
		wand = null;
	}

	@Override
	public int max(int lvl) {
		return  Math.round(3f*(tier+1)) +   //6 base damage, down from 10
				lvl*(tier+1);               //scaling unaffected
	}

	public MagesStaff(Wand wand){
		this();
		wand.cursed = false;
		this.wand = wand;
		updateWand(false);
		wand.curCharges = wand.maxCharges;
	}

	@Override
	public String defaultAction() {
		return AC_ZAP;
	}

	@Override
	public int targetingPos(Hero user, int dst) {
		if (wand != null) {
			return wand.targetingPos(user, dst);
		} else {
			return super.targetingPos(user, dst);
		}
	}

	@Override
	public int buffedVisiblyUpgraded() {
		if (wand != null){
			return Math.max(super.buffedVisiblyUpgraded(), wand.buffedVisiblyUpgraded());
		} else {
			return super.buffedVisiblyUpgraded();
		}
	}

	public Class<?extends Wand> wandClass(){
		return wand != null ? wand.getClass() : null;
	}

	@Override
	public Item upgrade(boolean enchant) {
		super.upgrade( enchant );

		updateWand(true);

		return this;
	}

	@Override
	public Item degrade() {
		super.degrade();

		updateWand(false);

		return this;
	}
	
	public void updateWand(boolean levelled){
		if (wand != null) {
			int curCharges = wand.curCharges;
			wand.level(level());
			//gives the wand one additional max charge
			wand.maxCharges = Math.min(wand.maxCharges + 1, 10);
			wand.curCharges = Math.min(curCharges + (levelled ? 1 : 0), wand.maxCharges);
			updateQuickslot();
		}
	}

	@Override
	public String status() {
		if (wand == null) return super.status();
		else return wand.status();
	}

	@Override
	public String name() {
		if (wand == null) {
			return super.name();
		} else {
			String name = Messages.get(wand, "staff_name");
			return enchantment != null && (cursedKnown || !enchantment.curse()) ? enchantment.name( name ) : name;
		}
	}

	@Override
	public String info() {
		String info = super.info();

		if (wand != null){
			info += "\n\n" + Messages.get(this, "has_wand", Messages.get(wand, "name"));
			if ((!cursed && !hasCurseEnchant()) || !cursedKnown)    info += " " + wand.statsDesc();
			else                                                    info += " " + Messages.get(this, "cursed_wand");

			if (Dungeon.hero.subClass == HeroSubClass.BATTLEMAGE){
				info += "\n\n" + Messages.get(wand, "bmage_desc");
			}
		}

		return info;
	}

	@Override
	public Emitter emitter() {
		if (wand == null) return null;
		Emitter emitter = new Emitter();
		emitter.pos(12.5f, 3);
		emitter.fillTarget = false;
		emitter.pour(StaffParticleFactory, 0.1f);
		return emitter;
	}

	private static final String WAND = "wand";

	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		bundle.put(WAND, wand);
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		wand = (Wand) bundle.get(WAND);
		if (wand != null) {
			wand.maxCharges = Math.min(wand.maxCharges + 1, 10);
		}
	}

	@Override
	public int value() {
		return 0;
	}
	
	@Override
	public Weapon enchant(Enchantment ench) {
		if (curseInfusionBonus && (ench == null || !ench.curse())){
			curseInfusionBonus = false;
			updateWand(false);
		}
		return super.enchant(ench);
	}

	private final Emitter.Factory StaffParticleFactory = new Emitter.Factory() {
		@Override
		//reimplementing this is needed as instance creation of new staff particles must be within this class.
		public void emit( Emitter emitter, int index, float x, float y ) {
			StaffParticle c = (StaffParticle)emitter.getFirstAvailable(StaffParticle.class);
			if (c == null) {
				c = new StaffParticle();
				emitter.add(c);
			}
			c.reset(x, y);
		}

		@Override
		//some particles need light mode, others don't
		public boolean lightMode() { //todo check it
			return !((false)
					|| (false)
					|| (false)
					|| (false)
					|| (false));
		}
	};

	//determines particle effects to use based on wand the staff owns.
	public class StaffParticle extends PixelParticle{

		private float minSize;
		private float maxSize;
		public float sizeJitter = 0;

		public StaffParticle(){
			super();
		}

		public void reset( float x, float y ) {
			revive();

			speed.set(0);

			this.x = x;
			this.y = y;

			if (wand != null)
				wand.staffFx( this );

		}

		public void setSize( float minSize, float maxSize ){
			this.minSize = minSize;
			this.maxSize = maxSize;
		}

		public void setLifespan( float life ){
			lifespan = left = life;
		}

		public void shuffleXY(float amt){
			x += Random.Float(-amt, amt);
			y += Random.Float(-amt, amt);
		}

		public void radiateXY(float amt){
			float hypot = (float)Math.hypot(speed.x, speed.y);
			this.x += speed.x/hypot*amt;
			this.y += speed.y/hypot*amt;
		}

		@Override
		public void update() {
			super.update();
			size(minSize + (left / lifespan)*(maxSize-minSize) + Random.Float(sizeJitter));
		}
	}
}
