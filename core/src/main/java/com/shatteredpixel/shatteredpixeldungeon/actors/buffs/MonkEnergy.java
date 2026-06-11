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

package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MeleeWeapon;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.ActionIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.AttackIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.HeroIcon;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndMonkAbilities;
import com.watabou.noosa.BitmapText;
import com.watabou.noosa.Image;
import com.watabou.noosa.Visual;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;
import com.watabou.utils.GameMath;

public class MonkEnergy extends Buff implements ActionIndicator.Action {

	{
		type = buffType.POSITIVE;
		revivePersists = true;
	}

	public float energy = 0;
	public int cooldown; //currently unused, abilities had cooldowns prior to v2.5

	private static final float MAX_COOLDOWN = 5;

	@Override
	public int icon() {
		return BuffIndicator.MONK_ENERGY;
	}

	@Override
	public void tintIcon(Image icon) {
		if (cooldown > 0){
			icon.hardlight(0.33f, 0.33f, 1f);
		} else {
			icon.resetColor();
		}
	}

	@Override
	public float iconFadePercent() {
		return GameMath.gate(0, cooldown/MAX_COOLDOWN, 1);
	}

	@Override
	public String iconTextDisplay() {
		if (cooldown > 0){
			return Integer.toString(cooldown);
		} else {
			return "";
		}
	}

	@Override
	public String desc() {
		String desc = Messages.get(this, "desc", (int)energy, energyCap());
		if (cooldown > 0){
			desc += "\n\n" + Messages.get(this, "desc_cooldown", cooldown);
		}
		return desc;
	}

	//10 at base, 20 at level 30
	public int energyCap(){
		return Math.max(10, 5 + Dungeon.hero.lvl/2);
	}

	public void abilityUsed( MonkAbility abil ){
		energy -= abil.energyCost();
		energy = Math.min(energy, energyCap());

        if (cooldown > 0 || energy < 1){
			ActionIndicator.clearAction(this);
		} else {
			ActionIndicator.refresh();
		}
		BuffIndicator.refreshHero();
	}

	public boolean abilitiesEmpowered( Hero hero ){
		//100%/80%/60% energy at +1/+2/+3
		return energy/energyCap() >= 1.2f - 0.2f*hero.pointsInTalent(Talent.MONASTIC_VIGOR);
	}

	@Override
	public String actionName() {
		return Messages.get(this, "action");
	}

	@Override
	public int actionIcon() {
		return HeroIcon.MONK_ABILITIES;
	}

	@Override
	public Visual secondaryVisual() {
		BitmapText txt = new BitmapText(PixelScene.pixelFont);
		txt.text( Integer.toString((int)energy) );
		txt.hardlight(CharSprite.POSITIVE);
		txt.measure();
		return txt;
	}

	@Override
	public int indicatorColor() {
		if (abilitiesEmpowered(Dungeon.hero)){
			return 0xAAEE22;
		} else {
			return 0xA08840;
		}
	}

	@Override
	public void doAction() {
		GameScene.show(new WndMonkAbilities(this));
	}

	public static abstract class MonkAbility {

		public static MonkAbility[] abilities = new MonkAbility[]{
				new Flurry(),
				new Focus(),
				new Dash(),
				new DragonKick(),
				new Meditate()
		};

		public String name(){
			return Messages.get(this, "name");
		}

		public String desc(){
            return Messages.get(this, "desc");
        }

		public abstract int energyCost();

		public boolean usable(MonkEnergy buff){
			return buff.energy >= energyCost();
		}

		public String targetingPrompt(){
			return null; //return a string if uses targeting
		}

		public abstract void doAbility(Hero hero, Integer target );


		public static class Flurry extends MonkAbility {

			@Override
			public int energyCost() {
				return 1;
			}

			@Override
			public boolean usable(MonkEnergy buff) {
				if (!super.usable(buff)) return false;
				return true;
			}

			@Override
			public String desc() {
				{
					//1.5x hero unarmed damage (rounds the result)
					return Messages.get(this, "desc", 2, Math.round(1.5f*(Dungeon.hero.STR()-8)));
				}

			}

			@Override
			public String targetingPrompt() {
				return Messages.get(MeleeWeapon.class, "prompt");
			}

			@Override
			public void doAbility(Hero hero, Integer target) {
				if (target == null || target == -1){
					return;
				}

				Char enemy = Actor.findChar(target);
				if (enemy == null || enemy == hero || !Dungeon.level.heroFOV[target]) {
					GLog.w(Messages.get(MeleeWeapon.class, "ability_no_target"));
					return;
				}



				if (!hero.canAttack(enemy)) {
					GLog.w(Messages.get(MeleeWeapon.class, "ability_target_range"));
					return;
				}

				hero.sprite.attack(enemy.pos, new Callback() {
					@Override
					public void call() {
						AttackIndicator.target(enemy);

						if (enemy.isAlive()){
							hero.sprite.attack(enemy.pos, new Callback() {
								@Override
								public void call() {
                                    hero.next();
								}
							});
						} else {
                            hero.next();
						}
					}
				});
			}
		}

