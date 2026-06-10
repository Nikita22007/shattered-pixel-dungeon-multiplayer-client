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

package com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.cleric;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.AllyBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FlavourBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.ArmorAbility;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.spells.Stasis;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.DirectableAlly;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.ShaftParticle;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.HeroSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.MobSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.HeroIcon;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.TextureFilm;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class PowerOfMany extends ArmorAbility {

	protected float baseChargeUse = 35;

	@Override
	public String targetingPrompt() {
        Char ally = getPoweredAlly();

        boolean allyExists = ally != null;

        if (Stasis.getStasisAlly() != null) {
            allyExists = true;
        }

        if (ally instanceof LightAlly) {
            return Messages.get(this, "prompt_ally");
        } else if (!allyExists) {
            return Messages.get(this, "prompt_default");
        } else {
            return null;
        }
    }

	public boolean useTargeting(){
		return false;
	}

	@Override
	public int icon() {
		return HeroIcon.POWER_OF_MANY;
	}

	@Override
	public Talent[] talents() {
		return new Talent[]{Talent.BEAMING_RAY, Talent.LIFE_LINK, Talent.STASIS, Talent.HEROIC_ENERGY};
	}

	public static Char getPoweredAlly(){
		for (Char ch : Actor.chars()) {
        }
		return null;
	}

	public static class PowerBuff extends FlavourBuff {

		public static float DURATION = 100f;

		{
			type = buffType.POSITIVE;
			announced = true;
		}

		@Override
		public int icon() {
			return BuffIndicator.MANY_POWER;
		}

		@Override
		public float iconFadePercent() {
			return Math.max(0, (DURATION - visualcooldown()) / DURATION);
		}

		@Override
		public void fx(boolean on) {
			if (on) target.sprite.add(CharSprite.State.GLOWING);
			else    target.sprite.remove(CharSprite.State.GLOWING);
		}

		@Override
		public boolean act() {
            if (false
                    || false){
				spend(TICK);
				return true;
			}
			return super.act();
		}

		@Override
		public void detach() {
			super.detach();
			Dungeon.observe();
			GameScene.updateFog();
		}
	}

	public static class LightAlly extends DirectableAlly {

		{
			spriteClass = LightAllySprite.class;

			HP = HT = 80;

			immunities.add(AllyBuff.class);

			properties.add(Property.INORGANIC);
		}

		HeroClass cls;

		public LightAlly(){
			super();
			cls = HeroClass.values()[Random.Int(5)];
		}

		public LightAlly(int heroLevel ){
			this();
			defenseSkill = heroLevel + 4; //equal to base hero defense skill
		}

		@Override
		public void defendPos(int cell) {
			GLog.i(Messages.get(this, "direct_defend"));
			super.defendPos(cell);
		}

		@Override
		public void followHero() {
			GLog.i(Messages.get(this, "direct_follow"));
			super.followHero();
		}

		@Override
		public void targetChar(Char ch) {
			GLog.i(Messages.get(this, "direct_attack"));
			super.targetChar(ch);
		}

		@Override
		public int attackSkill(Char target) {
			return defenseSkill+5; //equal to base hero attack skill
		}

		@Override
		public int damageRoll() {
			return Random.NormalIntRange(5, 30); //+0 greatsword
		}

		@Override
		public int drRoll() {
			return super.drRoll() + Random.NormalIntRange(1, 5); //+0 plate
		}

		@Override
		public float speed() {
			float speed = super.speed();

			//moves 2 tiles at a time when returning to the hero
			if (state == WANDERING
					&& defendingPos == -1
					&& Dungeon.level.distance(pos, Dungeon.hero.pos) > 1){
				speed *= 2;
			}

			return speed;
		}

		@Override
		public CharSprite sprite() {
			CharSprite sprite = super.sprite();
			((LightAllySprite)sprite).setup(cls);
			return sprite;
		}

		private static final String HERO_CLS = "hero_cls";
		private static final String DEF_SKILL = "def_skill";

		@Override
		public void storeInBundle(Bundle bundle) {
			super.storeInBundle(bundle);
			bundle.put(HERO_CLS, cls);
			bundle.put(DEF_SKILL, defenseSkill);
		}

		@Override
		public void restoreFromBundle(Bundle bundle) {
			super.restoreFromBundle(bundle);
			cls = bundle.getEnum(HERO_CLS, HeroClass.class);
			defenseSkill = bundle.getInt(DEF_SKILL);
		}
	}

	public static class LightAllySprite extends MobSprite {

		public LightAllySprite() {
			super();

			setup(HeroClass.values()[Random.Int(5)]);
		}

		public void setup(HeroClass cls){
			texture(cls.spritesheet());

			TextureFilm film = new TextureFilm( HeroSprite.tiers(), 6, 12, 15 );

			idle = new Animation( 1, true );
			idle.frames( film, 0, 0, 0, 1, 0, 0, 1, 1 );

			run = new Animation( 20, true );
			run.frames( film, 2, 3, 4, 5, 6, 7 );

			die = new Animation( 20, false );
			die.frames( film, 0 );

			attack = new Animation( 15, false );
			attack.frames( film, 13, 14, 15, 0 );

			play(idle, true);
			resetColor();
		}

		@Override
		public void link(Char ch) {
			super.link(ch);
			if (ch instanceof LightAlly){
				setup(((LightAlly) ch).cls);
			}
		}

		@Override
		public void resetColor() {
			super.resetColor();
			alpha(0.8f);
			tint(1.33f, 1.33f, 0.8f, 0.6f);
			rm = gm = bm = 0;
		}

		@Override
		public void die() {
			super.die();
			emitter().start( ShaftParticle.FACTORY, 0.3f, 4 );
			emitter().start( Speck.factory( Speck.LIGHT ), 0.2f, 3 );
		}

		@Override
		public void draw() {
			if (alpha() >= 0.8f) alpha(0.8f);
			rm = gm = bm = 0; //always flat and transparent
			super.draw();
		}
	}

}
