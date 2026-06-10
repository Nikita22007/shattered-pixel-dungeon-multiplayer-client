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

package com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.duelist;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FlavourBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.ArmorAbility;
import com.shatteredpixel.shatteredpixeldungeon.effects.FloatingText;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.HeroIcon;
import com.watabou.noosa.audio.Sample;

public class Challenge extends ArmorAbility {

	protected float baseChargeUse = 35;

	{
		baseChargeUse = 35;
	}

	@Override
	public int icon() {
		return HeroIcon.CHALLENGE;
	}

	@Override
	public String targetingPrompt() {
		return Messages.get(this, "prompt");
	}

	@Override
	public int targetedPos(Char user, int dst) {
		return dst;
	}

	@Override
	public Talent[] talents() {
		return new Talent[]{Talent.CLOSE_THE_GAP, Talent.INVIGORATING_VICTORY, Talent.ELIMINATION_MATCH, Talent.HEROIC_ENERGY};
	}

	public static class EliminationMatchTracker extends FlavourBuff{};

	public static class DuelParticipant extends Buff {

		public static float DURATION = 10f;

		private int left = (int)DURATION;
		private int takenDmg = 0;

		@Override
		public int icon() {
			return BuffIndicator.CHALLENGE;
		}

		@Override
		public float iconFadePercent() {
			return Math.max(0, (DURATION - left) / DURATION);
		}

		@Override
		public String iconTextDisplay() {
			return Integer.toString(left);
		}

		public void addDamage(int dmg){
			takenDmg += dmg;
		}

		@Override
		public boolean act() {

			left--;
			if (left == 0) {
				detach();
			} else {
				Char other = null;
				for (Char ch : Actor.chars()){
					if (ch != target) {
                    }
				}

				if (other == null
					|| target.alignment == other.alignment
					|| Dungeon.level.distance(target.pos, other.pos) > 5) {
					detach();
				}
			}

			spend(TICK);
			return true;
		}

		@Override
		public void detach() {
			super.detach();
			if (target != Dungeon.hero){
				if (!target.isAlive() || target.alignment == Dungeon.hero.alignment){
					Sample.INSTANCE.play(Assets.Sounds.BOSS);

					if (Dungeon.hero.hasTalent(Talent.INVIGORATING_VICTORY)){
                        DuelParticipant heroBuff = null;

						int hpToHeal = 0;
						if (heroBuff != null){
							hpToHeal = heroBuff.takenDmg;
						}

						//heals for 30%/50%/65%/75% of taken damage plus 5/10/15/20 bonus, based on talent points
						hpToHeal = (int)Math.round(hpToHeal * (1f - Math.pow(0.707f, Dungeon.hero.pointsInTalent(Talent.INVIGORATING_VICTORY))));
						hpToHeal += 5*Dungeon.hero.pointsInTalent(Talent.INVIGORATING_VICTORY);
						hpToHeal = Math.min(hpToHeal, Dungeon.hero.HT - Dungeon.hero.HP);
						if (hpToHeal > 0){
							Dungeon.hero.HP += hpToHeal;
							Dungeon.hero.sprite.emitter().start( Speck.factory( Speck.HEALING ), 0.33f, 6 );
							Dungeon.hero.sprite.showStatusWithIcon( CharSprite.POSITIVE, Integer.toString(hpToHeal), FloatingText.HEALING );
						}
					}
				}

			} else {
				if (Dungeon.hero.isAlive()) {
					GameScene.flash(0x80FFFFFF);

					if (Dungeon.hero.hasTalent(Talent.ELIMINATION_MATCH)){
                    }
				}
			}

			for (Char ch : Actor.chars()) {
			}
		}

		@Override
		public String desc() {
			return Messages.get(this, "desc", left);
		}

		private static final String LEFT = "left";
		private static final String TAKEN_DMG = "taken_dmg";

	}

}