		public static class Focus extends MonkAbility {

			@Override
			public int energyCost() {
				return 2;
			}

			@Override
			public boolean usable(MonkEnergy buff) {
				if (!super.usable(buff)) return false;
				return true;
			}

			@Override
			public void doAbility(Hero hero, Integer target) {

				if (((MonkEnergy) null).abilitiesEmpowered(hero)){
					hero.next();
				} else {
					hero.spendAndNext(1f);
				}
				((MonkEnergy) null).abilityUsed(this);
			}

		}

		public static class Dash extends MonkAbility {

			@Override
			public int energyCost() {
				return 3;
			}

			@Override
			public String targetingPrompt() {
				return Messages.get(this, "prompt");
			}

			@Override
			public void doAbility(Hero hero, Integer target) {
				if (target == null || target == -1){
					return;
				}

				int range = 4;

				if (Dungeon.hero.rooted){
					PixelScene.shake( 1, 1f );
					GLog.w(Messages.get(MeleeWeapon.class, "ability_target_range"));
					return;
				}

				if (Dungeon.level.distance(hero.pos, target) > range){
					GLog.w(Messages.get(MeleeWeapon.class, "ability_target_range"));
					return;
				}

				if (Actor.findChar(target) != null){
					GLog.w(Messages.get(MeleeWeapon.class, "ability_occupied"));
					return;
				}

				Ballistica dash = new Ballistica(hero.pos, target, Ballistica.PROJECTILE);

				if (!dash.collisionPos.equals(target)
						|| (Dungeon.level.solid[target] && !Dungeon.level.passable[target])){
					GLog.w(Messages.get(MeleeWeapon.class, "ability_target_range"));
					return;
				}

				hero.busy();
				Sample.INSTANCE.play(Assets.Sounds.MISS);
				hero.sprite.emitter().start(Speck.factory(Speck.JET), 0.01f, Math.round(4 + 2*Dungeon.level.trueDistance(hero.pos, target)));
				hero.sprite.jump(hero.pos, target, 0, 0.1f, new Callback() {
					@Override
					public void call() {
						if (Dungeon.level.map[hero.pos] == Terrain.OPEN_DOOR) {

						}
						hero.pos = target;
						Dungeon.level.occupyCell(hero);
						hero.next();
					}
				});

			}
		}

		public static class DragonKick extends MonkAbility {

			@Override
			public int energyCost() {
				return 4;
			}

			@Override
			public String desc() {
				//6x hero unarmed damage
				return Messages.get(this, "desc", 6, 6 * (Dungeon.hero.STR() - 8));
			}


			@Override
			public String targetingPrompt() {
				return Messages.get(MeleeWeapon.class, "prompt");
			}

			@Override
			public void doAbility(Hero hero, Integer target) {
				if (target == null || target == -1){
					return;
				}

				Char enemy = Actor.findChar(target);
				if (enemy == null || enemy == hero || !Dungeon.level.heroFOV[target]) {
					GLog.w(Messages.get(MeleeWeapon.class, "ability_no_target"));
					return;
				}

				if (!hero.canAttack(enemy)){
					GLog.w(Messages.get(MeleeWeapon.class, "ability_target_range"));
					return;
				}

				hero.sprite.attack(enemy.pos, new Callback() {
					@Override
					public void call() {
						AttackIndicator.target(enemy);
                        {
                            //trace a ballistica to our target (which will also extend past them
                            Ballistica trajectory = new Ballistica(hero.pos, enemy.pos, Ballistica.STOP_TARGET);
                            //trim it to just be the part that goes past them
                            trajectory = new Ballistica(trajectory.collisionPos, trajectory.path.get(trajectory.path.size() - 1), Ballistica.PROJECTILE);
                            //knock them back along that ballistica


						}
                    }
				});
			}
		}

		public static class Meditate extends MonkAbility {

			@Override
			public int energyCost() {
				return 5;
			}

			@Override
			public void doAbility(Hero hero, Integer target) {

				hero.sprite.operate(hero.pos);
				GameScene.flash(0x88000000, false);
				Sample.INSTANCE.play(Assets.Sounds.SCAN);

				for (Buff b : hero.buffs()){
					if (b.type == buffType.NEGATIVE){
						b.detach();
					}
				}

				//we process this as 5x wait actions instead of one 5 tick action to prevent
				// effects like time freeze from eating the whole action duration
				for (int i = 0; i < 5; i++) hero.spendConstant(Actor.TICK);

				if (((MonkEnergy) null).abilitiesEmpowered(hero)){
					int toHeal = Math.round((hero.HT - hero.HP)/5f);
					if (toHeal > 0) {
					}
					hero.cooldown();
				}


				hero.next();
				hero.busy();
			}

			;
		}

	}
}
