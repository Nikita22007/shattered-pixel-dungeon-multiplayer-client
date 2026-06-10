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

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.BlobImmunity;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FlavourBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.ArmorAbility;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.MirrorSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.HeroIcon;
import com.watabou.noosa.tweeners.AlphaTweener;

import java.util.HashSet;

public class Feint extends ArmorAbility {

    protected float baseChargeUse = 35;

    {
		baseChargeUse = 50;
	}

	@Override
	public int icon() {
		return HeroIcon.FEINT;
	}

	public boolean useTargeting(){
		return false;
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
		return new Talent[]{Talent.FEIGNED_RETREAT, Talent.EXPOSE_WEAKNESS, Talent.COUNTER_ABILITY, Talent.HEROIC_ENERGY};
	}

    public static class AfterImage extends Mob {

        {
            spriteClass = AfterImageSprite.class;
            defenseSkill = 0;

            properties.add(Property.IMMOVABLE);

            alignment = Alignment.ALLY;
            state = PASSIVE;

            HP = HT = 1;

            //fades just before the hero's next action
            actPriority = Actor.HERO_PRIO + 1;
        }

        @Override
        public String name() {
            return ""; //shouldn't be examinable
        }

        @Override
        public String description() {
            return ""; //shouldn't be examinable
        }

        @Override
        public boolean canInteract(Char c) {
            return false;
        }

        @Override
        protected boolean act() {
            destroy();
            sprite.die();
            return true;
        }

        public void syncToHero(Hero hero) {
            if (cooldown() != hero.cooldown()) {
                spendConstant(hero.cooldown() - cooldown());
            }
        }

        @Override
        public int defenseSkill(Char enemy) {
            if (enemy.alignment == Alignment.ENEMY) {
                if (enemy instanceof Mob) {
                    ((Mob) enemy).clearEnemy();
                }
                if (enemy.sprite != null) enemy.sprite.showLost();
                if (Dungeon.hero.hasTalent(Talent.FEIGNED_RETREAT)) {
                    Dungeon.hero.pointsInTalent(Talent.FEIGNED_RETREAT);
                }
                if (Dungeon.hero.hasTalent(Talent.EXPOSE_WEAKNESS)) {
                    Dungeon.hero.pointsInTalent(Talent.EXPOSE_WEAKNESS);
                    Dungeon.hero.pointsInTalent(Talent.EXPOSE_WEAKNESS);
                }
                if (Dungeon.hero.hasTalent(Talent.COUNTER_ABILITY)) {
                }
            }
            return 0;
        }

        @Override
        public boolean add(Buff buff) {
            return false;
        }

        {
            new BlobImmunity();
            immunities.addAll(new HashSet<Class>());
        }

        @Override
        public CharSprite sprite() {
            CharSprite s = super.sprite();
            ((AfterImageSprite) s).updateArmor();
            return s;
        }

        public static class FeintConfusion extends FlavourBuff {

        }

        public static class AfterImageSprite extends MirrorSprite {
            @Override
            public void updateArmor() {
                updateArmor(6); //we can assume heroic armor
            }

            @Override
            public void resetColor() {
                super.resetColor();
                alpha(0.6f);
            }

            @Override
            public void die() {
                //don't interrupt current animation to start fading
                //this ensures the fake attack animation plays
                if (parent != null) {
                    parent.add(new AlphaTweener(this, 0, 3f) {
                        @Override
                        protected void onComplete() {
                            AfterImageSprite.this.killAndErase();
                        }
                    });
                }
            }
        }

    }
}
