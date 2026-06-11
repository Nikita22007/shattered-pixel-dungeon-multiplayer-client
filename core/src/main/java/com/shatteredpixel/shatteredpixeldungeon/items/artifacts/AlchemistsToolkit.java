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

package com.shatteredpixel.shatteredpixeldungeon.items.artifacts;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfEnergy;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class AlchemistsToolkit extends Artifact {

	{
		image = ItemSpriteSheet.ARTIFACT_TOOLKIT;
		defaultAction = AC_BREW;

		levelCap = 10;
		
		charge = 0;
		partialCharge = 0;
	}

	public static final String AC_BREW = "BREW";
	public static final String AC_ENERGIZE = "ENERGIZE";

	private float warmUpDelay;

	@Override
	public String status() {
		if (isEquipped(Dungeon.hero) && warmUpDelay > 0 && !cursed){
			return Messages.format( "%d%%", Math.max(0, 100 - (int)warmUpDelay) );
		} else {
			return super.status();
		}
	}

	public int availableEnergy(){
		return charge;
	}

	public int consumeEnergy(int amount){
		int result = amount - charge;
		charge = Math.max(0, charge - amount);
		Talent.onArtifactUsed(Dungeon.hero);
		return Math.max(0, result);
	}

	@Override
	public String desc() {
		String result = Messages.get(this, "desc");

		if (isEquipped(Dungeon.hero)) {
			if (cursed)                 result += "\n\n" + Messages.get(this, "desc_cursed");
			else if (warmUpDelay > 0)   result += "\n\n" + Messages.get(this, "desc_warming");
			else                        result += "\n\n" + Messages.get(this, "desc_hint");
		}
		
		return result;
	}
	
	@Override
	public boolean doEquip(Hero hero) {
		if (super.doEquip(hero)){
			warmUpDelay = 101f;
			return true;
		} else {
			return false;
		}
	}
	
	private static final String WARM_UP = "warm_up";

	public class kitEnergy extends ArtifactBuff {

		@Override
		public boolean act() {

			if (warmUpDelay > 0){
				if (level() == 10){
					warmUpDelay = 0;
				} else if (warmUpDelay == 101){
					warmUpDelay = 100f;
				} else {
					if (!cursed) {
						{
							float turnsToWarmUp = (int) Math.pow(10 - level(), 2);
							warmUpDelay -= 100 / turnsToWarmUp;
						}
					}
				}
				updateQuickslot();
			}

			spend(TICK);
			return true;
		}

		public void gainCharge(float levelPortion) {
			if (cursed) return;

			//generates 2 energy every hero level, +1 energy per toolkit level
			//to a max of 12 energy per hero level
			//This means that energy absorbed into the kit is recovered in 5 hero levels
			float chargeGain = (2 + level()) * levelPortion;
			chargeGain *= RingOfEnergy.artifactChargeMultiplier(target);
			partialCharge += chargeGain;

			//charge is in increments of 1 energy.
			while (partialCharge >= 1) {
				charge++;
				partialCharge -= 1;

				updateQuickslot();
			}
		}

	}

}
