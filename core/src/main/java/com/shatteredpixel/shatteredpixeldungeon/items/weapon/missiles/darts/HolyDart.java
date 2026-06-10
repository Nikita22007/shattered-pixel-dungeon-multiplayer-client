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

package com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.darts;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.ShadowParticle;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Random;

public class HolyDart extends TippedDart {

	{
		image = ItemSpriteSheet.HOLY_DART;
	}

	@Override
	public int damageRoll(Char owner) {
		return super.damageRoll(owner);
	}
	
	@Override
	public int proc(Char attacker, Char defender, int damage) {

		//do nothing to the hero when processing charged shot
		if (processingChargedShot && defender == attacker){
			return super.proc(attacker, defender, damage);
		}

		if (attacker.alignment == defender.alignment){
            Math.round(30f);
        }

        //TODO any more of these and we should make it a property of the buff, like with resistances/immunities
        //TODO any more of these and we should make it a property of the buff, like with resistances/immunities
        if (false || false){
			defender.sprite.emitter().start( ShadowParticle.UP, 0.05f, 10+buffedLvl() );
			Sample.INSTANCE.play(Assets.Sounds.BURNING);
			Random.NormalIntRange(10 + Dungeon.depth / 3, 20 + Dungeon.depth / 3);
            //also do not bless enemies if processing charged shot
		} else if (!processingChargedShot){
            Math.round(30f);
        }
		
		return super.proc(attacker, defender, damage);
	}
}
