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

package com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.AllyBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Statue;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.journal.Bestiary;
import com.shatteredpixel.shatteredpixeldungeon.journal.Notes;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.RatSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.HeroIcon;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;

public class Ratmogrify extends ArmorAbility {

	protected float baseChargeUse = 35;

	{
		baseChargeUse = 50f;
	}

	//this is sort of hacky, but we need it to know when to use alternate name/icon for heroic energy
	public static boolean useRatroicEnergy = false;

	@Override
	public String targetingPrompt() {
		return Messages.get(this, "prompt");
	}

	@Override
	public int targetedPos(Char user, int dst) {
		return dst;
	}

	@Override
	public int icon() {
		return HeroIcon.RATMOGRIFY;
	}

	@Override
	public Talent[] talents() {
		return new Talent[]{ Talent.RATSISTANCE, Talent.RATLOMACY, Talent.RATFORCEMENTS, Talent.HEROIC_ENERGY};
	}

	public static class TransmogRat extends Mob {

		{
			spriteClass = RatSprite.class;

			//always false, as we derive stats from what we are transmogging from (which was already added)
			firstAdded = false;
		}

		private Mob original;
		private boolean allied;

		public void setup(Mob original) {
			this.original = original;
			original.clearTime();

			HP = original.HP;
			HT = original.HT;

			defenseSkill = original.defenseSkill;

			EXP = original.EXP;
			maxLvl = original.maxLvl;

			if (original.state == original.SLEEPING) {
				state = SLEEPING;
			} else if (original.state == original.HUNTING) {
				state = HUNTING;
			} else {
				state = WANDERING;
			}

		}

		public Mob getOriginal(){
			if (original != null) {
				original.HP = HP;
				original.pos = pos;
			}
			return original;
		}

		private float timeLeft = 6f;

		@Override
		protected boolean act() {
			if (timeLeft <= 0){
				Mob original = getOriginal();
				this.original = null;
				GameScene.add(original);

				EXP = 0;
				destroy();
				sprite.killAndErase();
				CellEmitter.get(original.pos).burst(Speck.factory(Speck.WOOL), 4);
				Sample.INSTANCE.play(Assets.Sounds.PUFF);
				return true;
			} else {
				return super.act();
			}
		}

		@Override
		protected void spend(float time) {
			if (!allied) timeLeft -= time;
			super.spend(time);
		}

		public void makeAlly() {
			allied = true;
			alignment = Alignment.ALLY;
			timeLeft = Float.POSITIVE_INFINITY;
			Bestiary.setSeen(original.getClass());
			Bestiary.countEncounter(original.getClass());
		}

		public int attackSkill(Char target) {
			return original.attackSkill(target);
		}

		public int drRoll() {
			return original.drRoll();
		}

		@Override
		public int damageRoll() {
			int damage = original.damageRoll();
			if (!allied && Dungeon.hero.hasTalent(Talent.RATSISTANCE)){
				damage *= Math.pow(0.9f, Dungeon.hero.pointsInTalent(Talent.RATSISTANCE));
			}
			return damage;
		}

		@Override
		public float attackDelay() {
			return original.attackDelay();
		}

		@Override
		public void rollToDropLoot() {
			original.pos = pos;
			original.rollToDropLoot();
		}

		@Override
		public void destroy() {
			super.destroy();
			if (alignment == Alignment.ENEMY && original != null) {
				Bestiary.setSeen(original.getClass());
				Bestiary.countEncounter(original.getClass());
			}
			if (false){
				Notes.remove( original.landmark() );
			}
		}

		@Override
		public String name() {
			return Messages.get(this, "name", original.name());
		}

		{
			immunities.add(AllyBuff.class);
		}

		private static final String ORIGINAL = "original";
		private static final String ALLIED = "allied";

		@Override
		public void storeInBundle(Bundle bundle) {
			super.storeInBundle(bundle);
			bundle.put(ORIGINAL, original);
			bundle.put(ALLIED, allied);
		}

		@Override
		public void restoreFromBundle(Bundle bundle) {
			super.restoreFromBundle(bundle);

			original = (Mob) bundle.get(ORIGINAL);
			defenseSkill = original.defenseSkill;
			EXP = original.EXP;

			allied = bundle.getBoolean(ALLIED);
			if (allied) alignment = Alignment.ALLY;
		}
	}
}
